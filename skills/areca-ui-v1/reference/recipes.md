# Screen recipes

Open this when building a whole screen or a common interaction: a settings screen, a validated
form, a remote list, a confirmation, a tabbed shell, a sheet, a menu, a dark region, or a custom
pressable surface. Validated against `dev.voir.areca:foundation` 1.0.0 and `dev.voir.areca:icons`
1.0.0.

The first recipe lists its imports in full; the others import from the packages in
[signatures.md](signatures.md) plus the usual Compose ones. Literal strings stand in for the app's
string resources, and screen state normally comes from a ViewModel (see the SKILL's presentation
section). Copy a recipe, then edit it; do not wrap it in a configurable "screen" composable.

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
                subtitle = {
                    Text(invoice.due, variant = TextVariant.BodySmall, color = TextColor.Subtle)
                },
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

## Bottom sheet with its own action bar

```kotlin
@Composable
fun FilterSheet(onApply: (unpaidOnly: Boolean) -> Unit) {
    val modal = rememberFlowModal(allowPartial = false, autoHeight = true)
    var unpaidOnly by remember { mutableStateOf(false) }

    Button(text = "Filters", onClick = { modal.show() }, style = ButtonStyle.soft())

    FlowModal(
        controller = modal,
        header = { TopBar("Filters", trailing = { CloseAction(onClick = { modal.hide() }) }) },
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

## Anti-patterns

| Don't | Do |
| --- | --- |
| `androidx.compose.material3.*` (`Button`, `Scaffold`, `TopAppBar`, `ModalBottomSheet`, `Snackbar`, `Switch`) | `Button`, `BaseLayout`, `TopBar`, `FlowModal` / `ConfirmSheet`, `ToastHost`, `Toggle` |
| `BasicText` / `Text(style = TextStyle(fontSize = 14.sp))` | `Text(text, variant = TextVariant.BodySmall)` |
| `Color(0xFF…)`, `MaterialTheme.colorScheme.*` | `ArecaTheme.colors.*`, `TextColor.*`, `ActionColor.*` |
| `Modifier.padding(16.dp)` around a screen | `Modifier.horizontalContentInsets()`, `contentInsets()`, `ArecaTheme.spacing.*` |
| `tween(300)` | `ArecaTheme.motion.slow.tween(ArecaTheme.motion.standard)` |
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
| `Box(Modifier.height(1.dp).background(…))`, a divider between two list rows | `HorizontalDivider()`; a `List` draws its own hairlines |
