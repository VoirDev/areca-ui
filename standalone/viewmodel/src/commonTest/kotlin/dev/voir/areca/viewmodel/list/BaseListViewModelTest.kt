package dev.voir.areca.viewmodel.list

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import dev.voir.areca.viewmodel.UiState
import dev.voir.areca.viewmodel.displayOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Verifies the [BaseListViewModel] state machine with a fake, virtual-time page source. */
@OptIn(ExperimentalCoroutinesApi::class)
class BaseListViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `a page size below one is rejected at construction`() {
        assertFailsWith<IllegalArgumentException> { ColorsViewModel(FakePageSource(), pageSize = 0) }
        assertFailsWith<IllegalArgumentException> { ColorsViewModel(FakePageSource(), pageSize = -1) }
    }

    @Test
    fun `load shows full screen loading and then the first page`() = runTest {
        val source = FakePageSource()
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)
        runCurrent()
        assertEquals(UiState.Loading, viewModel.state.value)

        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("all-1a", "all-1b"), list.items)
        assertEquals(1, list.pagingMeta.currentPage)
        assertTrue(list.pagingMeta.hasNextPage)
    }

    @Test
    fun `load failure shows a full screen error`() = runTest {
        val source = FakePageSource(failingQueries = setOf("all"))
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)

        advanceUntilIdle()

        assertIs<UiState.Error>(viewModel.state.value)
    }

    @Test
    fun `refresh from an error screen stays full screen`() = runTest {
        val source = FakePageSource(failingQueries = setOf("all"))
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)
        advanceUntilIdle()

        viewModel.dispatch(ColorsAction.Refresh)
        runCurrent()
        assertEquals(UiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertIs<UiState.Error>(viewModel.state.value)
        assertEquals(emptyList(), viewModel.refreshFailures)
    }

    @Test
    fun `refresh keeps the items and replaces them with page one`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)
        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()
        source.version = "v2"

        viewModel.dispatch(ColorsAction.Refresh)
        runCurrent()
        val refreshing = viewModel.list()
        assertTrue(refreshing.isRefreshing)
        assertEquals(4, refreshing.items.size)
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("all-1a-v2", "all-1b-v2"), list.items)
        assertEquals(1, list.pagingMeta.currentPage)
        assertFalse(list.isRefreshing)
    }

    @Test
    fun `failed refresh keeps the items and records the failure`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)
        source.failingQueries = setOf("all")

        viewModel.dispatch(ColorsAction.Refresh)
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("all-1a", "all-1b"), list.items)
        assertFalse(list.isRefreshing)
        assertIs<IllegalStateException>(list.refreshError)
        assertEquals(list.refreshError, viewModel.refreshFailures.single())
    }

    @Test
    fun `a new refresh clears the previous refresh failure`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)
        source.failingQueries = setOf("all")
        viewModel.dispatch(ColorsAction.Refresh)
        advanceUntilIdle()
        source.failingQueries = emptySet()

        viewModel.dispatch(ColorsAction.Refresh)
        runCurrent()
        assertNull(viewModel.list().refreshError)
        advanceUntilIdle()

        assertNull(viewModel.list().refreshError)
    }

    @Test
    fun `inline reload keeps the current items visible`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.Filter("red", ReloadStrategy.Inline))
        runCurrent()

        val reloading = viewModel.list()
        assertTrue(reloading.isReloading)
        assertEquals("red", reloading.filters)
        assertEquals(listOf("all-1a", "all-1b"), reloading.items)
        assertEquals(1, reloading.pagingMeta.currentPage)
        advanceUntilIdle()
        assertEquals(listOf("red-1a", "red-1b"), viewModel.list().items)
    }

    @Test
    fun `failed inline reload restores the previous query and records the failure`() = runTest {
        val source = FakePageSource(failingQueries = setOf("red"))
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.Filter("red", ReloadStrategy.Inline))
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("all-1a", "all-1b"), list.items)
        assertNull(list.filters)
        assertEquals(1, list.pagingMeta.currentPage)
        assertTrue(list.pagingMeta.hasNextPage)
        assertFalse(list.isReloading)
        assertIs<IllegalStateException>(list.refreshError)
        assertEquals(1, viewModel.refreshFailures.size)
    }

    @Test
    fun `full screen filter failure keeps the new filter for retry`() = runTest {
        val source = FakePageSource(failingQueries = setOf("red"))
        val viewModel = loadedViewModel(source)
        viewModel.dispatch(ColorsAction.Filter("red", ReloadStrategy.FullScreen))
        advanceUntilIdle()
        assertIs<UiState.Error>(viewModel.state.value)
        source.failingQueries = emptySet()

        viewModel.dispatch(ColorsAction.Retry)
        advanceUntilIdle()

        assertEquals(listOf("red-1a", "red-1b"), viewModel.list().items)
        assertEquals("red", viewModel.list().filters)
    }

    @Test
    fun `inline reload from an error screen is full screen`() = runTest {
        val source = FakePageSource(failingQueries = setOf("all"))
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)
        advanceUntilIdle()

        viewModel.dispatch(ColorsAction.Filter("red", ReloadStrategy.Inline))
        runCurrent()

        assertEquals(UiState.Loading, viewModel.state.value)
        advanceUntilIdle()
        assertEquals("red", viewModel.list().filters)
    }

    @Test
    fun `next page appends the page and advances paging`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.LoadNextPage)
        runCurrent()
        assertTrue(viewModel.list().isLoadingNextPage)
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("all-1a", "all-1b", "all-2a", "all-2b"), list.items)
        assertEquals(2, list.pagingMeta.currentPage)
        assertFalse(list.isLoadingNextPage)
    }

    @Test
    fun `queued next page requests load a single page`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)

        repeat(3) { viewModel.dispatch(ColorsAction.LoadNextPage) }
        advanceUntilIdle()

        assertEquals(listOf(1, 2), source.requestedPages)
        assertEquals(4, viewModel.list().items.size)
    }

    @Test
    fun `next page stops at the end of the list`() = runTest {
        val source = FakePageSource(lastPage = 2)
        val viewModel = loadedViewModel(source)
        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()

        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()

        assertEquals(listOf(1, 2), source.requestedPages)
        assertFalse(viewModel.list().pagingMeta.hasNextPage)
    }

    @Test
    fun `next page failure is recorded and retry loads the page`() = runTest {
        val source = FakePageSource(failingPages = setOf(2))
        val viewModel = loadedViewModel(source)
        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()
        val failed = viewModel.list()
        assertIs<IllegalStateException>(failed.nextPageError)
        assertEquals(2, failed.items.size)
        source.failingPages = emptySet()

        viewModel.dispatch(ColorsAction.RetryNextPage)
        runCurrent()
        assertNull(viewModel.list().nextPageError)
        advanceUntilIdle()

        assertEquals(4, viewModel.list().items.size)
        assertNull(viewModel.list().nextPageError)
    }

    @Test
    fun `next page on an error screen is ignored`() = runTest {
        val source = FakePageSource(failingQueries = setOf("all"))
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)
        advanceUntilIdle()

        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()

        assertIs<UiState.Error>(viewModel.state.value)
        assertEquals(listOf(1), source.requestedPages)
    }

    @Test
    fun `clearing the ViewModel during a next page load records no error`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)
        val store = ViewModelStore()
        store.put("colors", viewModel)
        source.hangingPages = setOf(2)
        viewModel.dispatch(ColorsAction.LoadNextPage)
        runCurrent()

        store.clear()
        advanceUntilIdle()

        assertNull(viewModel.list().nextPageError)
    }

    @Test
    fun `a replaced list discards a late next page`() = runTest {
        val source = FakePageSource(nextPageDelayMillis = 1_000)
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.LoadNextPage)
        runCurrent()
        assertTrue(viewModel.list().isLoadingNextPage)
        viewModel.dispatch(ColorsAction.Filter("red", ReloadStrategy.Inline))
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("red-1a", "red-1b"), list.items)
        assertEquals("red", list.filters)
        assertEquals(1, list.pagingMeta.currentPage)
        assertFalse(list.isLoadingNextPage)
    }

    @Test
    fun `next page is ignored while a latest wins reload runs in its own job`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.FilterLatestWins("red"))
        runCurrent()
        assertTrue(viewModel.list().isReloading)
        viewModel.dispatch(ColorsAction.LoadNextPage)
        advanceUntilIdle()

        assertEquals(listOf("red-1a", "red-1b"), viewModel.list().items)
        assertEquals(listOf(1, 1), source.requestedPages)
    }

    @Test
    fun `a superseded latest wins reload leaves only the newest query`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)

        viewModel.dispatch(ColorsAction.FilterLatestWins("red"))
        runCurrent()
        advanceTimeBy(50)
        viewModel.dispatch(ColorsAction.FilterLatestWins("blue"))
        advanceUntilIdle()

        val list = viewModel.list()
        assertEquals(listOf("blue-1a", "blue-1b"), list.items)
        assertEquals("blue", list.filters)
        assertFalse(list.isReloading)
        assertNull(list.refreshError)
        assertEquals(emptyList(), viewModel.refreshFailures)
    }

    @Test
    fun `a cancelled refresh clears its flag and reports no failure`() = runTest {
        val source = FakePageSource()
        val viewModel = loadedViewModel(source)
        source.hangingPages = setOf(1)

        viewModel.dispatch(ColorsAction.RefreshInJob)
        runCurrent()
        assertTrue(viewModel.list().isRefreshing)
        viewModel.dispatch(ColorsAction.CancelJob)
        advanceUntilIdle()

        val list = viewModel.list()
        assertFalse(list.isRefreshing)
        assertNull(list.refreshError)
        assertEquals(emptyList(), viewModel.refreshFailures)
    }

    @Test
    fun `a result for another page counts as a failure`() = runTest {
        val source = FakePageSource(pageOffset = -1)
        val viewModel = ColorsViewModel(source)

        viewModel.dispatch(ColorsAction.Load)
        advanceUntilIdle()

        val error = assertIs<UiState.Error>(viewModel.state.value)
        assertIs<IllegalStateException>(error.cause)
    }

    /**
     * Creates a ViewModel over [source] with page 1 loaded.
     *
     * @param source Fake page source to read from.
     * @return The loaded ViewModel.
     */
    private fun TestScope.loadedViewModel(
        source: FakePageSource,
    ): ColorsViewModel {
        val viewModel = ColorsViewModel(source)
        viewModel.dispatch(ColorsAction.Load)
        advanceUntilIdle()
        source.requestedPages.clear()
        source.requestedPages += 1
        return viewModel
    }
}

