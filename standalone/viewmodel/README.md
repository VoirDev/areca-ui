# Areca ViewModel

Kotlin Multiplatform base classes for screen-level presentation logic: one immutable state stream,
serialized actions, best-effort effects, and paginated lists with refresh, filtering and sorting.
It builds on AndroidX Lifecycle `ViewModel` and Kotlin Coroutines, and does not depend on Compose or
the Areca theme.

## Install

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.voir.areca:viewmodel:<version>")
        }
    }
}
```

Use `api(…)` instead when your module exposes Areca ViewModel types in its own public API.
`viewmodel` passes on `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` and
`org.jetbrains.kotlinx:kotlinx-coroutines-core` as API dependencies. Targets: Android, JVM, iOS
ARM64 and iOS Simulator ARM64. For the repository and credentials, see
[Getting started](../../docs/getting-started.md).

## Quick start

```kotlin
data class GreetingState(val text: String = "")

sealed interface GreetingAction {
    data object Load : GreetingAction
}

class GreetingViewModel(
    private val getGreeting: GetGreetingUseCase,
) : BaseViewModel<GreetingAction, Nothing, GreetingState>(initial = GreetingState()) {

    init {
        dispatch(GreetingAction.Load)
    }

    override suspend fun reduce(action: GreetingAction) {
        when (action) {
            GreetingAction.Load -> runAction(
                block = { getGreeting() },
                onSuccess = { text -> setDisplay(GreetingState(text)) },
            )
        }
    }
}
```

## Types

| Type | Use it for | Package |
| --- | --- | --- |
| `BaseViewModel<A, E, S>` | A screen with one state stream, serialized actions and best-effort effects | `dev.voir.areca.viewmodel` |
| `UiState<S>` | The top-level screen lifecycle: `Initial`, `Loading`, `Display(data)`, `Error(message, cause)` | `dev.voir.areca.viewmodel` |
| `RefreshGate` | Dropping concurrent refresh requests and rapid repeats | `dev.voir.areca.viewmodel` |
| `BaseListViewModel<A, E, T, F, S>` | A paginated list with filtering and sorting | `dev.voir.areca.viewmodel.list` |
| `BaseSimpleListViewModel<A, E, T>` | A paginated list without filtering or sorting | `dev.voir.areca.viewmodel.list` |
| `ListState`, `PageResult`, `PagingMeta`, `ReloadStrategy` | A list's state, one fetched page, paging metadata, how a filter or sort change reloads | `dev.voir.areca.viewmodel.list` |

The module provides no dependency injection, navigation, persistence, logging or UI. ViewModels
depend on domain use cases and presentation mappers, not on repositories, data sources or DTOs.

---

## BaseViewModel

| Type parameter | Meaning | Examples |
| --- | --- | --- |
| `A` | An action sent to the reducer | Load, retry, save, item clicked |
| `E` | A best-effort, one-off UI impulse | Navigate, show a toast, focus a field |
| `S` | Durable screen data for rendering | Form fields, content, selection |

```text
UI intent -> dispatch(action) -> reduce(action) -> state and/or effect -> UI
```

- `dispatch` never suspends and never drops an action. One coroutine runs `reduce` for each action
  in dispatch order, one at a time. Actions dispatched from `init` wait until the reducer starts;
  actions pending when the ViewModel is cleared are discarded. The queue is unbounded, so do not use
  actions as a job queue.
- `state` is a `StateFlow<UiState<S>>` starting at `UiState.Display(initial)`; call `setInitial()`
  only when the screen needs an untouched initial phase.
- `effects` is a `Flow<E>`, sent with the protected `emitEffect`.
- If `reduce` throws, `onReduceFailure(action, throwable)` publishes a full-screen `UiState.Error`
  by default, and later actions keep being processed.

```kotlin
data class ProfileState(
    val profile: Profile? = null,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
)

sealed interface ProfileAction {
    data object Load : ProfileAction
    data object Retry : ProfileAction
    data object Save : ProfileAction
}

