---
name: areca-viewmodel-v1
description: Areca ViewModel 1.x conventions for Kotlin Multiplatform state, actions, concurrency, effects, lists, and tests. Use when writing, reviewing, refactoring, or debugging .kt or .kts presentation code that consumes dev.voir.areca:viewmodel.
---

# Areca ViewModel Guidelines

> **Audience:** Kotlin Multiplatform presentation developers and coding agents
> **Validated library version:** `dev.voir.areca:viewmodel` 1.0.0

## Role

Areca provides UI-agnostic AndroidX ViewModels, immutable state, ordered actions, one-shot
effects, list state, and refresh admission for Android, JVM, and supported iOS targets. It does not
own dependency injection, navigation, persistence, networking, localization, or UI components.

Areca is published to GitHub Packages, not Maven Central. Add its repository to
`settings.gradle.kts`, limited to the Areca group:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven("https://maven.pkg.github.com/VoirDev/areca-ui") {
                    name = "Areca"
                    credentials(PasswordCredentials::class)
                }
            }
            filter { includeGroup("dev.voir.areca") }
        }
    }
}
```

GitHub Packages asks for credentials even for public packages: the Gradle properties
`ArecaUsername` (a GitHub user name) and `ArecaPassword` (a classic personal access token with
`read:packages`), set in `~/.gradle/gradle.properties` or, on CI, as
`ORG_GRADLE_PROJECT_ArecaUsername` and `ORG_GRADLE_PROJECT_ArecaPassword`. Never commit them.

```kotlin
commonMain.dependencies {
    implementation("dev.voir.areca:viewmodel:1.0.0")
}
```

Use `api` only when the module intentionally exposes Areca types in its public contract.

The core types live in `dev.voir.areca.viewmodel`; the list types (`BaseListViewModel`,
`BaseSimpleListViewModel`, `ListState`, `PageResult`, `PagingMeta`, `ReloadStrategy`) live in
`dev.voir.areca.viewmodel.list`.

## Model

| Concept | Contract |
| --- | --- |
| State | Immutable, complete render snapshot and single source of UI truth |
| Action | User or lifecycle input sent to the ViewModel |
| Effect | One-shot UI impulse, such as a navigation request, a transient message, or a permission prompt; never dropped, but it may arrive late |
| ViewModel | Maps use-case output and actions into state |

Scope a ViewModel to one presentation feature, not to a screen: one data area and the actions on
it, such as a library, a profile summary, or this month's stats. A screen that shows four features
binds four ViewModels, one in each stateful composable that renders a feature, and needs no screen
ViewModel unless it has state of its own, such as a multi-step form's current step. Use a plain UI
state holder for UI-only state that no use case feeds.

Reuse a ViewModel class wherever its feature appears, and pass what differs between placements as a
constructor parameter: a preview of the four latest items and the full list are one ViewModel with
a window parameter, not two ViewModels calling the same use cases. Each placement gets its own
instance from a factory; both observe the same use-case `Flow`, so they stay consistent without
sharing an instance. Share one instance only when placements shown together must agree on transient
state, such as a draft, and only through an explicit navigation-graph or DI scope with a known owner
and closing boundary.

```kotlin
/** How much of the library a placement shows. */
sealed interface LibraryWindow {
    /** The [count] most recently added items. */
    data class Latest(val count: Int) : LibraryWindow

    /** Every item. */
    data object All : LibraryWindow
}

internal class LibraryViewModel(
    private val window: LibraryWindow,
    private val library: LibraryUseCase,
    private val mapper: LibraryItemMapper,
) : BaseViewModel<LibraryAction, LibraryEffect, LibraryState>(LibraryState())