/**
 * Reads the displayed list, failing when the screen does not show one.
 *
 * @return The displayed list state.
 */
private fun ColorsViewModel.list(): ListState<String, String, Unit> =
    assertNotNull(state.value.displayOrNull())

/**
 * Fake, virtual-time page source: items are named `<query>-<page><letter>[-<version>]`.
 *
 * @property failingQueries Filters whose requests fail; `"all"` stands for no filter.
 * @property failingPages Pages whose requests fail.
 * @property lastPage Last page that exists.
 * @property nextPageDelayMillis Delay of requests for pages after the first.
 * @property pageOffset Added to the page number reported in each result.
 */
private class FakePageSource(
    var failingQueries: Set<String> = emptySet(),
    var failingPages: Set<Int> = emptySet(),
    private val lastPage: Int = 5,
    private val nextPageDelayMillis: Long = 100,
    private val pageOffset: Int = 0,
) {
    /** Pages requested so far, in order. */
    val requestedPages = mutableListOf<Int>()

    /** Pages whose requests never complete. */
    var hangingPages: Set<Int> = emptySet()

    /** Suffix added to item names, to tell a refreshed page from the original. */
    var version: String? = null

    /**
     * Returns one page.
     *
     * @param page Requested page.
     * @param filter Requested filter.
     * @return The page.
     */
    suspend fun fetch(page: Int, filter: String?): PageResult<String> {
        requestedPages += page
        if (page in hangingPages) awaitCancellation()
        delay(if (page == 1) 100 else nextPageDelayMillis)
        val query = filter ?: "all"
        check(query !in failingQueries && page !in failingPages) { "Backend unavailable" }
        val suffix = version?.let { "-$it" }.orEmpty()
        return PageResult(
            items = listOf("$query-${page}a$suffix", "$query-${page}b$suffix"),
            currentPage = page + pageOffset,
            hasNextPage = page < lastPage,
        )
    }
}