sealed interface ProfileEffect {
    data class ShowMessage(val message: String) : ProfileEffect
}

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val messages: ProfileMessages,
) : BaseViewModel<ProfileAction, ProfileEffect, ProfileState>(
    initial = ProfileState(),
) {

    init {
        load()
    }

    fun load() = dispatch(ProfileAction.Load)

    fun retry() = dispatch(ProfileAction.Retry)

    fun save() = dispatch(ProfileAction.Save)

    override suspend fun reduce(action: ProfileAction) {
        when (action) {
            ProfileAction.Load,
            ProfileAction.Retry,
            -> runAction(
                block = { getProfile() },
                onSuccess = { profile -> setDisplay(ProfileState(profile = profile)) },
            )
            ProfileAction.Save -> save(display.profile ?: return)
        }
    }

    // Full-screen errors from runAction get their text here.
    override fun errorMessage(throwable: Throwable): String = messages.loadFailed(throwable)

    private suspend fun save(profile: Profile) {
        mutateDisplay { it.copy(isSaving = true, saveFailed = false) }
        try {
            saveProfile(profile)
            mutateDisplay { it.copy(isSaving = false) }
            emitEffect(ProfileEffect.ShowMessage(messages.saved()))
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            mutateDisplay { it.copy(isSaving = false, saveFailed = true) }
        }
    }
}
```

Public methods dispatch actions; `reduce` maps each action to behaviour.

### State helpers

All protected, for the subclass:

| Helper | |
| --- | --- |
| `display` | The latest display data, even while the top-level state is loading or an error |
| `setDisplay(value)` | Replace it and publish `UiState.Display` |
| `mutateDisplay { … }` | Transform it and publish `UiState.Display` — even from an error screen |
| `currentDisplayOrNull()` | The data only while the top-level state is `Display` |
| `setLoading()`, `setInitial()`, `setError(…)` | Change the top-level state, keeping the cached `display` |
| `runAction(block, onSuccess, onFailure)` | Publish `Loading`, run `block` inline, then call `onSuccess` or `onFailure` (by default `setError`) |
| `emitEffect(effect)` | Queue an effect |

Use `runAction` for work that replaces the whole screen with loading or error UI. For inline work,
keep a loading flag in `S` and update it with `mutateDisplay`. Cancellation is never treated as a
failure.

### UiState, state and effects

`UiState` is only the top-level lifecycle. Saving, refreshing, per-item loading, validation errors,
selection and empty content belong in `S`. `UiState.displayOrNull()` returns the data of a `Display`.

| Put in state | Send as an effect |
| --- | --- |
| Loaded content, form values and validation | Navigation requests, such as leaving a form after it saves |
| Loading flags and inline errors | Toasts, snackbars and other transient messages |
| The selected item or tab | Permission prompts and focus requests |
| Outcomes the screen must keep showing — a completed payment, an acknowledgement — until the UI acknowledges them | Animations and haptics |

Effects are queued without limit and never dropped. One sent while nothing collects waits and is
delivered in order when a collector starts, so it can arrive late. Several collectors compete for
effects rather than each getting a copy: keep exactly one lifecycle-aware collector per screen.

### Ownership and threading

- Obtain ViewModels through a `ViewModelStoreOwner` — `viewModel { … }` in Compose, an activity or
  fragment, a navigation back-stack entry — so they are cleared. One held in plain `remember { … }`
  or created directly from Swift is never cleared and leaks its coroutines.
- `viewModelScope` runs on `Dispatchers.Main.immediate`. On a JVM without a Main dispatcher it falls
  back to a single-parallelism view of `Dispatchers.Default`; desktop UIs should add
  `kotlinx-coroutines-swing` (or `-javafx`) so state is published on the UI thread.
- Call the state helpers only from `reduce` or from a coroutine launched in `viewModelScope` that has
  not switched dispatcher. They are confined, not atomic: a call from another thread can lose an
  update. Keep blocking or CPU-heavy work behind the use case.
- Reducers run one at a time, so a long request delays later actions; nothing is cancelled or
  replaced automatically. For latest-wins work, cancel the previous `Job` and launch the new one in
  `viewModelScope`. Rethrow `CancellationException` from every broad failure handler in owned jobs.

---

## RefreshGate

`RefreshGate` admits one refresh, rejects requests while it runs, and rejects repeats for a cooldown
after it completes (one second by default). Rejected requests are dropped, not queued. Use one gate
per refresh owner.

1. Call `tryReserve { … }` synchronously at the user-entry point; its callback dispatches or starts
   the accepted work.
2. Wrap that work in `runReserved { … }`, which releases the reservation and starts the cooldown
   however the work ends. It throws `IllegalStateException` when no reservation is held.

Every successful `tryReserve` must lead to exactly one `runReserved`.

```kotlin
data class FeedState(
    val items: List<FeedItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val refreshFailed: Boolean = false,
)