// Home: koinViewModel<LibraryViewModel>(key = "library-preview") { parametersOf(LibraryWindow.Latest(4)) }
// Library page: koinViewModel<LibraryViewModel> { parametersOf(LibraryWindow.All) }
```

State flows down and actions flow up. UI renders state and calls public ViewModel methods; it does
not call use cases. A ViewModel depends on presentation mappers and domain use cases, never network,
persistence, repositories, cache types, DTOs, entities, platform views, contexts, or another
ViewModel.

## Ownership and threading

- Obtain ViewModels through a `ViewModelStoreOwner`: Compose `viewModel { … }`, an Android activity
  or fragment, or a navigation back-stack entry. Clearing the store cancels `viewModelScope`, the
  reducer, and every owned job. A ViewModel held without a store, such as one created in plain
  `remember { … }` or directly from Swift, is never cleared and leaks its coroutines.
- `viewModelScope` runs on `Dispatchers.Main.immediate`. On a JVM without a Main dispatcher it falls
  back to a single-parallelism view of `Dispatchers.Default`, so construction never fails there.
  Desktop apps still add `kotlinx-coroutines-swing` (or `-javafx`) so state is published on the UI
  thread.
- Call the state helpers (`display`, `setDisplay`, `mutateDisplay`, `setLoading`, `setInitial`,
  `setError`, `currentDisplayOrNull`, `runAction`) only from `reduce` or from a coroutine launched
  in `viewModelScope` that has not switched dispatcher. They are confined, not atomic: a call from
  another thread, or after `withContext` to another dispatcher, can lose an update. Keep blocking or
  CPU-heavy work behind the use case.

## State

`BaseViewModel<A, E, S>` exposes `state: StateFlow<UiState<S>>` and `effects: Flow<E>`. It starts at
`UiState.Display(initial)`, not `Initial`.

- Use `Display` for renderable data and `mutateDisplay` for immutable changes. `mutateDisplay`
  always publishes `Display`, even from a loading or error screen.
- Use `currentDisplayOrNull()?.let { … }` to run work only while content is shown; the block may
  call suspend functions. `display` always returns the latest known data, even while the top-level
  state is loading or failed.
- `setLoading()`, `setInitial()`, and `setError(...)` change the top-level state and keep the cached
  `display`.
- Outside the ViewModel, read content with `UiState<T>.displayOrNull()`.
- Use top-level `Loading` or `Error` only when content must be replaced.
- Keep existing content visible during refresh, save, validation, selection, or per-item work;
  represent those flags and errors inside `S`.
- Put an outcome the screen must keep showing in state, such as a completed payment or an
  acknowledgement the user must confirm. Let the UI acknowledge the handled state. Navigation
  requests are effects, not state.

## Errors

The library owns no wording and never shows exception text.

- Publish full-screen errors with `setError(message, cause)`, passing localized,
  presentation-safe text from a presentation mapper and, optionally, the cause.
- `setError(throwable)`, the default `runAction` failure, full-screen list failures, and the default
  `onReduceFailure` take their text from `errorMessage(throwable)`. Override `errorMessage` to map
  failures to safe text. Its default is an empty string, and the UI then words the error from
  `UiState.Error.cause` or with a generic localized message.
- Never display diagnostic, backend, or exception text. `ListState.refreshError` and
  `ListState.nextPageError` hold throwables the UI maps the same way.

## Actions and concurrency

`dispatch` never suspends and never drops an action. It feeds an unbounded queue processed by one
ordered reducer, one action at a time. Actions dispatched from `init` or before the reducer starts
are kept; actions still pending when the ViewModel is cleared are discarded. It is not a durable
queue: never dispatch from an unbounded loop, and do not send background jobs, messages, or
externally generated work through it. Prevent UI bursts for critical actions with disabled controls
or explicit admission.

A throwable that escapes `reduce` goes to `onReduceFailure(action, throwable)`, whose default
publishes a full-screen `setError(throwable)`; later actions continue. Override it to log or to keep
the screen as it is; it must not throw. The ViewModel's own cancellation is never reported.

Choose concurrency per action:

| Need | Pattern |
| --- | --- |
| Latest request wins | Cancel the previous owned `Job`, then launch the new one in `viewModelScope` |
| Ignore while active | `RefreshGate` or an explicit in-flight flag |
| Strict sequence | Perform finite work in the reducer |
| Independent work | Launch separately owned jobs and define state merge rules |

Reducers are serialized: a long reducer holds later actions, and actions never cancel or replace
earlier ones. Keep reducers short when work should not block later actions. Every owned job must
preserve structured concurrency and rethrow `CancellationException` from broad failure handling.

`runAction(block, onSuccess, onFailure)` runs inline and launches nothing: it publishes `Loading`,
suspends the caller until `block` completes, then calls `onSuccess` or `onFailure` (default
`setError(throwable)`). Called from `reduce`, it holds the reducer until it returns, so never use it
for Flow collection or latest-wins work. `runAction` and the list handlers rethrow cancellation and
never publish it as an error. Use `runAction` only for work that replaces the whole screen; manage
inline flags in `S` with `mutateDisplay` instead.

```kotlin
import androidx.lifecycle.viewModelScope
import dev.voir.areca.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Renderable profile state.
 *
 * @property profile Profile shown when available.
 */