/** Inputs accepted by [ColorsViewModel]. */
private sealed interface ColorsAction {
    data object Load : ColorsAction
    data object Retry : ColorsAction
    data object Refresh : ColorsAction
    data object RefreshInJob : ColorsAction
    data object CancelJob : ColorsAction
    data object LoadNextPage : ColorsAction
    data object RetryNextPage : ColorsAction
    data class Filter(val filter: String, val strategy: ReloadStrategy) : ColorsAction
    data class FilterLatestWins(val filter: String) : ColorsAction
}

/** Filterable list used to exercise [BaseListViewModel]. */
private class ColorsViewModel(
    private val source: FakePageSource,
    pageSize: Int = 2,
) : BaseListViewModel<ColorsAction, Nothing, String, String, Unit>(pageSize = pageSize) {
    val refreshFailures = mutableListOf<Throwable>()
    private var job: Job? = null

    override suspend fun reduce(action: ColorsAction) {
        when (action) {
            ColorsAction.Load -> handleLoad()
            ColorsAction.Retry -> handleRetry()
            ColorsAction.Refresh -> handleRefresh()
            ColorsAction.RefreshInJob -> job = viewModelScope.launch { handleRefresh() }
            ColorsAction.CancelJob -> job?.cancel()
            ColorsAction.LoadNextPage -> handleLoadNextPage()
            ColorsAction.RetryNextPage -> handleRetryLoadNextPage()
            is ColorsAction.Filter -> handleUpdateFilters(action.filter, action.strategy)
            is ColorsAction.FilterLatestWins -> {
                job?.cancel()
                job = viewModelScope.launch {
                    handleUpdateFilters(action.filter, ReloadStrategy.Inline)
                }
            }
        }
    }

    override suspend fun fetchPage(page: Int, filters: String?, sorting: Unit?): PageResult<String> =
        source.fetch(page, filters)

    override suspend fun onRefreshError(throwable: Throwable) {
        refreshFailures += throwable
    }
}