sealed interface FeedAction {
    data object Refresh : FeedAction
}

class FeedViewModel(
    private val refreshFeed: RefreshFeedUseCase,
) : BaseViewModel<FeedAction, Nothing, FeedState>(initial = FeedState()) {
    private val refreshGate = RefreshGate()

    fun refresh() {
        refreshGate.tryReserve { dispatch(FeedAction.Refresh) }
    }

    override suspend fun reduce(action: FeedAction) {
        when (action) {
            FeedAction.Refresh -> refreshGate.runReserved { refreshItems() }
        }
    }

    private suspend fun refreshItems() {
        mutateDisplay { it.copy(isRefreshing = true, refreshFailed = false) }
        try {
            val items = refreshFeed()
            mutateDisplay { it.copy(items = items, isRefreshing = false) }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            mutateDisplay { it.copy(isRefreshing = false, refreshFailed = true) }
        }
    }
}
```

When the refresh should not hold the reducer, launch it instead of dispatching:

```kotlin
private val refreshGate = RefreshGate(cooldown = 2.seconds)

fun refresh() {
    refreshGate.tryReserve {
        viewModelScope.launch {
            refreshGate.runReserved { refreshItems() }
        }
    }
}
```

`cooldown = Duration.ZERO` only suppresses concurrent refreshes; `timeSource` takes a
`TestTimeSource` in tests.

---

## Collecting from Compose

Collect with `org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose`, so a screen in the
background stops collecting:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import dev.voir.areca.viewmodel.UiState

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    genericErrorMessage: String,
    onMessage: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is ProfileEffect.ShowMessage -> onMessage(effect.message)
                }
            }
        }
    }

    when (val current = state) {
        UiState.Initial,
        UiState.Loading,
        -> FullScreenLoading()
        is UiState.Display -> ProfileContent(current.data)
        is UiState.Error -> ErrorContent(
            message = current.message.ifEmpty { genericErrorMessage },
            onRetry = viewModel::retry,
        )
    }
}
```

---

## Paginated lists

`BaseListViewModel` holds a `ListState`: the loaded `items`, `PagingMeta`, the active `filters` and
`sorting`, refresh, reload and next-page flags, and inline `nextPageError` and `refreshError`. The
subclass implements `fetchPage` and maps its actions to the protected `handle*` functions.

