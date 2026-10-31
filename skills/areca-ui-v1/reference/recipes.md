# Screen recipes

Open this when building a whole screen or a common interaction from foundation: a settings
screen, a validated form, a remote list, a confirmation, a tabbed shell, pickers, a sheet, a menu,
a dark region, a subscription card with a footer, a custom pressable surface, or an analytics overview
built on the charts module, at the latest `dev.voir.areca:foundation` and `dev.voir.areca:icons`
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
import dev.voir.areca.foundation.button.ActionColor
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
import dev.voir.areca.theme.horizontalContentInsets
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
        bg = ArecaTheme.colors.bgCanvas,
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
                .horizontalContentInsets()
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
                    leading = { SettingsRowIcon(ArecaIcons.Bell, color = ActionColor.Brand) },
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
- `SettingsRowStatus.Loading` shows a spinner in place of the value while a change saves;
  `SettingsRowStatus.Error(message)` shows the failure under the row.

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

## List screen with refresh, empty and error states

`PagedContentList` shows the loading face, the error face with retry, the empty face or the items,
requests pages near the end, and adds pull-to-refresh when `onRefresh` is set. `contentPadding`
takes the layout's padding, so the rows scroll under the bars and rest clear of them.

```kotlin
data class Invoice(val id: String, val customer: String, val due: String, val amount: String)

@Composable
fun InvoicesScreen(
    state: PagedContentListState<Invoice>,
    onRefresh: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpen: (Invoice) -> Unit,
    onCreate: () -> Unit,
) {
    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
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
        PagedContentList(
            state = state,
            contentPadding = padding,
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
                    top = { FeaturedIcon(ArecaIcons.Inbox, contentDescription = null) },
                    heading = { Text("No invoices yet") },
                    body = { Text("Invoices you send appear here.") },
                    content = { Button(text = "New invoice", onClick = onCreate) },
                )
            },
        ) { invoice ->
            NavigationListItem(
                title = { Text(invoice.customer) },
                onClick = { onOpen(invoice) },
                subtitle = { Text(invoice.due) },
                value = { Text(invoice.amount) },
                onClickLabel = "Open invoice",
            )
        }
    }
}
```

For a list outside `ContentList`, wrap the scroller in
`PullToRefreshLayout(isRefreshing, onRefresh) { … }` (opt in to `ExperimentalArecaApi`) and show
`Fallback` yourself when the screen's state says the content is missing or failed.

With `dev.voir.areca:viewmodel`, map its list state to the foundation list state in the UI module,
and wire `onRefresh`, `onLoadNextPage`, `onRetryInitial` and `onRetryLoadMore` to the ViewModel's
public methods:

```kotlin
fun <T> UiState<ListState<T, *, *>>.toPagedContentListState(
    loadMoreErrorText: String,
): PagedContentListState<T> = when (this) {
    UiState.Initial, UiState.Loading ->
        PagedContentListState(items = emptyList(), hasNextPage = false, loadingInitial = true)
    is UiState.Error ->
        PagedContentListState(items = emptyList(), hasNextPage = false, initialError = message)
    is UiState.Display -> PagedContentListState(
        items = data.items,
        hasNextPage = data.pagingMeta.hasNextPage,
        loadingNextPage = data.isLoadingNextPage,
        loadMoreError = loadMoreErrorText.takeIf { data.nextPageError != null },
        refreshing = data.isRefreshing,
    )
}
```

## Confirmation flow

The sheet's visibility is the caller's state. Answer `onConfirm` by setting `confirming`, then clear
`visible` on success or `confirming` on failure; the sheet takes no further press until one of them
changes. The toast host sits in `BaseLayout`'s overlay.

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
        bg = ArecaTheme.colors.bgCanvas,
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
        overlay = { ToastHost(state = toasts) },
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

## Tabbed screen

`TabBar` switches destinations and applies the bottom inset itself; `Tabs` switches views inside
one.

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

    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
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
                onValueSelected = { destination = it },
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
            onSelect = { if (it != null) onLevelChange(it) },
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

