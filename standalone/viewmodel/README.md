# Areca ViewModel

`viewmodel` is a Kotlin Multiplatform state-management module for screen-level presentation
logic. It provides:

- `BaseViewModel` for unidirectional action, state, and best-effort effect handling.
- `UiState` for the top-level screen lifecycle.
- `RefreshGate` for dropping concurrent refresh requests and rapid repeats.
- `BaseListViewModel` for paginated, filterable, and sortable lists.
- `BaseSimpleListViewModel` for paginated lists without filtering or sorting.

The module is intentionally UI-agnostic. It depends on AndroidX Lifecycle ViewModel and Kotlin
Coroutines, but it does not depend on Compose. UI modules decide how to collect and render its
flows.

Supported targets are Android, JVM, iOS ARM64, and iOS Simulator ARM64.

## When to use it

Use `BaseViewModel` when a screen needs:

- one immutable state stream;
- serialized handling of UI actions;
- best-effort, one-off UI impulses such as a focus request or a non-critical message;
- consistent full-screen loading and error transitions.

Use `BaseListViewModel` when the main screen content is a paginated list and you want standard
handling for initial loading, refresh, next-page loading, filtering, and sorting.

Use `BaseSimpleListViewModel` when the list is paginated but has no filters or sorting.

Use `RefreshGate` when a user can trigger refresh repeatedly before the UI has rendered its
loading state, or when refreshes need a short cooldown after completion.

This module does not provide dependency injection, navigation, persistence, logging,
localization, or UI components. ViewModels depend on domain use cases and presentation mappers,
never on repositories, data sources, or DTOs.

## Add the dependency

The canonical published Maven coordinate is `dev.voir.areca:viewmodel`.

Add it to the consuming Kotlin Multiplatform module, replacing `<version>` with the required Areca
version:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.voir.areca:viewmodel:<version>")
        }
    }
}
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

Use `api("dev.voir.areca:viewmodel:<version>")` instead when the consuming module exposes Areca
ViewModel types in its own public API.