```kotlin
data class ProductFilter(val query: String)

enum class ProductSort {
    Name,
    Price,
}

sealed interface ProductsAction {
    data object Load : ProductsAction
    data object Retry : ProductsAction
    data object Refresh : ProductsAction
    data object LoadNextPage : ProductsAction
    data object RetryNextPage : ProductsAction
    data class UpdateFilter(val filter: ProductFilter) : ProductsAction
    data object ClearFilter : ProductsAction
    data class UpdateSort(val sort: ProductSort) : ProductsAction
}

sealed interface ProductsEffect {
    data object RefreshFailed : ProductsEffect
}

class ProductsViewModel(
    private val getProducts: GetProductsPageUseCase,
) : BaseListViewModel<
    ProductsAction,
    ProductsEffect,
    Product,
    ProductFilter,
    ProductSort,
>(
    initialSorting = ProductSort.Name,
    pageSize = 30,
) {

    init {
        load()
    }

    fun load() = dispatch(ProductsAction.Load)
    fun retry() = dispatch(ProductsAction.Retry)
    fun refresh() = dispatch(ProductsAction.Refresh)
    fun loadNextPage() = dispatch(ProductsAction.LoadNextPage)
    fun retryNextPage() = dispatch(ProductsAction.RetryNextPage)
    fun updateFilter(filter: ProductFilter) = dispatch(ProductsAction.UpdateFilter(filter))
    fun clearFilter() = dispatch(ProductsAction.ClearFilter)
    fun updateSort(sort: ProductSort) = dispatch(ProductsAction.UpdateSort(sort))

    override suspend fun reduce(action: ProductsAction) {
        when (action) {
            ProductsAction.Load -> handleLoad()
            ProductsAction.Retry -> handleRetry()
            ProductsAction.Refresh -> handleRefresh()
            ProductsAction.LoadNextPage -> handleLoadNextPage()
            ProductsAction.RetryNextPage -> handleRetryLoadNextPage()
            is ProductsAction.UpdateFilter -> handleUpdateFilters(
                filters = action.filter,
                strategy = ReloadStrategy.FullScreen,
            )
            ProductsAction.ClearFilter -> handleClearFilters()
            is ProductsAction.UpdateSort -> handleUpdateSorting(
                sorting = action.sort,
                strategy = ReloadStrategy.Inline,
            )
        }
    }

    override suspend fun fetchPage(
        page: Int,
        filters: ProductFilter?,
        sorting: ProductSort?,
    ): PageResult<Product> {
        val products = getProducts(
            page = page,
            pageSize = pageSize,
            query = filters?.query,
            sort = sorting,
        )

        return PageResult(
            items = products.items,
            currentPage = page,
            totalPages = products.totalPages,
            totalItems = products.totalItems,
            hasNextPage = page < products.totalPages,
        )
    }

    override suspend fun onRefreshError(throwable: Throwable) {
        emitEffect(ProductsEffect.RefreshFailed)
    }
}
```

`fetchPage` only fetches and maps one page: no appending, no state updates, no swallowed errors.

| Operation | While loading | Success | Failure |
| --- | --- | --- | --- |
| Initial load / retry | `UiState.Loading` | Page 1 | `UiState.Error` |
| Refresh | `isRefreshing`, items kept | Page 1 | Items kept, `refreshError` set, `onRefreshError` called |
| Refresh from a loading or error screen | `UiState.Loading` | Page 1 | `UiState.Error` |
| Next page | `isLoadingNextPage` | Page appended | `nextPageError` set, loaded pages kept |
| Filter or sort, `ReloadStrategy.FullScreen` | `UiState.Loading` | Page 1 | `UiState.Error`; retry uses the new query |
| Filter or sort, `ReloadStrategy.Inline` | `isReloading`, items kept | Page 1 | Previous query restored, items kept, `refreshError` set, `onRefreshError` called |

- `refreshError` clears when the next refresh or reload starts and when the list is replaced.
  `onRefreshError` is an optional extra, such as a snackbar effect. An inline reload from a loading
  or error screen runs full-screen.
- Next-page loads run in a job of their own, so the reducer stays free. A next-page request is
  ignored while one is in flight, while a refresh or inline reload runs, and while no content shows,
  so repeated scroll callbacks load one page. Any replacement cancels an in-flight next-page load.
- Cancellation never sets an error or calls `onRefreshError`.

**`PageResult`.** `items` holds only the requested page. `currentPage` is the one-based page
requested; a result for another page counts as a failure. `hasNextPage` decides whether another
request may be made. `totalPages` and `totalItems` may be `null`. `pageSize` must be at least 1.

**Rendering helpers.** `ListState.isEmpty` is true only after page 1 loaded with no items;
`ListState.hasContent` when items exist and no inline reload is active;
`ListState.isAnyOperationInProgress` combines refresh, inline reload and next-page loading.

### Lists without filters or sorting