data class ProfileState(
    val profile: ProfileItem? = null,
)

/** Inputs accepted by [ProfileViewModel]. */
sealed interface ProfileAction {
    /** Requests a latest-wins profile load. */
    data object Load : ProfileAction
}

/**
 * Produces profile state from the profile use case.
 *
 * @property getProfile Loads the current profile.
 * @property profileMapper Converts domain profiles for rendering.
 * @property errorMessages Converts use-case errors to localized, presentation-safe text.
 */
internal class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val profileMapper: ProfileItemMapper,
    private val errorMessages: UseCaseErrorMessages,
) : BaseViewModel<ProfileAction, Nothing, ProfileState>(ProfileState()) {
    /** Latest profile-loading job. */
    private var loadJob: Job? = null

    /** Requests profile loading. */
    fun load(): Unit = dispatch(ProfileAction.Load)

    /** Maps an action to its behavior. */
    override suspend fun reduce(action: ProfileAction) {
        when (action) {
            ProfileAction.Load -> startLoad()
        }
    }

    /** Cancels the previous load and starts a replacement. */
    private fun startLoad() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            setLoading()
            when (val result = getProfile()) {
                is UseCaseResult.Success -> setDisplay(
                    ProfileState(profileMapper.map(result.value)),
                )
                is UseCaseResult.Failure -> setError(
                    message = errorMessages.of(result.error),
                )
            }
        }
    }
}
```

## Effects

State describes what remains true and must survive UI recreation; an effect is a one-shot impulse
the UI performs once.

- Navigation requests (leaving a form after it saves), transient messages (toasts, snackbars), and
  permission prompts are effects, as are focus requests, animations, and haptics.
- Loaded content, form values and validation, loading flags, inline errors, selections, and any
  outcome the screen must keep showing are state.

Send effects only with the `protected` `emitEffect`; UI code cannot push effects into a ViewModel.
Effects are lossless: they use an unbounded queue, so sending never suspends the reducer and never
drops an effect. An effect sent while no collector is active waits and is delivered, in order, to
the next collector, so it can arrive late, after the UI that caused it is gone; a navigation request
sent while the screen is in the background is delivered when it returns. Collectors compete rather
than each receiving a copy, so collect effects with exactly one lifecycle-aware collector per
ViewModel instance, in the composable that owns the instance. A screen with several feature
ViewModels has one collector for each; a shared instance is collected only by its owner.

Collect state and effects with the platform's lifecycle-aware mechanism. On Compose platforms, use
`collectAsStateWithLifecycle` and `repeatOnLifecycle` from
`org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose`; do not keep invisible UI collecting
indefinitely.

## Lists

| Base | Use |
| --- | --- |
| `BaseSimpleListViewModel` | Page-numbered refresh and next-page loading |
| `BaseListViewModel` | Same, plus filter or sort state |
| `BaseViewModel` | Cursor paging or domain-owned accumulated snapshots |

`fetchPage(page)` returns exactly the requested page and maps a domain use case; it never appends,
mutates state, swallows errors, or calls a data source. Page numbers are one-based,
`PageResult.currentPage` must equal the requested page (any other value is a failure of that
request), and `hasNextPage` is authoritative. `pageSize` must be greater than 0; the constructor
throws `IllegalArgumentException` otherwise.

| Operation | In flight | Success | Failure |
| --- | --- | --- | --- |
| `handleLoad` / `handleRetry` | Top-level `Loading` | Replace with page 1 | Top-level `Error` |
| `handleRefresh` with content | `isRefreshing`, items kept | Replace with page 1 | Items kept, `refreshError` set |
| `handleLoadNextPage` | `isLoadingNextPage` | Append the page | Pages kept, `nextPageError` set |
| Filter/sort, `ReloadStrategy.FullScreen` | Top-level `Loading` | Replace with page 1 | Top-level `Error`; retry uses the new query |
| Filter/sort, `ReloadStrategy.Inline` | `isReloading`, items kept | Replace with page 1 | Items kept, previous query restored, `refreshError` set |

- Render `ListState.refreshError` with the retained content. `onRefreshError` is an optional extra,
  such as a snackbar effect; the failure is already in state.
- Refresh and inline reload are full-screen when started from a loading or error screen.
- Next-page loads run in a ViewModel-owned job, so the reducer stays free. Repeat requests are
  ignored while one is in flight, during a refresh or inline reload, and while no content is shown.
- Any reload from page 1 cancels an in-flight next-page job and drops stale pages, so the
  `handle*` functions may also run in latest-wins jobs launched in `viewModelScope`.
- Cancellation never sets an error and never calls `onRefreshError`.
- The `handle*` functions that take a strategy default to `ReloadStrategy.FullScreen`; choose
  inline reload deliberately.
- Prefer domain-owned paging when cached or observed pages are authoritative.

`RefreshGate` rejects concurrent refreshes and requests inside its cooldown; it does not queue them.
Every successful `tryReserve` must reach exactly one `runReserved`; `dispatch` never drops an
action, so reserving in front of `dispatch` is safe. Create one gate per refresh owner and use
`Duration.ZERO` when only overlap prevention is needed.

## Tests

Install a `StandardTestDispatcher` as Main with `Dispatchers.setMain` before constructing the
ViewModel, call public methods, and advance the scheduler. Actions dispatched from `init` are kept
until the scheduler advances, so `runCurrent()` or `advanceUntilIdle()` processes them. Reset Main
after each test. Use `TestTimeSource` for `RefreshGate`. Test:

- initial and full-screen states;
- success, safe failure mapping, cancellation publishing no error, and chosen concurrency behavior;
- action ordering when several actions are dispatched;
- outcomes kept in state, and effects, navigation requests included, with one collector;
- refresh admission and retained-content failure through `refreshError`;
- next-page append, duplicate-request guard, end guard, failure, and retry; and
- both filter/sort strategies and domain-to-page mapping.

Use fake use cases and deterministic inputs. Do not test data layers through a ViewModel.

## Checklist

- [ ] Each ViewModel serves one presentation feature, not a screen; placements of the same feature
      reuse its class with a parameter instead of repeating its use-case calls.
- [ ] ViewModel scope and owner are explicit, it is obtained through a `ViewModelStoreOwner`, and
      UI-only state uses a plain holder.
- [ ] State is immutable and complete, and holds every outcome the screen must keep showing.
- [ ] Navigation requests, transient messages, and permission prompts are effects, collected by
      exactly one lifecycle-aware collector per ViewModel instance.
- [ ] State helpers run only from `reduce` or from `viewModelScope` coroutines that have not
      switched dispatcher.
- [ ] Concurrency, cancellation, and long-running work are intentional; `runAction` only replaces
      the whole screen.
- [ ] Full-screen errors carry presentation-safe text, or an empty message the UI words from
      `cause`.
- [ ] Only use cases and presentation mappers cross the ViewModel boundary.
- [ ] Paging and refresh follow the selected ownership model; `refreshError` and `nextPageError`
      are rendered.
- [ ] Lifecycle-aware collectors and coroutine tests cover the public contract.

## References

- [Android UI layer](https://developer.android.com/topic/architecture/ui-layer)
- [Android UI events](https://developer.android.com/topic/architecture/ui-layer/events)
- [Android architecture recommendations](https://developer.android.com/topic/architecture/recommendations)