Only modules inside this Areca repository's Gradle build use the type-safe project accessor of the
`:standalone:viewmodel` project:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.standalone.viewmodel)
        }
    }
}
```

Do not copy `projects.standalone.viewmodel` into an external project: that accessor exists only
inside this repository's build.

## Core model

The three generic parameters of `BaseViewModel<A, E, S>` have distinct responsibilities:

| Type | Meaning | Examples |
| --- | --- | --- |
| `A` | An action sent to the reducer | Load, retry, save, item clicked |
| `E` | A best-effort, one-off UI impulse | Focus a field, show a non-critical message |
| `S` | Durable screen data used for rendering | Form fields, content, selection |

Actions enter through `dispatch`. A single coroutine processes them through `reduce` in dispatch
order, one at a time. State is exposed as `StateFlow<UiState<S>>`, while effects are exposed as
`Flow<E>`.

`dispatch` is non-suspending and never drops an action. Actions dispatched from a constructor or an
`init` block are kept until the reducer starts. The queue is unbounded, so do not dispatch in an
unbounded loop or use actions as a background job queue. Actions still pending when the ViewModel
is cleared are discarded.

```text
UI intent -> dispatch(action) -> reduce(action) -> state and/or effect -> UI
```

If `reduce` throws, the failure goes to `onReduceFailure(action, throwable)`, whose default
publishes it as a full-screen `UiState.Error`. Later actions keep being processed. The cancellation
of the ViewModel itself is never reported as a failure.

### `UiState`

`UiState` represents only the top-level screen lifecycle:

- `UiState.Initial`: work has not started.
- `UiState.Loading`: full-screen loading.
- `UiState.Display(data)`: renderable screen data is available.
- `UiState.Error(message, cause)`: full-screen failure.

Fine-grained states belong inside the screen-specific `S` type. Examples include saving a form,
refreshing a list, per-item loading, validation errors, selection, and empty content.

The initial state of `BaseViewModel` is `UiState.Display(initial)`, not `UiState.Initial`. Call
`setInitial()` from a subclass only when the screen explicitly needs an untouched initial phase.

### State and effects are different

State describes something that remains true and must survive UI recreation. Effects are
best-effort impulses whose loss or late arrival is acceptable.

Use state for:

- loaded content;
- form values and validation;
- loading flags and inline errors;
- the selected item or tab;
- outcomes the screen must keep showing, such as a completed payment or an acknowledgement the
  user must confirm. Put them in state and let the UI acknowledge them once handled.

Use effects for:

- navigation requests, such as leaving a form after it saves;
- toasts, snackbars and other transient messages;
- permission prompts and focus requests;
- animations and haptics.

Collect effects with exactly one lifecycle-aware collector per screen. Because the queue keeps every
effect until a collector takes it, a navigation request sent while the screen is in the background
is delivered when it returns.

Effects are queued without limit and never dropped, and sending one never suspends the reducer.
An effect sent while no collector is active waits in the queue and is delivered, in order, when a
collector starts, so it can arrive late, after the UI that caused it is gone. Several collectors
compete for effects rather than each receiving a copy, so keep exactly one lifecycle-aware
collector per screen.

Inside a ViewModel, send effects with `emitEffect`. It is `protected`: UI code cannot push effects
into a ViewModel.

## Ownership and threading

- Obtain ViewModels through a `ViewModelStoreOwner`, for example `viewModel { … }` in Compose, an
  Android activity or fragment, or a navigation back-stack entry. Clearing the store cancels the
  ViewModel's `viewModelScope`, its reducer, and its jobs. A ViewModel held without a store, such
  as one created in plain `remember { … }` or directly from Swift, is never cleared and leaks its
  coroutines.
- `viewModelScope` runs on `Dispatchers.Main.immediate`. On a JVM without a Main dispatcher
  module, it falls back to a single-parallelism view of `Dispatchers.Default`, so constructing a
  ViewModel never throws there; desktop UIs should still add `kotlinx-coroutines-swing` (or
  `-javafx`) so state is published on the UI thread.
- Reducers and work launched in `viewModelScope` never run in parallel with each other. Call the
  state helpers (`display`, `setDisplay`, `mutateDisplay`, `setLoading`, `setError`, `runAction`,
  and so on) only from `reduce` or from a coroutine launched in `viewModelScope` that has not
  switched dispatcher. Move blocking or CPU-heavy work behind the use case rather than into the
  reducer.
- The state helpers are confined, not atomic. `mutateDisplay` reads the latest display data and
  writes the result back without a compare-and-set; it stays consistent because nothing else writes
  in that context, and the same confinement covers a reducer's read-then-write across suspension
  points, which no atomic helper could. A call from another thread, or after `withContext` to
  another dispatcher, can lose an update.
- When an action is dispatched on the main thread, `Main.immediate` may run its reducer before
  `dispatch` returns.

## Basic ViewModel example

```kotlin
import dev.voir.areca.viewmodel.BaseViewModel
import kotlinx.coroutines.CancellationException

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

The public methods form the screen-facing API. They dispatch actions rather than directly mutating
state. The reducer owns the action-to-behavior mapping.

### Updating display data

Subclasses can use:

- `display` to read the latest known display data, even while top-level state is loading or error;
- `setDisplay(value)` to replace it;
- `mutateDisplay { ... }` to transform it and publish `UiState.Display` (it always publishes
  `Display`, even from an error screen, so check `currentDisplayOrNull()` first when that matters);
- `currentDisplayOrNull()` when work should proceed only if the current top-level state is
  `Display`, for example `currentDisplayOrNull()?.let { … }`, which may also call suspend
  functions.

`setLoading()`, `setInitial()`, and `setError(...)` change the visible top-level state but do not
erase the cached `display` value.

### Running work inline

`runAction` is a suspend helper that runs in the caller; it launches nothing. Its sequence:

1. publish `UiState.Loading`;
2. run `block` inline, suspending the caller;
3. invoke `onSuccess` with the result, or `onFailure` when the block throws (by default
   `setError`).

Cancellation is never treated as a failure: when the calling coroutine is cancelled, `onFailure`
is not called and the cancellation propagates. Use `runAction` for operations that should
replace the whole screen with loading or error UI. For inline operations, manage a loading flag
inside `S` with `mutateDisplay` instead.

Reducers are processed serially. If a reducer performs a long request, later actions wait until it
finishes; requests are not automatically cancelled or replaced. For latest-wins work, cancel the
previous `Job` and launch the new one in `viewModelScope`; for independent work, launch separately
owned jobs. Every owned job must rethrow `CancellationException` from broad failure handling.

## Gating user-requested refreshes