Extend `BaseSimpleListViewModel` and implement `fetchPage(page)`:

```kotlin
class NotificationsViewModel(
    private val getNotifications: GetNotificationsPageUseCase,
) : BaseSimpleListViewModel<NotificationAction, Nothing, Notification>() {

    override suspend fun reduce(action: NotificationAction) {
        when (action) {
            NotificationAction.Load -> handleLoad()
            NotificationAction.Refresh -> handleRefresh()
            NotificationAction.LoadNextPage -> handleLoadNextPage()
        }
    }

    override suspend fun fetchPage(page: Int): PageResult<Notification> =
        getNotifications(page = page, pageSize = pageSize)
}
```

`NoFilter` and `NoSort` are aliases for `Unit`, for a custom list base that needs the type
parameters.

---

## Words

The library owns no wording and never shows exception text. `UiState.Error.message` is what the
ViewModel supplies:

- `setError(message, cause)` publishes a localised, presentation-safe message.
- `setError(throwable)`, the default `runAction` failure, full-screen list failures and the default
  `onReduceFailure` take the message from `errorMessage(throwable)`, which returns `""` unless
  overridden, and keep the throwable as the cause.

When the message is empty, the UI words the failure from `cause` or with a generic localised message.
Map failures to presentation-safe text in a presentation mapper; never display raw server or
diagnostic messages. `ListState.nextPageError` and `refreshError` are throwables for the UI to map
the same way. See [Localisation](../../docs/localisation.md).

## Testing

Install a `StandardTestDispatcher` as Main before constructing the ViewModel, call its public
methods, and advance virtual time. Actions dispatched from `init` wait for the scheduler, so
`runCurrent()` and `advanceUntilIdle()` see them:

```kotlin
import dev.voir.areca.viewmodel.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `profile is displayed after loading`() = runTest {
        val viewModel = ProfileViewModel(
            getProfile = FakeGetProfileUseCase(profile),
            saveProfile = FakeSaveProfileUseCase(),
            messages = FakeProfileMessages(),
        )

        advanceUntilIdle()

        assertEquals(UiState.Display(ProfileState(profile)), viewModel.state.value)
    }
}
```

Use fake use cases, and `RefreshGate(timeSource = TestTimeSource())` for gates. Test at least:

- the initial `UiState.Display(initial)`;
- successful and failed full-screen actions, and that cancellation publishes no error;
- action ordering when several actions are dispatched;
- critical outcomes in state, and effect delivery with one collector;
- refresh success and failure with content kept;
- next-page append, the duplicate-request and end-of-list guards, failure and retry;
- filter and sort reloads for both strategies;
- `PageResult` mapping.

## Checklist

1. Choose `BaseViewModel`, `BaseListViewModel` or `BaseSimpleListViewModel` by the screen's shape.
2. Model UI intent as actions, handled in `reduce`; expose small public methods that dispatch them.
3. Keep state immutable and update it with `setDisplay` or `mutateDisplay`, only from the
   ViewModel's own context.
4. Keep durable information and critical outcomes in state; use effects only for best-effort
   impulses, with exactly one lifecycle-aware collector.
5. Keep fine-grained loading and error flags in `S`; reserve `UiState.Loading` and `UiState.Error`
   for full-screen transitions.
6. Never display exception text; supply it through `setError(message, cause)` or `errorMessage`.
7. Return exactly the requested page from `fetchPage`; render `refreshError` and `nextPageError`.
8. Rethrow `CancellationException` from broad failure handlers in owned jobs.
9. Obtain ViewModels through a `ViewModelStoreOwner`.
10. Do not assume a new action cancels an earlier one.

## Where to look

- **KDoc** on `BaseViewModel`, `BaseListViewModel`, `RefreshGate` and `UiState`.
- **Related**: the [foundation README](../../ui/foundation/README.md) for the components that render
  loading, empty and error states.
- **Skill**: [`skills/areca-viewmodel-v1/SKILL.md`](../../skills/areca-viewmodel-v1/SKILL.md).
