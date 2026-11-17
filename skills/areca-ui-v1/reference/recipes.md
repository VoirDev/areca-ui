# Screen recipes

Open this when building a whole screen or a common interaction from foundation: a settings
screen, a validated form, a form with a docked action, a pushed screen with a floating action and
toasts, a remote list, a panel page, a confirmation, a tabbed shell, pickers, a sheet, a menu,
a dark region, a subscription card with a footer, a custom pressable surface, an analytics overview
built on the charts module, a plan picker for a paywall, a catalogue with filters, or a product
page, at the latest `dev.voir.areca:foundation` and `dev.voir.areca:icons`
release. Also open it to review UI against the [anti-pattern table](#anti-patterns).

Screens built from a flow module have their recipes in that module's reference:

| Screen | Recipe in |
| --- | --- |
| Sign in with a form and providers; providers only; email first; verify a code; the pages an emailed link opens | [flow-auth.md](flow-auth.md) |
| The profile; editing it; the username; the account menu and switcher; devices; changing the photo with cropping | [flow-profile.md](flow-profile.md) |
| The attachment strip, its sheets and the viewer; attachments in a form, on a record, as documents; cropping before attaching | [flow-attachments.md](flow-attachments.md) |
| Cropping a photo on its own | [crop.md](crop.md) |
| Calendars, date ranges, time and date-time pickers | [datetime.md](datetime.md) |
| Charts beyond the analytics overview below | [charts.md](charts.md) |
| An app shell across phone, tablet and foldable: destinations, toolbars, search and status | [adaptive-shell.md](adaptive-shell.md) |

The first recipe lists its imports in full; the others import from the packages in
[signatures.md](signatures.md) plus the usual Compose ones. Literal strings stand in for the app's
string resources, and screen state normally comes from a ViewModel (see the SKILL's "Feed screens
from presentation state"). Copy a recipe, then edit it; do not wrap it in a configurable "screen" composable.

## Settings screen

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsRowTone
import dev.voir.areca.foundation.settings.SettingsSelectRow
import dev.voir.areca.foundation.settings.SettingsToggleRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.rememberTopBarScrollState
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.theme.ArecaTheme
```

```kotlin
enum class AlertSound { Chime, Bell, Silent }

@Composable
fun NotificationSettingsScreen(
    pushEnabled: Boolean,
    pushStatus: SettingsRowStatus,
    onPushEnabledChange: (Boolean) -> Unit,
    sound: AlertSound,
    onSoundChange: (AlertSound) -> Unit,
    soundLabel: (AlertSound) -> String,
    onSignOut: () -> Unit,
    onBack: () -> Unit,
) {
    val scrollState = rememberTopBarScrollState()

    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Notifications",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                scrollState = scrollState,
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
    ) { padding ->
        Settings(
            modifier = Modifier
                .nestedScroll(scrollState.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(vertical = ArecaTheme.spacing.xl3),
        ) {
            Section(
                header = { Text("Alerts") },
                footer = { Text("Applies to this device only.") },
            ) {
                SettingsToggleRow(
                    checked = pushEnabled,
                    onCheckedChange = onPushEnabledChange,
                    title = { Text("Push notifications") },
                    leading = { SettingsRowIcon(ArecaIcons.Bell, hue = ArecaTheme.hues.red) },
                    status = pushStatus,
                )
                SettingsSelectRow(
                    title = { Text("Sound") },
                    options = AlertSound.entries,
                    selected = sound,
                    onSelect = onSoundChange,
                    optionLabel = soundLabel,
                )
            }
            Section {
                SettingsRow(
                    title = { Text("Sign out") },
                    tone = SettingsRowTone.Danger,
                    onClick = onSignOut,
                )
            }
        }
    }
}
```

- The status-bar inset goes to the top bar and the rest to `contentWindowInsets`; the two add up.
- `Settings` does not scroll; the caller scrolls it and applies `BaseLayout`'s `padding`.
- Each `Section` keeps itself to the screen's gutter (`SettingsSectionSize.outerPadding`), so never
  add `horizontalContentInsets()` or side padding around `Settings`: the inset would double. On a
  sheet a section draws flat, its rows edge to edge.
- `SettingsRowStatus.Loading` shows a spinner in place of the value while a change saves;
  `SettingsRowStatus.Error(message)` shows the failure under the row.
- The bell's tile takes a hue: it tells the row apart and means nothing. Keep `ActionColor` for a
  tile whose colour means something.

## Form with validation

`FieldGroup` owns the status: an `error` turns the field red, shows the message and announces it.
Validation rules and their wording are the app's; the form only draws them.

```kotlin
@Composable
fun SignUpForm(
    submitting: Boolean,
    serverError: String?,
    onSubmit: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var attempted by rememberSaveable { mutableStateOf(false) }

    val emailError = "Enter an email address like name@example.com."
        .takeIf { attempted && "@" !in email }
    val passwordError = "Use at least 8 characters.".takeIf { attempted && password.length < 8 }
    val submit = {
        attempted = true
        if ("@" in email && password.length >= 8) onSubmit(email, password)
    }

    Column(
        modifier = modifier.contentInsets(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    ) {
        Heading(
            title = { Text("Create your account") },
            supportive = { Text("You can change these later in Settings.") },
        )
        if (serverError != null) {
            Notice(title = null, tone = NoticeTone.Danger) { NoticeText(serverError) }
        }
        FieldGroup(label = "Email", required = true, error = emailError) { field ->
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                singleLine = true,
                placeholder = { FieldDefaults.Placeholder("name@example.com") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        FieldGroup(
            label = "Password",
            required = true,
            assistive = "At least 8 characters.",
            error = passwordError,
        ) { field ->
            PasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                revealContentDescription = "Show password",
                hideContentDescription = "Hide password",
            )
        }
        Button(
            text = "Create account",
            onClick = submit,
            modifier = Modifier.fillMaxWidth(),
            loading = submitting,
            loadingStateDescription = "Creating account",
        )
    }
}
```

Give a whole form one look with `CompositionLocalProvider(LocalFieldStyle provides
FieldStyle.filled()) { … }` rather than passing `style` to each field.

## Form with a docked action

The action sits in a `BottomActionBar` at the bottom edge. `ready` softens it until the form is
complete, but a press still runs validation and puts each error under its field — a disabled button
would only leave the person guessing. `caption` says what to know before committing.

```kotlin
@Composable
fun NewWalletScreen(
    saving: Boolean,
    onCreate: (name: String) -> Unit,
    onBack: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var attempted by rememberSaveable { mutableStateOf(false) }
    val complete = name.isNotBlank()
    val nameError = "Give the wallet a name.".takeIf { attempted && !complete }

    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        topBar = {
            TopBar(
                title = "New wallet",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
        bottomBar = {
            BottomActionBar(
                actionText = "Create wallet",
                onAction = {
                    attempted = true
                    if (complete) onCreate(name.trim())
                },
                loading = saving,
                caption = "You can rename it later.",
                ready = complete,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .contentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            FieldGroup(label = "Name", required = true, error = nameError) { field ->
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    field = field,
                    singleLine = true,
                )
            }
        }
    }
}
```

`BottomActionBar` clears the navigation bar and the keyboard itself, so `contentWindowInsets` leaves
the bottom out. Use `enabled = false` with a `hint` only for an action that must not happen at all.

## Pushed screen

A screen opened over another: a back control, a floating primary action, toasts. The frame places
the floating action above the navigation bar and adds its height to `padding`, and draws the toasts
above it. The parent's effect collector shows messages through the same `ToastState`. The
transactions are rows on the grey page, so they sit on cards: one inset card per day, its date
inside.

```kotlin
@Composable
fun WalletScreen(
    wallet: Wallet,
    days: PagedContentListState<ContentListGroup<String, Transaction>>,
    toasts: ToastState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpen: (Transaction) -> Unit,
) {
    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = wallet.name,
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
        floatingAction = {
            Button(
                text = "Add",
                onClick = onAdd,
                leading = { Icon(ArecaIcons.Plus, contentDescription = null) },
            )
        },
        toastState = toasts,
    ) { padding ->
        GroupedPagedContentList(
            state = days,
            contentPadding = padding,
            groupLayout = ContentListGroupLayout.Inset,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            groupHeaderContent = { day -> Text(day) },
        ) { transaction ->
            ListItem(
                title = { Text(transaction.merchant) },
                value = { AmountText(transaction.amount) },
                onClick = { onOpen(transaction) },
                onClickLabel = "Open transaction",
            )
        }
    }
}
```

The stateful parent creates `val toasts = rememberToastState()` and calls `toasts.show("Transaction
saved")` from its effects. A screen with a `BottomActionBar` gets its floating action above the bar.

## List screen with refresh, empty and error states

`PagedContentList` shows the loading face, the error face with retry, the empty face or the items,
requests pages near the end, and adds pull-to-refresh when `onRefresh` is set. `contentPadding`
takes the layout's padding, so the rows scroll under the bars and rest clear of them. Rows on the
grey page sit on cards, so the invoices come by month, in `GroupedPagedContentList` with
`ContentListGroupLayout.Inset`: one card per month, its name inside.

```kotlin
data class Invoice(
    val id: String,
    val customer: String,
    val month: String,
    val due: String,
    val amount: Amount,
)

@Composable
fun InvoicesScreen(
    state: PagedContentListState<ContentListGroup<String, Invoice>>,
    onRefresh: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpen: (Invoice) -> Unit,
    onCreate: () -> Unit,
) {
    val listState = rememberLazyListState()
    listState.ScrollToTopOnTabReselect()

    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Invoices",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                trailing = { AddAction(onClick = onCreate, contentDescription = "New invoice") },
            )
        },
    ) { padding ->
        GroupedPagedContentList(
            state = state,
            contentPadding = padding,
            groupLayout = ContentListGroupLayout.Inset,
            listState = listState,
            labels = ContentListLabels(
                emptyTitle = "No invoices yet",
                errorTitle = "Couldn’t load invoices",
                retry = "Try again",
                loadMoreErrorTitle = "Couldn’t load more invoices",
                endReached = "That’s every invoice",
            ),
            onRefresh = onRefresh,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            emptyContent = {
                Fallback(
                    title = "No invoices yet",
                    body = "Invoices you send appear here. Create one with +.",
                )
            },
            initialErrorContent = { message, retry ->
                Fallback(
                    title = "Couldn’t load invoices",
                    body = message,
                    tone = FallbackTone.Failure,
                    actionText = "Try again",
                    onAction = retry,
                )
            },
            groupHeaderContent = { month -> Text(month) },
        ) { invoice ->
            NavigationListItem(
                title = { Text(invoice.customer) },
                onClick = { onOpen(invoice) },
                subtitle = { Text(invoice.due) },
                value = { AmountText(invoice.amount) },
                onClickLabel = "Open invoice",
            )
        }
    }
}
```

The empty face's button, when it has one, is a neutral way back ("Clear filters"); the create
action stays in the top bar. The faces lie on the page; only the rows take cards, and the rows
bring their own 16dp start padding, so nothing adds a gutter around them.
`listState.ScrollToTopOnTabReselect()` takes the list back to the top when its tab is tapped again.
A list with no groups is `PagedContentList` with the same arguments, less `groupLayout` and
`groupHeaderContent`; its rows lie straight on whatever is under it, so keep it for a sheet.

For a list outside `ContentList`, wrap the scroller in
`PullToRefreshLayout(isRefreshing, onRefresh) { … }` (opt in to `ExperimentalArecaApi`) and draw
the faces yourself. `rememberSettledLoading` keeps a fast load from flashing its skeleton. A section
of a screen sits on its own card; its rows bring their own inset, so the card's padding goes only to
the faces that are not rows:

```kotlin
@Composable
fun RecentInvoicesSection(
    state: RecentInvoicesState,
    onRetry: () -> Unit,
    onOpen: (Invoice) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showSkeleton = rememberSettledLoading(state.loading)
    // Under the delay: draw nothing, not the empty face.
    if (state.loading && !showSkeleton) return

    Card(modifier = modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
        when {
            showSkeleton -> SkeletonGroup(contentDescription = "Loading invoices") {
                repeat(3) { ListItemSkeleton() }
            }
            state.error != null -> Fallback(
                title = "Couldn’t load invoices",
                modifier = Modifier.padding(padding),
                body = state.error,
                tone = FallbackTone.Failure,
                actionText = "Try again",
                onAction = onRetry,
            )
            state.invoices.isEmpty() -> Fallback(
                title = "No invoices yet",
                modifier = Modifier.padding(padding),
                body = "Invoices you send appear here.",
                horizontalAlignment = Alignment.Start,
            )
            else -> ListItemColumn {
                state.invoices.forEach { invoice ->
                    NavigationListItem(
                        title = { Text(invoice.customer) },
                        onClick = { onOpen(invoice) },
                        value = { AmountText(invoice.amount) },
                        onClickLabel = "Open invoice",
                    )
                }
            }
        }
    }
}
```

A placeholder for something that is not a row — an avatar, a thumbnail, a card — is a
`SkeletonBlock` sized by its modifier: `SkeletonBlock(Modifier.size(48.dp), shape =
ArecaTheme.shapes.full)`, `SkeletonBlock(Modifier.fillMaxWidth().aspectRatio(2f), shape =
ArecaTheme.shapes.surface)`.

With `dev.voir.areca:viewmodel`, map its list state to the foundation list state in the UI module,
and wire `onRefresh`, `onLoadNextPage`, `onRetryInitial` and `onRetryLoadMore` to the ViewModel's
public methods. `items` turns the loaded rows into what the list draws — the month groups above, or
`{ it }` for a list with no groups:

```kotlin
fun <T, R> UiState<ListState<T, *, *>>.toPagedContentListState(
    loadMoreErrorText: String,
    items: (List<T>) -> List<R>,
): PagedContentListState<R> = when (this) {
    UiState.Initial, UiState.Loading ->
        PagedContentListState(items = emptyList(), hasNextPage = false, loadingInitial = true)
    is UiState.Error ->
        PagedContentListState(items = emptyList(), hasNextPage = false, initialError = message)
    is UiState.Display -> PagedContentListState(
        items = items(data.items),
        // A list ViewModel starts at Display(initial) with no page loaded: show the loading face
        // until the first page arrives, or every list flashes its empty face first.
        loadingInitial = data.pagingMeta.currentPage == 0,
        hasNextPage = data.pagingMeta.hasNextPage,
        loadingNextPage = data.isLoadingNextPage,
        loadMoreError = loadMoreErrorText.takeIf { data.nextPageError != null },
        refreshing = data.isRefreshing,
    )
}

// The invoices by month, for the grouped list above.
val invoices = state.toPagedContentListState("Couldn’t load more invoices") { loaded ->
    loaded.groupBy { it.month }.map { (month, inMonth) -> ContentListGroup(month, month, inMonth) }
}
```

## Panel page

A total over its breakdown: the header on the page's ground, the content on a rounded panel that
runs to the bottom edge however short it is. The page scrolls as one column and goes back to the
top when its tab is tapped again.

```kotlin
@Composable
fun SpendingScreen(month: MonthSpending, onOpenCategory: (Category) -> Unit) {
    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Spending",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
            )
        },
    ) { padding ->
        PanelLayout(
            contentPadding = padding,
            header = {
                Stat(
                    label = "Spent in ${month.name}",
                    value = month.total,
                    modifier = Modifier.contentInsets(),
                    note = month.comparison,
                    size = StatSize.Large,
                )
            },
            panel = {
                // Rows carry their own 16dp start padding; only the heading takes the gutter, so
                // the heading and the rows' content line up.
                Column(
                    modifier = Modifier.padding(vertical = ArecaTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    Heading(title = { Text("By category") }, modifier = Modifier.horizontalContentInsets())
                    ListItemColumn {
                        month.categories.forEach { category ->
                            NavigationListItem(
                                title = { Text(category.name) },
                                onClick = { onOpenCategory(category) },
                                value = { AmountText(category.amount) },
                                onClickLabel = "Open category",
                            )
                        }
                    }
                }
            },
        )
    }
}
```

What is on the panel is drawn on its raised surface, so cards and rows there take that ladder's
fills. Put no lazy list inside a `PanelLayout`; a long, open-ended list is a list screen.

## Confirmation flow

The sheet's visibility is the caller's state. Answer `onConfirm` by setting `confirming`, then clear
`visible` on success or `confirming` on failure; the sheet takes no further press until one of them
changes. The frame draws the toasts: pass the `ToastState` as `BaseLayout`'s `toastState`.

```kotlin
@Composable
fun SecurityScreen(
    signOutOtherDevices: suspend () -> Boolean,
    onBack: () -> Unit,
) {
    var asking by rememberSaveable { mutableStateOf(false) }
    var working by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val toasts = rememberToastState()

    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Security",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
        toastState = toasts,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).contentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Button(
                text = "Sign out other devices",
                onClick = {
                    failed = false
                    asking = true
                },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.soft(ActionColor.Danger),
            )
        }
    }

    ConfirmSheet(
        visible = asking,
        icon = ArecaIcons.LogOut,
        title = "Sign out of every other device?",
        description = "Phones, tablets and browsers on this account must sign in again.",
        confirmText = "Sign out others",
        onConfirm = {
            working = true
            scope.launch {
                val done = signOutOtherDevices()
                working = false
                if (done) {
                    asking = false
                    toasts.show(label = "Other devices signed out")
                } else {
                    failed = true
                }
            }
        },
        onDismiss = { asking = false },
        note = if (failed) "Couldn’t reach the server. Try again." else "You stay signed in here.",
        noteColor = if (failed) ArecaTheme.colors.fgDangerText else ArecaTheme.colors.fgSubtle,
        confirming = working,
        cancelText = "Cancel",
    )
}
```

With a ViewModel, the sheet's `visible`, `confirming` and failure note are fields of the screen
state, and the success toast is an effect.

Signing out is undone by signing in, so the sheet keeps its neutral style. A yes that destroys
something — deleting an account, a file — takes `style = ConfirmSheetStyle.Danger`. A question with
a choice in it ("cancel now" or "after the next payment") puts the rows in `extra`, wrapped in
`ConfirmSheetRows` so they run edge to edge on the sheet rather than inside its padding:

```kotlin
extra = {
    ConfirmSheetRows {
        RadioListItem(selected = !later, onSelect = { later = false }, title = { Text("Now") })
        RadioListItem(selected = later, onSelect = { later = true }, title = { Text("After the next payment") })
    }
},
```

## Tabbed screen

`TabBar` switches destinations and applies the bottom inset itself; `Tabs` switches views inside
one. A tab bar of your own feeds `LocalTabReselect`, emitting when the selected tab is tapped again,
so each destination's `ScrollToTopOnTabReselect()` takes it back to the top.

```kotlin
enum class Destination(val label: String, val icon: ImageVector) {
    Home("Home", ArecaIcons.Home),
    Search("Search", ArecaIcons.Search),
    Account("Account", ArecaIcons.User),
}

enum class Feed(val label: String) { Following("Following"), Popular("Popular") }

@Composable
fun MainScreen() {
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }
    val reselects = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    CompositionLocalProvider(LocalTabReselect provides reselects) {
        BaseLayout(
            topBar = {
                TopBar(
                    title = destination.label,
                    windowInsets = WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                    ),
                )
            },
            bottomBar = {
                TabBar(
                    items = Destination.entries.map { entry ->
                        TabBarItem(value = entry) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(entry.icon, contentDescription = null)
                                Text(entry.label, variant = TextVariant.LabelSmall, maxLines = 1)
                            }
                        }
                    },
                    selectedValue = destination,
                    onValueSelected = { value ->
                        if (value == destination) reselects.tryEmit(Unit)
                        destination = value
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        ) { padding ->
            when (destination) {
                Destination.Home -> HomeTab(Modifier.padding(padding))
                Destination.Search -> Text("Search", modifier = Modifier.padding(padding))
                Destination.Account -> Text("Account", modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun HomeTab(modifier: Modifier = Modifier) {
    val feed = rememberTabsState(Feed.Following)

    Column(modifier) {
        Tabs(
            tabs = Feed.entries.map { entry -> TabItem(value = entry) { Text(entry.label) } },
            state = feed,
            modifier = Modifier.fillMaxWidth(),
        )
        when (feed.selectedValue) {
            Feed.Following -> Text("Posts from people you follow")
            Feed.Popular -> Text("Popular posts")
        }
    }
}
```

When the app uses a navigation library, the selected destination is its back stack, not
`rememberSaveable`; pass it as `selectedValue` and navigate in `onValueSelected`.

## Pickers

A setting with a few values is a `SlidingSelector`, not `Tabs`: `Tabs` switch views. The default
`SelectorMode.Required` always has a value and never reports `null`; `SelectorMode.Optional` is a
three-state toggle that reports `null` when the chosen option is tapped again. A `SelectorItem`'s
`thumb` colour is the thumb's while that option is chosen, and its `contentDescription` names an
option whose content alone does not ("Has iris", an icon).

```kotlin
enum class Preference { Has, Avoid }

@Composable
fun NoteFilters(
    level: Level,
    onLevelChange: (Level) -> Unit,
    notes: List<NoteFilter>,
    onPreferenceChange: (note: String, Preference?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
        // Segmented (the default style), equal shares (the default layout).
        SlidingSelector(
            items = Level.entries.map { SelectorItem(it) { Text(it.label) } },
            selected = level,
            onSelectedChange = onLevelChange,
        )
        for (filter in notes) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(filter.note, variant = TextVariant.BodyMedium, modifier = Modifier.weight(1f))
                SlidingSelector(
                    items = listOf(
                        SelectorItem(Preference.Has, thumb = ArecaTheme.colors.fgBase, contentDescription = "Has ${filter.note}") {
                            Text("Has")
                        },
                        SelectorItem(Preference.Avoid, thumb = ArecaTheme.colors.bgDangerBase, contentDescription = "Avoid ${filter.note}") {
                            Text("Avoid")
                        },
                    ),
                    selected = filter.preference,
                    onSelect = { onPreferenceChange(filter.note, it) },
                    mode = SelectorMode.Optional,
                    style = SlidingSelectorStyle.Pill,
                    size = SlidingSelectorSize.Small,
                    layout = SelectorLayout.Hug,
                )
            }
        }
    }
}
```

`SlidingSelectorStyle.Pill` is words with a capsule under the chosen one and no ground — a choice
inside a sentence ("Match: All of them | Any one"). `Underline` and `Button` are the other presets;
`layout = SelectorLayout.Scroll` for options that overflow. Never rebuild a moving thumb by hand
with `animateDpAsState`: every selector, tab strip and floating tab bar shares this one.

## Bottom sheet with its own action bar

A sheet that state opens is `FlowModal(visible = …)`; its header is `FlowModalTopBar`. Its content
lies flat on the sheet — rows, fields and sections at the sheet's gutter, never wrapped in a card or
a `RaisedSurface` and never given side padding of their own.

Every sheet is as tall as its content, up to the cap, so none passes `autoHeight` (ignored) or
`allowPartial = false` (the default). A body that can outgrow the cap scrolls — a lazy list or
`verticalScroll` — or it is cut off there; a body that must stay tall while a search narrows it
takes `Modifier.fillMaxHeight()`. `rememberFlowModal(allowPartial = true)` is only for a long list
that may peek first.

```kotlin
@Composable
fun FilterSheet(onApply: (unpaidOnly: Boolean) -> Unit) {
    var open by rememberSaveable { mutableStateOf(false) }
    var unpaidOnly by remember { mutableStateOf(false) }

    Button(text = "Filters", onClick = { open = true }, style = ButtonStyle.soft())

    FlowModal(
        visible = open,
        onDismissRequest = { open = false },
        header = { FlowModalTopBar("Filters", onClose = { open = false }, closeContentDescription = "Close") },
        footer = {
            FlowModalActionBar(
                confirmText = "Apply",
                onConfirm = {
                    onApply(unpaidOnly)
                    open = false
                },
            )
        },
    ) {
        CheckboxListItem(
            checked = unpaidOnly,
            onCheckedChange = { unpaidOnly = it },
            title = { Text("Unpaid only") },
        )
    }
}
```

A sheet about one thing takes the thing. It is open while `item` is not `null` and keeps drawing the
last item while it slides out, so deleting the transaction from the sheet does not cut it off:

```kotlin
FlowModal(
    item = editing,
    onDismissRequest = { editing = null },
    header = { transaction ->
        FlowModalTopBar(transaction.merchant, onClose = { editing = null })
    },
    footer = { transaction ->
        FlowModalActionBar(
            confirmText = "Delete",
            onConfirm = {
                onDelete(transaction)
                editing = null
            },
            style = FlowModalActionBarStyle.default(confirm = ButtonStyle.soft(ActionColor.Danger)),
        )
    },
) { transaction ->
    TransactionDetails(transaction)
}
```

A choice made in a sheet is never built by hand: `SelectFlowModal` sizes the sheet to the options,
lays them flat with a brand tick, pins a search above a long list and closes on a single pick.

```kotlin
val themePicker = rememberFlowModal()

SelectFlowModal(
    controller = themePicker,
    title = "Appearance",
    values = setOf(theme),
    options = listOf(
        SelectOption(Theme.System, Theme.System, "System", description = "Follows the device",
            leading = { SettingsRowIcon(ArecaIcons.Monitor, hue = ArecaTheme.hues.gray) }),
        SelectOption(Theme.Light, Theme.Light, "Light",
            leading = { SettingsRowIcon(ArecaIcons.Sun, hue = ArecaTheme.hues.orange) }),
        SelectOption(Theme.Dark, Theme.Dark, "Dark",
            leading = { SettingsRowIcon(ArecaIcons.MoonStar, hue = ArecaTheme.hues.indigo) }),
    ),
    onChange = { picked -> picked.firstOrNull()?.let(onTheme) },
)

// Many options: pass onQueryChange and filter; the search is pinned and the sheet takes its cap.
SelectFlowModal(
    controller = currencyPicker,
    title = "Currency",
    values = setOf(currency.code),
    sections = listOf(
        SelectOptionSection("recent", "Recent", recent.map { it.option() }),
        SelectOptionSection("all", "All currencies", matching.map { it.option() }, indexed = true),
    ),
    onChange = { picked -> picked.firstOrNull()?.let(onCurrency) },
    state = search,
    onQueryChange = onSearch,
)
// fun Currency.option() = SelectOption(code, code, "$code · $name", detail = symbol)
```

A sheet that code drives — a step of an imperative flow — keeps the controller form. Navigate only
once it has left:

```kotlin
val picker = rememberFlowModal()
val scope = rememberCoroutineScope()

FlowModal(picker, header = { FlowModalTopBar("Choose a wallet", onClose = { picker.hide() }) }) {
    // A list that can outgrow the sheet is lazy, so it scrolls under the header at the cap.
    WalletRows(onPick = { wallet ->
        scope.launch {
            picker.hideAndWait()
            onOpenWallet(wallet)
        }
    })
}
```

## Menu on an icon button

```kotlin
@Composable
fun MoreMenu(onRename: () -> Unit, onArchive: () -> Unit) {
    val menu = rememberDropdownPopup()

    IconButton(
        imageVector = ArecaIcons.DotsVertical,
        contentDescription = "More actions",
        onClick = { menu.toggle() },
        modifier = Modifier.anchored(menu.anchorState),
        style = IconButtonStyle.plain(ActionColor.Neutral),
    )
    DropdownPopup(state = menu, onDismissRequest = { menu.dismiss() }) {
        Column {
            ListItem(
                title = { Text("Rename") },
                onClick = {
                    menu.dismiss()
                    onRename()
                },
            )
            ListItem(
                title = { Text("Archive") },
                onClick = {
                    menu.dismiss()
                    onArchive()
                },
            )
        }
    }
}
```

The actions of a row — which a phone shows as a sheet and a pointer as a menu, with a
right-click — are `AdaptiveMenu(state, actions = listOf(AdaptiveMenuItem(…)))` on the same
`rememberDropdownPopup()`, with `Modifier.contextMenu(menu)` on the row.

## Region that is always dark

A photo viewer, a camera or an editor is dark whatever the app theme. `ArecaDarkTheme` keeps the
app's palette, shapes and type and flips only the direction; `SystemBars` paints the Android system
bars while composed and restores them when it leaves.

```kotlin
@Composable
fun PhotoViewer(photo: Painter, onClose: () -> Unit) {
    ArecaDarkTheme {
        // The page's own ground, which BaseLayout paints below.
        SystemBars(color = ArecaTheme.surface.ground)
        BaseLayout(
            topBar = {
                TopBar(
                    title = "Photo",
                    style = TopBarStyle.Scrim,
                    windowInsets = WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                    ),
                    leading = { CloseAction(onClick = onClose, contentDescription = "Close") },
                )
            },
        ) {
            Image(photo, contentDescription = "Photo", modifier = Modifier.fillMaxSize())
        }
    }
}
```

To name a dark colour before the dark scope begins (a style default resolved at the call site),
read `ArecaTheme.darkColors`. In a dialog, pair `SystemBars` with `edgeToEdgeDialogProperties()`.

## Subscription card

A card for one subscription, plan or budget: what it is in the body, its state along the bottom as
a footer band. The band states; it holds a button only when the card has one action of its own.
Neutral is the resting state, so a list of cards that need nothing stays quiet and colour appears
only on the cards that have something to say. The state swaps the band's style, glyph and words,
never the layout.

```kotlin
enum class SubscriptionState { Active, DueSoon, Overdue, Renewed }

@Composable
fun SubscriptionCard(
    plan: PlanUi,
    onOpen: () -> Unit,
    onPayNow: () -> Unit,
    statusIcon: (SubscriptionState) -> ImageVector,
    modifier: Modifier = Modifier,
) {
    // One mapping per app, shared by every card with a status band, so a colour means the same
    // thing on each.
    val footerStyle = when (plan.state) {
        SubscriptionState.Active -> CardFooterStyle.plain()
        SubscriptionState.DueSoon -> CardFooterStyle.soft(ActionColor.Warning)
        SubscriptionState.Overdue -> CardFooterStyle.soft(ActionColor.Danger)
        SubscriptionState.Renewed -> CardFooterStyle.soft(ActionColor.Success)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onOpen,
        onClickLabel = plan.openLabel,
        footerStyle = footerStyle,
        footer = {
            // The band inks its glyph, words and button; none of them is styled here.
            Icon(statusIcon(plan.state), contentDescription = null)
            Text(plan.statusLine, modifier = Modifier.weight(1f), maxLines = 1)
            if (plan.state == SubscriptionState.Overdue) {
                Button(text = plan.payNowLabel, onClick = onPayNow)
            }
        },
    ) { padding ->
        Heading(
            title = { Text(plan.name) },
            supportive = { Text(plan.priceLine) },
            modifier = Modifier.padding(padding),
        )
    }
}
```

- `plain()` for the resting state, `soft(Warning)` for something coming up, `soft(Danger)` for
  something past due or failed, `soft(Success)` for something just settled. `solid(color)` only for
  the one card on a screen whose band is its call to action.
- The status line is one line: the date or amount it reports ("Due tomorrow", "€9.99 on 3 Dec").
- Leave the card's own `style` to the theme (`LocalCardStyle`): every card of this kind has the
  same corner (`shapes.surface`) and takes the `raised` ground of whatever it sits on.

## Custom pressable surface

Only when no component fits. Fill with `background(color, shape)`, never `clip`, and give the
indication the same shape so the focus ring draws outside the bounds. Take the fill from a surface
role, never a ground token.

```kotlin
@Composable
fun Tile(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = ArecaTheme.shapes.surface
    Box(
        modifier = modifier
            .minimumTouchTarget()
            .background(ArecaTheme.surface.raised, shape)
            .clickCursor()
            .clickable(
                interactionSource = null,
                indication = TouchIndication(shape = shape),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(ArecaTheme.spacing.xl),
    ) {
        RaisedSurface { Text(label) }
    }
}
```

The tile takes the `raised` fill of the surface it sits on, like a `Card`: white on a grouped page,
grey on a plain one or in a sheet (where a card is emphasis only). `RaisedSurface` puts its content on that fill's surface, so a
chip or a track inside it takes the right `well`.

## Analytics overview

A tab that reads a history and lets the reader pick one period of it. The period chart is the
picker: tapping a column selects it, the chart scrolls it into view, and every section below reads
the same selection — except "your usual month", which stays fixed so it can be compared against.
It needs `dev.voir.areca:charts` beside `foundation`, and `icons` for the glyph.

- The presentation layer hands over periods already summed, labelled and described; the chart draws
  numbers and strings, it does not aggregate or format. Put the latest period last.
- Give every column a `contentDescription` that says the whole period ("October: income €4,250,
  spent €2,340"); the drawn labels alone ("Now") do not.
- "Back to latest" is selecting the last key — the chart scrolls to it. The chart stays on the
  latest column as periods arrive until the reader scrolls away, and moves only when the selected
  key changes, not on a refresh. Hoist `rememberColumnChartState()` only to scroll without selecting
  (`scrollToEnd()` pins it to the end again), or to read `isAtEnd`, `firstVisibleIndex` and
  `lastVisibleIndex`.
- A row whose meter belongs to it goes in `ExpandableListItem`'s `bottom` slot, so tapping the
  meter opens the row; what it reveals goes in its content.
- The period's switch, title, figures and chart head the page on its ground, as a `PanelLayout`
  header does; the sections under them — the usual figures, the categories — sit on cards. The
  category rows bring their own inset, so their card's padding is not applied.
- A row's amount is an `Amount` the presentation layer formatted for the locale, drawn with
  `AmountText` in the row's `value` (or `trailing`) slot with no `style`: it takes the slot's type,
  with tabular figures so the column of amounts lines up and the cents a step smaller.
- Elsewhere on such a tab: a day-by-day month is a `Calendar` with `CalendarHeatDay` in its `day`
  slot and `calendarHeatLevel` mapping amounts to shades; a running total against another period is
  a `LineChart`; a filter sheet's foot is `FlowModalActionBar(layout = FlowModalActionBarLayout.Split(),
  showDivider = false)`; filter chips that must look tappable before any is chosen use
  `BadgeStyle.FilledChip`.

```kotlin
/** One period as the presentation layer hands it over: already summed, labelled and worded. */
@Immutable
data class PeriodSummary(
    val key: String,
    val label: String,
    val sublabel: String?,
    val title: String,
    val income: Float,
    val spent: Float,
    val incomeText: String,
    val spentText: String,
    val description: String,
)

/** One category of the selected period, against its usual amount. */
@Immutable
data class CategoryShare(
    val name: String,
    val amount: Amount,
    val detail: String,
    val share: Float,
    val usualShare: Float,
    val usualAmount: String,
    val subcategories: List<Pair<String, Amount>>,
)

/**
 * The stateless overview: a period switch, the selected period's figures, the period chart that
 * picks it, the fixed usual figures, and the categories that follow the selection.
 */
@Composable
fun AnalyticsOverview(
    periods: List<PeriodSummary>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    usualSpend: Float,
    usual: List<Triple<String, String, String>>,
    categories: List<CategoryShare>,
    modifier: Modifier = Modifier,
) {
    val chart = rememberColumnChartState()
    val latest = periods.last()
    val period = periods.first { it.key == selectedKey }
    val income = ArecaTheme.colors.bgBrandBase
    val spent = ArecaTheme.colors.bgDangerBase
    var step by remember { mutableStateOf("Month") }
    var open by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        Column(
            modifier = Modifier.horizontalContentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            SlidingSelector(
                items = listOf("Week", "Month", "Year").map { SelectorItem(it) { Text(it) } },
                selected = step,
                onSelectedChange = { step = it },
                layout = SelectorLayout.Hug,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(period.title, variant = TextVariant.HeadingSmall, modifier = Modifier.weight(1f))
                if (selectedKey != latest.key) {
                    IconButton(
                        imageVector = ArecaIcons.RefreshCcw,
                        contentDescription = "Back to ${latest.title}",
                        onClick = { onSelect(latest.key) },
                        style = IconButtonStyle.soft(ActionColor.Brand),
                        size = IconButtonSize.Small,
                    )
                }
            }
            StatGrid {
                Stat(label = "Income", value = period.incomeText)
                Stat(label = "Expenses", value = period.spentText)
            }
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(period.spent, spent, "Spent"),
                    StackedBarSegment((period.income - period.spent).coerceAtLeast(0f), income, "Kept"),
                ),
                modifier = Modifier.fillMaxWidth(),
                size = StackedBarChartSize.Tiny,
            )
        }

        // Selecting a column is the whole interaction: the chart scrolls it into view, and the
        // sections below read the selection from the same state.
        ColumnChart(
            columns = periods.map { p ->
                ColumnChartColumn(
                    key = p.key,
                    bars = listOf(ColumnChartBar.of(p.income, income), ColumnChartBar.of(p.spent, spent)),
                    label = p.label,
                    sublabel = p.sublabel,
                    contentDescription = p.description,
                )
            },
            selectedKey = selectedKey,
            onSelect = onSelect,
            state = chart,
            referenceLines = listOf(ColumnChartReferenceLine(usualSpend, "usual spend", ArecaTheme.colors.fgDangerText)),
            contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        )

        Column(
            modifier = Modifier.horizontalContentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Text("Your usual month", variant = TextVariant.HeadingExtraSmall)
            Card(modifier = Modifier.fillMaxWidth()) { padding ->
                StatGrid(modifier = Modifier.padding(padding)) {
                    usual.forEach { (label, value, note) -> Stat(label = label, value = value, note = note) }
                }
            }
        }

        // The rows bring their own 16dp inset, so the card's padding is not applied.
        Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { _ ->
            Column {
                categories.forEach { category ->
                    ExpandableListItem(
                        expanded = open == category.name,
                        onExpandedChange = { open = if (it) category.name else null },
                        title = { Text(category.name) },
                        // Formatted parts, drawn in the slot's type: no style, no fontWeight.
                        value = { AmountText(category.amount) },
                        // The meter is part of the row, so a tap on it opens the category too.
                        bottom = {
                            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                                MeterBar(
                                    fraction = category.share,
                                    marker = category.usualShare,
                                    color = spent,
                                    contentDescription = "Usual ${category.usualAmount}",
                                )
                                Text(category.detail, variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle)
                            }
                        },
                        onClickLabel = "Show subcategories",
                    ) {
                        category.subcategories.forEach { (name, amount) ->
                            ListItem(title = { Text(name) }, value = { AmountText(amount) }, nestingLevel = 1)
                        }
                    }
                }
            }
        }
    }
}
```

## Plan picker (paywall)

A choice of plans, each a card with its price, committed by the screen's docked action. The cards
are one radio group; the badge marks the plan to recommend.

```kotlin
enum class Plan { Yearly, Monthly }

@Composable
fun PlanPicker(
    plan: Plan,
    onPlanChange: (Plan) -> Unit,
    onSubscribe: () -> Unit,
    onClose: () -> Unit,
    subscribing: Boolean,
) {
    BaseLayout(
        topBar = { TopBar("Premium", leading = { CloseAction(onClick = onClose, contentDescription = "Close") }) },
        bottomBar = {
            BottomActionBar(
                actionText = "Subscribe",
                onAction = onSubscribe,
                loading = subscribing,
                caption = "Cancel any time in Settings.",
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .horizontalContentInsets()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            ChoiceCard(
                selected = plan == Plan.Yearly,
                onClick = { onPlanChange(Plan.Yearly) },
                badge = { Badge(style = BadgeStyle.Brand) { Text("Best value") } },
                trailing = { AmountText(Amount(whole = "39", fraction = "99", symbol = "€")) },
            ) {
                Text("Yearly", variant = TextVariant.LabelLarge)
                Text("€3.33 a month", variant = TextVariant.BodySmall, color = TextColor.Subtle)
            }
            ChoiceCard(
                selected = plan == Plan.Monthly,
                onClick = { onPlanChange(Plan.Monthly) },
                trailing = { AmountText(Amount(whole = "4", fraction = "99", symbol = "€")) },
            ) {
                Text("Monthly", variant = TextVariant.LabelLarge)
                Text("Cancel any time", variant = TextVariant.BodySmall, color = TextColor.Subtle)
            }
        }
    }
}
```

- `selectableGroup()` on the column makes the cards one radio group; each card is announced as a
  radio button with its state. `mode = ChoiceCardMode.Multiple` for add-ons that combine.
- The chosen card wears the accent ring; leave the cards' fill and corner to the surface.
- The prices come from the store, formatted for the user's locale, and are passed as parts.
- One solid button on the screen: the bar's. A plan is chosen by its card, never by a button in it.

## Catalogue with filters

A searchable list with filters: the query under the title, the count of filters in force on the
bar's `FilterAction`, those filters as chips that remove themselves under the query, the items by
family on inset cards, and a sheet that edits a draft of them. The view model owns the filters and the draft; the sheet is open while there is a draft,
so it is `FlowModal(item = draft)` and slides out with the last draft shown. Filtering that the
person must confirm ("Show results") edits the draft; a chip removed applies at once.

```kotlin
/** How the catalogue is ordered. */
enum class CatalogueSort { Popular, Newest, Price }

/**
 * The filters in force, owned by the screen's view model.
 *
 * @property query The search text.
 * @property families The families chosen; empty for all.
 * @property sort The order.
 * @property inStockOnly Whether only items in stock are listed.
 */
@Immutable
data class CatalogueFilters(
    val query: String = "",
    val families: Set<String> = emptySet(),
    val sort: CatalogueSort = CatalogueSort.Popular,
    val inStockOnly: Boolean = false,
) {
    /** How many filters are in force, for the top bar's count. */
    val count: Int get() = families.size + (if (inStockOnly) 1 else 0)
}

/**
 * One item of the catalogue.
 *
 * @property id Its identity.
 * @property name Its name.
 * @property detail A line under the name.
 */
@Immutable
data class CatalogueItem(val id: String, val name: String, val detail: String)

/**
 * A searchable catalogue: the query and the filters in force under the title, the items by family
 * on cards, and a sheet that edits the filters.
 *
 * @param filters The filters in force.
 * @param draft The filters being edited in the sheet, or `null` while the sheet is closed.
 * @param families Every family that can be chosen.
 * @param results The items the filters let through, by family, and where loading them stands.
 * @param onQueryChange Called with the new search text.
 * @param onFiltersChange Called with filters to apply at once — a chip removed.
 * @param onOpenFilters Opens the sheet with the current filters as its draft.
 * @param onDraftChange Called with the draft as it is edited.
 * @param onApplyDraft Applies the draft and closes the sheet.
 * @param onDismissFilters Closes the sheet and drops the draft.
 * @param onLoadNextPage Asks for the next page of results.
 * @param onOpenItem Opens an item.
 */
@Composable
fun CatalogueScreen(
    filters: CatalogueFilters,
    draft: CatalogueFilters?,
    families: List<String>,
    results: PagedContentListState<ContentListGroup<String, CatalogueItem>>,
    onQueryChange: (String) -> Unit,
    onFiltersChange: (CatalogueFilters) -> Unit,
    onOpenFilters: () -> Unit,
    onDraftChange: (CatalogueFilters) -> Unit,
    onApplyDraft: () -> Unit,
    onDismissFilters: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpenItem: (CatalogueItem) -> Unit,
) {
    val listState = rememberLazyListState()
    listState.ScrollToTopOnTabReselect()

    BaseLayout(
        topBar = {
            TopBar(
                title = "Catalogue",
                trailing = { FilterAction(onClick = onOpenFilters, badge = filters.count.takeIf { it > 0 }?.toString(), contentDescription = "Filters") },
                bottom = {
                    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                        SearchField(
                            value = filters.query,
                            onValueChange = onQueryChange,
                            modifier = Modifier.horizontalContentInsets(),
                            placeholder = { Text("Search perfumes") },
                        )
                        if (filters.families.isNotEmpty() || filters.inStockOnly) {
                            // The filters in force, each with its own way out.
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                            ) {
                                items(filters.families.toList(), key = { it }) { family ->
                                    Chip(
                                        label = family,
                                        onRemove = { onFiltersChange(filters.copy(families = filters.families - family)) },
                                        removeContentDescription = "Remove $family",
                                    )
                                }
                                if (filters.inStockOnly) {
                                    item(key = "stock") {
                                        Chip(
                                            label = "In stock",
                                            onRemove = { onFiltersChange(filters.copy(inStockOnly = false)) },
                                            removeContentDescription = "Remove In stock",
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            )
        },
    ) { padding ->
        // Rows on the grey page sit on cards: one inset card per family, its name inside.
        GroupedPagedContentList(
            state = results,
            contentPadding = padding,
            groupLayout = ContentListGroupLayout.Inset,
            listState = listState,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            emptyContent = {
                Fallback(
                    title = "Nothing matches",
                    body = "Try fewer filters or another search.",
                    actionText = "Clear filters",
                    onAction = { onFiltersChange(CatalogueFilters(query = filters.query)) },
                )
            },
            groupHeaderContent = { family -> Text(family) },
        ) { item ->
            ListItem(
                title = { Text(item.name) },
                subtitle = { Text(item.detail) },
                onClick = { onOpenItem(item) },
                onClickLabel = "Open ${item.name}",
            )
        }
    }

    // State-driven: open while there is a draft, and the draft is what the sheet shows.
    FlowModal(
        item = draft,
        onDismissRequest = onDismissFilters,
        header = { FlowModalTopBar(title = "Filters", onClose = onDismissFilters) },
        footer = { FlowModalActionBar(confirmText = "Show results", onConfirm = onApplyDraft) },
    ) { edited ->
        // Flat on the sheet, and scrolling once a long list of families outgrows it.
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Text("Family", variant = TextVariant.Overline, modifier = Modifier.horizontalContentInsets())
            FlowBadgeSelect(
                options = families.map { BadgeOption(label = { Text(it) }, value = it) },
                onSelect = { option ->
                    val chosen = edited.families
                    onDraftChange(edited.copy(families = if (option.value in chosen) chosen - option.value else chosen + option.value))
                },
                selected = edited.families.toList(),
                selectionMode = BadgeSelectionMode.Multiple,
                style = BadgeStyle.FilledChip,
                size = BadgeSize.Chip,
                modifier = Modifier.horizontalContentInsets(),
            )
            Text("Sort", variant = TextVariant.Overline, modifier = Modifier.horizontalContentInsets())
            SlidingSelector(
                items = CatalogueSort.entries.map { SelectorItem(it) { Text(it.name) } },
                selected = edited.sort,
                onSelectedChange = { onDraftChange(edited.copy(sort = it)) },
                modifier = Modifier.horizontalContentInsets(),
            )
            CheckboxListItem(
                checked = edited.inStockOnly,
                onCheckedChange = { onDraftChange(edited.copy(inStockOnly = it)) },
                title = { Text("In stock only") },
            )
        }
    }
}
```

- One filter vocabulary: the same chips above the list and in the sheet, the same order.
- The chips ride in the bar's `bottom` under the query, so the filters in force stay in sight while
  the list scrolls.
- The empty face says what to do — "Clear filters" — and keeps the query.
- In the sheet, the overlines and the chips take the sheet's gutter; the checkbox row brings its own.
- Compiled in the playground as `CatalogueFiltersRecipe.kt`.

## Product detail

A product page: pictures under a transparent bar, what it is and its price, its figures, its facts
on a section, and the one thing to do with it pinned to the bottom. The pictures run under the
status bar and the bar, so the list takes only the bottom padding; `TopBarStyle.Scrim` sets the
controls in dark discs that read over any picture, and the frame is square so the page never shows
at its edges.

```kotlin
/**
 * What a product page shows.
 *
 * @property name The product's name.
 * @property maker Who makes it, the line above the name.
 * @property price Its price, formatted by the app.
 * @property pictures Models the media image loader understands.
 * @property rating Its rating, formatted.
 * @property reviews How many reviews, formatted.
 * @property facts Label and value pairs: concentration, size, year.
 * @property favourite Whether the person has saved it.
 */
@Immutable
data class ProductUi(
    val name: String,
    val maker: String,
    val price: Amount,
    val pictures: List<Any>,
    val rating: String,
    val reviews: String,
    val facts: List<Pair<String, String>>,
    val favourite: Boolean,
)

/**
 * A product page: its pictures under a transparent bar, what it is, its figures, its facts, and the
 * one thing to do with it pinned to the bottom.
 *
 * The pictures run under the status bar and the bar, so the content takes only the bottom padding;
 * the bar's [TopBarStyle.Scrim] puts its controls in dark discs that read over any picture.
 *
 * @param product What to show.
 * @param onBack Leaves the page.
 * @param onShare Shares the product.
 * @param onFavouriteChange Saves or unsaves it.
 * @param onBuy The page's primary action.
 */
@Composable
fun ProductDetailScreen(
    product: ProductUi,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onFavouriteChange: () -> Unit,
    onBuy: () -> Unit,
) {
    val carousel = rememberCarouselState(itemCount = product.pictures.size)

    BaseLayout(
        topBar = {
            TopBar(
                title = "",
                style = TopBarStyle.Scrim,
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
                trailing = {
                    ShareAction(onClick = onShare, contentDescription = "Share")
                    FavoriteAction(selected = product.favourite, onClick = onFavouriteChange, contentDescription = "Save")
                },
            )
        },
        bottomBar = { BottomActionBar(actionText = "Add to bag", onAction = onBuy) },
    ) { padding ->
        // The hero runs under the bar: only the bottom padding is applied.
        LazyColumn(contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())) {
            item(key = "hero") {
                Box {
                    Carousel(state = carousel, contentDescription = product.name) { index ->
                        MediaFrame(
                            model = product.pictures[index],
                            contentDescription = null,
                            aspectRatio = 1f,
                            // Full bleed: square corners, so the page never shows at its edges.
                            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
                        )
                    }
                    PageIndicator(
                        pageCount = carousel.pageCount,
                        currentPage = carousel.currentPage,
                        currentPageOffset = carousel.currentPageOffset,
                        style = PageIndicatorStyle.onImage(),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
                    )
                }
            }
            item(key = "title") {
                Column(
                    modifier = Modifier.horizontalContentInsets().padding(vertical = ArecaTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                ) {
                    Text(product.maker, variant = TextVariant.Overline, color = TextColor.Subtle)
                    Text(product.name, variant = TextVariant.HeadingMedium)
                    AmountText(product.price, style = ArecaTheme.typography.headingSmall)
                }
            }
            item(key = "figures") {
                Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                    StatGrid(modifier = Modifier.padding(padding), columns = 2) {
                        Stat(label = "Rating", value = product.rating)
                        Stat(label = "Reviews", value = product.reviews)
                    }
                }
            }
            item(key = "facts") {
                Settings(modifier = Modifier.padding(top = ArecaTheme.spacing.xl)) {
                    Section(header = { Text("Details") }) {
                        product.facts.forEach { (label, value) ->
                            SettingsRow(title = { Text(label) }, value = { Text(value) })
                        }
                    }
                }
            }
        }
    }
}
```

- The price is an `AmountText`; figures are `Stat`s on a card; facts are a `Section` of value
  rows. The name and price head the page on its ground; what follows sits on cards.
- Compiled in the playground as `ProductDetailRecipe.kt` (needs `dev.voir.areca:media`).

## Anti-patterns

| Don't | Do |
| --- | --- |
| `androidx.compose.material3.*` (`Button`, `Scaffold`, `TopAppBar`, `ModalBottomSheet`, `Snackbar`, `Switch`) | `Button`, `BaseLayout`, `TopBar`, `FlowModal` / `ConfirmSheet`, `BaseLayout(toastState = …)`, `Toggle` |
| `BasicText` / `Text(style = TextStyle(fontSize = 14.sp))` | `Text(text, variant = TextVariant.BodySmall)` |
| `Color(0xFF…)`, `MaterialTheme.colorScheme.*` | `ArecaTheme.colors.*`, `TextColor.*`, `ActionColor.*` |
| `Modifier.padding(16.dp)` around a screen | `Modifier.horizontalContentInsets()`, `contentInsets()`, `ArecaTheme.spacing.*` |
| `tween(300)` | `ArecaTheme.motion.slow.tween(ArecaTheme.motion.standard)` |
| A hand-built segmented control or pill row: `Row` of options with an `animateDpAsState` offset | `SlidingSelector(style = SlidingSelectorStyle.Segmented / Pill, …)` |
| `Tabs(variant = TabsVariant.Segmented)` for a setting, or a three-state toggle built from two `Toggle`s | `SlidingSelector`; `mode = SelectorMode.Optional` for has / avoid / either |
| `Modifier.clickable { }` on a `Box` | `Card(onClick = …)`, `ListItem(onClick = …)`, or the custom surface above |
| `Modifier.clip(shape).clickable(…)` | `Modifier.background(color, shape).clickable(indication = TouchIndication(shape = shape), …)` |
| Two `ButtonStyle.solid()` buttons on one screen | One solid; the rest `soft`, `outlined` or `plain` |
| `Checkbox` next to a bare `Text` | `CheckboxListItem(checked, onCheckedChange, title = { Text(…) })` |
| `IconButton(…, contentDescription = null)` | A localised description |
| Relying on `"Back"`, `"Cancel"`, `ContentListLabels.English` in a localised app | Pass the app's strings |
| `ArecaThemeProvider(darkTheme = true)` nested for a dark region | `ArecaDarkTheme { … }` |
| `if (open) FlowModal(…)` | `FlowModal(visible = open, onDismissRequest = { open = false })`, or the controller form driven with `show()` / `hide()` |
| `if (item != null) FlowModal(…)`, or an app `rememberLastNonNull` around a sheet | `FlowModal(item = item, onDismissRequest = { item = null }) { item -> … }` |
| `controller.hide()` then `navigate(…)` at once | `scope.launch { controller.hideAndWait(); navigate(…) }` |
| An app `AppModalTopBar`, or `TopBar(style = TopBarStyle.Modal, size = TopBarSize.Modal, windowInsets = FlowModalHeaderInsets, trailing = { CloseAction(…) })` in each sheet | `FlowModalTopBar(title, onClose = …)`; a pinned search in its `bottom` |
| Rows straight on the grey page: a `LazyList`, a `PagedContentList` or `ListItem`s under `BaseLayout` with no card | A card under the rows: `GroupedPagedContentList(groupLayout = ContentListGroupLayout.Inset)` for a long list, `Card(Modifier.fillMaxWidth().horizontalContentInsets()) { _ -> List { … } }` or a `Section` for a short one |
| A `Card` or `RaisedSurface` around a sheet's form, list or settings, or side padding around its rows | Nothing: rows, fields and `Section`s lie flat on the sheet at its gutter; a `Card` there is emphasis only — a total, a `ChoiceCard` |
| `horizontalContentInsets()` or side padding around rows, a `List` or `Settings`; a `separatorInset` to clear a leading icon | Nothing: rows carry their own 16dp start padding, sections their gutter, and hairlines start at the text |
| `rememberFlowModal(autoHeight = true)`, `allowPartial = false`, or a fixed sheet height | Nothing: a sheet is as tall as its content. A long body scrolls; a tall searchable body takes `Modifier.fillMaxHeight()`; `allowPartial = true` only for a long list that may peek |
| An app choice sheet — an outlined card per option, a `LazyColumn` under its own header — or `controller.hide()` in a single choice's `onChange` | `SelectFlowModal(controller, title = …, values, options)`, which closes itself on the pick |
| Choice rows in a `ConfirmSheet`'s `extra` inset by the sheet's padding | `extra = { ConfirmSheetRows { RadioListItem(…) } }` |
| Attachment sources drawn as cards of your own — a big camera card over two small ones | `AttachmentSourceSheet(layout = AttachmentChoiceLayout.Bento)` (`dev.voir.areca:flow-attachments`) when the sources are fixed and one leads; the default rows otherwise |
| An app `EmptyContent` / `EmptyState` / `ErrorContent` wrapper around `Fallback` | `Fallback(title, body = …, tone = FallbackTone.Empty / Failure, actionText, onAction)` |
| A hand-built floating button: a shadowed `Box` in `overlay` with `navigationBarsPadding()` and a magic `Spacer(Modifier.height(88.dp))` at the end of the list | `BaseLayout(floatingAction = { Button(…, leading = { Icon(…) }) })`; the frame adds its height to `padding` |
| `ToastHost` in `overlay` lifted above a bar with `padding(bottom = …)` | `BaseLayout(toastState = toasts)` |
| An app `LocalFloatingBarInset` or `TabReselect` local | The padding `BaseLayout` and `AdaptiveNavigation` hand their content for what the bars cover; `listState.ScrollToTopOnTabReselect()`, fed by `LocalTabReselect` |
| `if (loading) Skeleton()` that flashes for one frame on a fast network | `if (rememberSettledLoading(loading)) …` |
| A `Box(Modifier.size(48.dp).background(…, CircleShape))` as an avatar placeholder | `SkeletonBlock(Modifier.size(48.dp), shape = ArecaTheme.shapes.full)` in a `SkeletonGroup` |
| A disabled submit button that never says what is missing | `BottomActionBar(ready = complete, …)`: the press runs validation and shows the errors |
| `if (asking) ConfirmSheet(…)` | `ConfirmSheet(visible = asking, …)`, so it animates out |
| A wrapper composable that only changes a component's colours | `style = XStyle.….copy(…)` or `LocalXStyle provides …` |
| Ignoring `BaseLayout`'s `padding` | Apply it as `contentPadding` or `Modifier.padding(padding)` |
| A `Row` of `Text` and `Icon` pretending to be a list row | `ListItem(title, leading, trailing, onClick)` |
| `subtitle = { Column { Text(a, color = …); Text(b) } }` | `subtitle = { Text(a) }, caption = { Text(b) }` |
| `title = { Text(name, fontWeight = FontWeight.SemiBold) }` in a row | `title = { Text(name) }`; the row owns its type |
| `Modifier.padding(top = 2.dp)` on a row's icon to line it up | Nothing: rows align by scenario (`SlotRowLayout.Auto`); pick `.Top`, `.Center` or `.FirstLine` if the scenario differs |
| A flag or a photo beside icon tiles at its own size | `SettingsRowTile { … }`, the tile's size and shape |
| `shape =` passed to one card on a screen | One corner per nesting level, set once through the card style |
| A danger row behind a hairline at the bottom of a section | `Section(band = { SettingsRow(tone = Danger, …) }, bandColor = bgDangerMuted)` |
| A `Card` with a `HorizontalDivider` and a `Row` of buttons at the bottom | `Card(footer = { … }, footerStyle = …)` |
| `forEach` over an open-ended collection inside a `Section` | `LazyList` / `PagedContentList` of `ListItem` rows, or ask which the product wants |
| `Box(Modifier.height(1.dp).background(…))`, a divider between two list rows | `HorizontalDivider()`; a `List` draws its own hairlines |
| `private val SectionGap = 22.dp` used as a gap or padding | The nearest `ArecaTheme.spacing.*` step; a named literal is still a literal |
| `Text(…, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgSubtle)` | `Text(…, variant = TextVariant.BodySmall, color = TextColor.Subtle)` |
| `TopBar(style = TopBarStyle.default(bg = …))` repeated on every screen | `LocalTopBarStyle provides …` once in the theme's `componentDefaults` |
| `Settings(styles = …)` wrapped in an app `SettingsList` to restyle every list | `LocalSettingsStyles provides SettingsDefaults.styles(…)` once in the theme's `componentDefaults` |
| A wrapper that restyles `Settings` sections per screen — a grey `sectionStyle(bg = …)` for a sheet, a white one for a panel | Nothing: a section's card, hairlines and band follow the surface it is drawn on. Provide `LocalSettingsStyles` once, and only for what differs app-wide |
| A hand-picked ground on something drawn on a surface: `Card(style = CardStyle.default(bg = ArecaTheme.colors.bgSubtle))`, `background(ArecaTheme.colors.bgSurface)` on a panel, `bgMuted` for a track | A surface role: leave the style's colour unspecified, or read `ArecaTheme.surface.raised` / `well` / `separator`, and wrap your own fill's content in `RaisedSurface` |
| An app grey for a tile inside a card (a constant of its own), or a sheet style that greys a sheet so its cards turn white | `ArecaTheme.surface.raised` inside the card, with `RaisedSurface`; a sheet keeps its default ground and its cards follow the sheet's ladder |
| `Box(Modifier.background(ArecaTheme.colors.bgCanvas))` around the app, or `BaseLayout(bg = ArecaTheme.colors.bgCanvas)` | Nothing: `BaseLayout` paints the page of the surface layout |
| App-wide locals in a `CompositionLocalProvider` around the content, lost inside `ArecaDarkTheme` | `ArecaThemeProvider(componentDefaults = ComponentDefaults { arrayOf(…) })` |
| `Icon(rememberVectorPainter(ArecaIcons.Trash), …)` | `Icon(ArecaIcons.Trash, …)` — the `ImageVector` overload |
| `RoundedCornerShape(ArecaTheme.radius.lg)` | `ArecaTheme.shapes.lg` |
| A pill drawn from `Text` and `Modifier.background(…, shapes.full)` — "PRO", "Best value" | `Badge(style = BadgeStyle.Brand) { Text(…) }` |
| A label over a large number built from two `Text`s | `Stat(label, value, note)`; several in `StatGrid` |
| `listOf(a, b).joinToString(" · ")` shown to the user | A string resource with placeholders, so a locale can reorder or re-punctuate it |
| `ConfirmSheetStyle.Default.copy(icon = …Danger, confirm = …Danger)` | `ConfirmSheetStyle.Danger` |
| `SlidingSelector(…, onSelect = { if (it != null) value = it })` | `SlidingSelector(…, onSelectedChange = { value = it })` |
| `FlowBadgeSelect(…, verticalArrangement = Arrangement.spacedBy(…))` | Leave the arrangements alone: the chips' touch target already spaces the lines |
| `Checkbox(checked = true, indeterminate = true)` only to get the dash | `indeterminate = true` draws the dash whatever `checked` is; `checked` decides what a tap asks for |
| `if (sheetSubject != null) FlowModal(…)` for a sheet about something that can be deleted | `FlowModal(item = sheetSubject, …)`, which keeps the last subject until it has animated out |
| An app `SettingsTint` / `HueTint` palette for settings tiles, or the `blue` ramp recoloured so `Info` reads as an accent | `SettingsRowIcon(imageVector, hue = ArecaTheme.hues.purple)`, `FeaturedIconStyle.solidHue` / `softHue`; `ArecaThemeProvider(hues = …)` to change the set |
| An app `FitText` that shrinks a `BasicText` to fit | `Text(…, maxLines = 1, autoSize = TextAutoSize.StepBased(minFontSize = …, maxFontSize = …))`; for money `AmountText(autoSize = …)` |
| An amount split into two `Text`s — the figures with `fontWeight`, the cents smaller — or a `fontWeight` on a figure in a row slot | `AmountText(Amount(whole, fraction, symbol = …))`, in the row's `value` or `trailing` slot without a style |
| A radio card built from `Modifier.clip(shape).border(…).selectable(…)` — a `PlanCard`, an `OptionCard` | `ChoiceCard(selected, onClick, badge = …, trailing = …) { … }` in a `selectableGroup()` column |
| A star `IconButton` whose `contentDescription` switches between "Add to favourites" and "Remove from favourites" | `IconToggleButton(checked, onCheckedChange, ArecaIcons.Star, contentDescription = "Favourite", checkedImageVector = …)`, announced as a toggle with its state |
| A filter chip with a cross built from `Badge`, a `Row` and an `IconButton` | `Chip(label, onRemove = …, removeContentDescription = …, leading = { … })` |
| Vibrating from the app on each key of a `KeyboardKeys` keyboard (and the `VIBRATE` permission it needs) | Nothing: every key press plays the platform keyboard tap through `LocalHapticFeedback`; `LocalKeyboardHaptics provides false` to silence it |