`RefreshGate` admits one refresh immediately, rejects requests while it is running, and rejects
rapid repeats for one second after it completes by default. Rejected requests are dropped; they are
not queued for later execution. Create one gate per ViewModel or other refresh owner so unrelated
screens remain independent.

The API has two phases:

1. Call `tryReserve` synchronously at the user-entry point. Its callback dispatches or starts the
   accepted work.
2. Wrap that accepted suspend operation in `runReserved`. It releases the reservation in `finally`
   and starts the cooldown whether the operation succeeds, fails, or is cancelled. It throws
   `IllegalStateException`, without running the operation, when no reservation is held.

This split reserves the work before an asynchronously processed action can update screen state.
Every successful `tryReserve` must therefore lead to exactly one `runReserved` call. `dispatch`
never drops an action, so reserving in front of `dispatch` is safe.

### With `BaseViewModel`

```kotlin
import dev.voir.areca.viewmodel.BaseViewModel
import dev.voir.areca.viewmodel.RefreshGate
import kotlinx.coroutines.CancellationException

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

The failure is recorded in state, so the UI shows it whether or not anything collects effects.

### Starting a coroutine directly

When the refresh should not hold the reducer, launch it in `viewModelScope` instead of dispatching:

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

Use `Duration.ZERO` when only concurrent refresh suppression is required. A custom `TimeSource`
can be supplied for deterministic tests.

## Collecting from Compose UI

The ViewModel module itself is independent of Compose. A Compose consumer collects it with the
lifecycle-aware APIs of `org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose`, which is
available for every Compose Multiplatform target, so a screen in the background stops collecting:

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

## Paginated lists

`BaseListViewModel` owns a `ListState` containing:

- all currently loaded `items`;
- `PagingMeta`;
- active `filters` and `sorting`;
- refresh, reload, and next-page loading flags;
- an inline `nextPageError` and an inline `refreshError`.

The concrete ViewModel supplies `fetchPage`, which maps one page from a domain use case, and maps
its actions to the protected `handle*` functions.

```kotlin
import dev.voir.areca.viewmodel.list.BaseListViewModel
import dev.voir.areca.viewmodel.list.PageResult
import dev.voir.areca.viewmodel.list.ReloadStrategy

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

`fetchPage` should only fetch and map one page. Do not append items, update state, or swallow errors
inside it; the base class owns those behaviors.

### List operation behavior

| Operation | Loading representation | Success | Failure |
| --- | --- | --- | --- |
| Initial load / retry | Top-level `UiState.Loading` | Replace with page 1 | Top-level `UiState.Error` |
| Pull-to-refresh | `isRefreshing = true`, items kept | Replace with page 1 | Keep items, set `refreshError`, call `onRefreshError` |
| Pull-to-refresh from an error or loading screen | Top-level `UiState.Loading` | Replace with page 1 | Top-level `UiState.Error` |
| Load next page | `isLoadingNextPage = true` | Append the page | Set `nextPageError` and keep loaded pages |
| Filter/sort, full-screen | Top-level `UiState.Loading` | Replace with page 1 | Top-level `UiState.Error`; retry uses the new query |
| Filter/sort, inline | `isReloading = true`, items kept | Replace with page 1 | Restore the previous query, keep items, set `refreshError`, call `onRefreshError` |

`refreshError` is cleared when the next refresh or reload starts and when the list is replaced.
`onRefreshError` is an optional extra, such as a snackbar effect; the failure is already in state.
Inline reload applies only while the screen shows content; from a loading or error screen it is
full-screen.

Load, retry, refresh, and filter or sort changes run inline in the reducer. A next-page load runs in
a job owned by the ViewModel, so the reducer stays free: while one is in flight, further next-page
requests are ignored, so repeated scroll callbacks load one page. A next-page request is also
ignored while a refresh or inline reload runs and while no content is shown. Starting any
replacement cancels an in-flight next-page load, and a response that belongs to a replaced list is
discarded. The `handle*` functions can therefore also run in jobs launched in `viewModelScope`, for
example to make filter changes latest-wins. Cancellation never sets an error and never calls
`onRefreshError`.

### `PageResult` contract

- `items` contains only the requested page, never previously loaded pages.
- `currentPage` is the one-based page that was requested. A result for another page is treated as
  a failure of that request.