```kotlin
@Composable
fun FilterSheet(onApply: (unpaidOnly: Boolean) -> Unit) {
    val modal = rememberFlowModal(allowPartial = false, autoHeight = true)
    var unpaidOnly by remember { mutableStateOf(false) }

    Button(text = "Filters", onClick = { modal.show() }, style = ButtonStyle.soft())

    FlowModal(
        controller = modal,
        header = {
            TopBar(
                "Filters",
                windowInsets = FlowModalHeaderInsets,
                trailing = { CloseAction(onClick = { modal.hide() }) },
            )
        },
        footer = {
            FlowModalActionBar(
                confirmText = "Apply",
                onConfirm = {
                    onApply(unpaidOnly)
                    modal.hide()
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

## Region that is always dark

A photo viewer, a camera or an editor is dark whatever the app theme. `ArecaDarkTheme` keeps the
app's palette, shapes and type and flips only the direction; `SystemBars` paints the Android system
bars while composed and restores them when it leaves.

```kotlin
@Composable
fun PhotoViewer(photo: Painter, onClose: () -> Unit) {
    ArecaDarkTheme {
        SystemBars(color = ArecaTheme.colors.bgCanvas)
        BaseLayout(
            bg = ArecaTheme.colors.bgCanvas,
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

The plan in the card's body, its renewal and the way to change it in a footer. The footer's style
inks the copy and the button, so neither is styled here; a failed payment swaps the style and the
words, not the layout.

```kotlin
@Composable
fun SubscriptionCard(
    plan: PlanUi,
    onManage: () -> Unit,
    onUpdateCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val failed = plan.paymentFailed

    Card(
        modifier = modifier.fillMaxWidth(),
        style = CardStyle.Outlined,
        footerStyle = CardFooterStyle.soft(if (failed) ActionColor.Danger else ActionColor.Brand),
        footer = {
            Text(
                text = if (failed) plan.failureLine else plan.renewalLine,
                modifier = Modifier.weight(1f),
            )
            Button(onClick = if (failed) onUpdateCard else onManage) {
                Text(if (failed) "Update card" else "Manage")
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

## Custom pressable surface

Only when no component fits. Fill with `background(color, shape)`, never `clip`, and give the
indication the same shape so the focus ring draws outside the bounds.

```kotlin
@Composable
fun Tile(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = ArecaTheme.shapes.lg
    Box(
        modifier = modifier
            .minimumTouchTarget()
            .background(ArecaTheme.colors.bgSurface, shape)
            .clickCursor()
            .clickable(
                interactionSource = null,
                indication = TouchIndication(shape = shape),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(ArecaTheme.spacing.xl),
    ) {
        Text(label)
    }
}
```

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
    val description: String,
)

/** One category of the selected period, against its usual amount. */
@Immutable
data class CategoryShare(
    val name: String,
    val amount: String,
    val detail: String,
    val share: Float,
    val usualShare: Float,
    val usualAmount: String,
    val subcategories: List<Pair<String, String>>,
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
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            SlidingSelector(
                items = listOf("Week", "Month", "Year").map { SelectorItem(it) { Text(it) } },
                selected = step,
                onSelect = { if (it != null) step = it },
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
                Stat(label = "Income", value = "€${period.income.toInt()}")
                Stat(label = "Expenses", value = "€${period.spent.toInt()}")
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
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Text("Your usual month", variant = TextVariant.HeadingExtraSmall)
            StatGrid {
                usual.forEach { (label, value, note) -> Stat(label = label, value = value, note = note) }
            }
        }

        Column(modifier = Modifier.padding(horizontal = ArecaTheme.spacing.md)) {
            categories.forEach { category ->
                ExpandableListItem(
                    expanded = open == category.name,
                    onExpandedChange = { open = if (it) category.name else null },
                    title = { Text(category.name) },
                    value = { Text(category.amount) },
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
                    showChevron = false,
                    container = ExpandableListItemContainer.Soft,
                    onClickLabel = "Show subcategories",
                ) {
                    category.subcategories.forEach { (name, amount) ->
                        ListItem(title = { Text(name) }, value = { Text(amount) }, nestingLevel = 1)
                    }
                }
            }
        }
    }
}
```

## Anti-patterns

| Don't | Do |
| --- | --- |
| `androidx.compose.material3.*` (`Button`, `Scaffold`, `TopAppBar`, `ModalBottomSheet`, `Snackbar`, `Switch`) | `Button`, `BaseLayout`, `TopBar`, `FlowModal` / `ConfirmSheet`, `ToastHost`, `Toggle` |
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
| `if (open) FlowModal(…)` | Call `FlowModal` always; drive it with `show()` / `hide()` |
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