- `hasNextPage` is authoritative for whether another request may be made.
- `totalPages` and `totalItems` may be `null` when the source does not provide them.
- The base class copies the configured `pageSize` into `PagingMeta`. `pageSize` must be at least
  1; the constructor throws `IllegalArgumentException` otherwise.

### Lists without filters or sorting

Extend `BaseSimpleListViewModel` and implement only `fetchPage(page)`:

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

`NoFilter` and `NoSort` are aliases for `Unit` if a custom list base needs those generic types
explicitly.

### List rendering helpers

- `ListState.isEmpty` is true only after page 1 has loaded successfully with no items.
- `ListState.hasContent` is true when items exist and no inline reload is active.
- `ListState.isAnyOperationInProgress` combines refresh, inline reload, and next-page loading.

## Error handling

The library owns no wording and never shows exception text. `UiState.Error.message` is whatever
the ViewModel supplies:

- `setError(message, cause)` publishes a localized, presentation-safe message the ViewModel
  already has, with an optional cause.
- `setError(throwable)`, the default `runAction` failure, the full-screen list failures, and the
  default `onReduceFailure` take the message from `errorMessage(throwable)` and keep the throwable
  as the cause. `errorMessage` returns an empty string unless the ViewModel overrides it.

When the message is empty, the UI words the failure itself, either from `cause` or with a generic
localized message. Map domain and transport failures to presentation-safe text in a presentation
mapper; never display raw server or diagnostic messages. `ListState.nextPageError` and
`ListState.refreshError` hold throwables for the UI to map the same way.

## Testing guidance

Install a `StandardTestDispatcher` as Main before constructing the ViewModel, call its public
methods, and advance virtual time. Actions dispatched from `init` are kept until the scheduler runs,
so `runCurrent()` and `advanceUntilIdle()` see them:

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

Use fake use cases and `TestTimeSource` for `RefreshGate`. At minimum, test:

- the initial `UiState.Display(initial)` value;
- successful and failed full-screen actions, and that cancellation publishes no error;
- action ordering when several actions are dispatched;
- critical outcomes in state and best-effort effect delivery with one collector;
- refresh success and retained-content failure;
- next-page append, duplicate-request guard, end-of-list guard, failure, and retry;
- filter and sort reloads for both strategies;
- accurate `PageResult` mapping.

Build and run this module's tests with:

```shell
./gradlew :standalone:viewmodel:build
```

`commonTest` runs on the JVM, as Android host tests (`testAndroidHostTest`), and on the iOS
simulator.

## Rules for developers and AI agents

When creating or modifying a consumer of this module:

1. Use the published `dev.voir.areca:viewmodel:<version>` dependency by default. Use
   `projects.standalone.viewmodel` only for a module inside this exact Areca Gradle build.
2. Choose `BaseViewModel`, `BaseListViewModel`, or `BaseSimpleListViewModel` based on screen shape.
3. Model UI intent as an action and keep action handling centralized in `reduce`.
4. Prefer small public methods that dispatch actions; do not let UI code invoke protected handlers.
5. Keep state immutable and update it through `setDisplay` or `mutateDisplay`, only from the
   ViewModel's own context.
6. Keep persistent information and critical outcomes in state; use effects only for best-effort
   impulses.
7. Collect state and effects with lifecycle-aware collectors, with exactly one effect collector.
8. Keep fine-grained loading and error flags inside the screen state; reserve `UiState.Loading` and
   `UiState.Error` for full-screen transitions.
9. Supply presentation-safe error text through `setError(message, cause)` or `errorMessage`, or
   word the error in the UI from its cause; never display exception text.
10. In list implementations, return exactly the requested page from `fetchPage` and let the base
    class manage appending, replacement, and error transitions.
11. Render `ListState.refreshError` and `ListState.nextPageError`; override `onRefreshError` only
    for best-effort extras.
12. Rethrow `CancellationException` from every broad failure handler in owned jobs.
13. Obtain ViewModels through a `ViewModelStoreOwner` so they are cleared.
14. Do not add Compose APIs or dependencies to this module solely for a consuming UI.
15. Do not assume actions cancel earlier actions; reducers are serialized.
16. Update this README, KDoc, and tests together when changing any state-transition behavior.

When inspecting unfamiliar consumer code, start by locating its action type, effect type, state
type, `reduce` implementation, and `fetchPage` implementation if present. Those define the screen's
complete state-management contract.
