package dev.voir.areca.playground.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.Toast
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.PasswordField
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.field.AffixTextField
import dev.voir.areca.foundation.field.select.SelectField
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.list.List
import dev.voir.areca.foundation.list.ListItemSkeleton
import dev.voir.areca.foundation.list.RadioListItem
import dev.voir.areca.foundation.list.ToggleListItem
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.overlay.ConfirmationDialog
import dev.voir.areca.foundation.overlay.ConfirmationDialogStyle
import dev.voir.areca.foundation.selection.ColorItem
import dev.voir.areca.foundation.selection.ColorShade
import dev.voir.areca.foundation.selection.NumberStepper
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SelectorMode
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.selection.SlidingSelectorStyle
import dev.voir.areca.foundation.selection.SwatchPicker
import dev.voir.areca.foundation.selection.SwatchPickerLayout
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.status.StatusMark
import dev.voir.areca.foundation.status.StatusMarkState
import dev.voir.areca.foundation.status.StatusScene
import dev.voir.areca.foundation.status.StatusSceneActions
import dev.voir.areca.foundation.tabbar.FloatingTabBar
import dev.voir.areca.foundation.tabbar.TabBar
import dev.voir.areca.foundation.tabbar.TabBarItem
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.TabsSize
import dev.voir.areca.foundation.tabs.TabsVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.FilterAction
import dev.voir.areca.foundation.topbar.SearchAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.Activity
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Car
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Coffee
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.LogOut
import dev.voir.areca.icons.Plus
import dev.voir.areca.icons.Share
import dev.voir.areca.icons.ShoppingCart
import dev.voir.areca.icons.Trash
import dev.voir.areca.icons.Tv
import dev.voir.areca.icons.User
import dev.voir.areca.icons.Wallet
import dev.voir.areca.playground.foundation.settings.AutoLockSample
import dev.voir.areca.playground.foundation.settings.DependentSettingsSample
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** One line of an account's history, as the list, navigation and overlay images show it. */
internal class SampleTransaction(
    val merchant: String,
    val detail: String,
    val amount: String,
    val icon: ImageVector,
)

/** A few days of one account's history. */
internal val SampleTransactions = listOf(
    SampleTransaction("Corner Grocery", "Groceries · 09:41", "−€42.80", ArecaIcons.ShoppingCart),
    SampleTransaction("Northwind Ltd", "Salary · 08:00", "+€4,250.00", ArecaIcons.Wallet),
    SampleTransaction("City Taxi", "Transport · Yesterday", "−€14.20", ArecaIcons.Car),
    SampleTransaction("Coffee Lab", "Dining · Yesterday", "−€4.50", ArecaIcons.Coffee),
    SampleTransaction("Streaming plan", "Subscriptions · 21 Oct", "−€12.99", ArecaIcons.Tv),
)

/**
 * The leading tile of a transaction row.
 *
 * @param transaction The row's transaction.
 */
@Composable
internal fun TransactionIcon(transaction: SampleTransaction) {
    FeaturedIcon(
        imageVector = transaction.icon,
        contentDescription = null,
        style = FeaturedIconStyle.soft(ActionColor.Neutral),
        size = FeaturedIconSize.Small,
    )
}

/**
 * Transactions as a list on one surface, grouped by day.
 *
 * @param transactions Rows to show.
 */
@Composable
internal fun TransactionList(transactions: kotlin.collections.List<SampleTransaction>) {
    List(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) {
        transactions.forEach { transaction ->
            item(
                key = transaction.merchant,
                title = { Text(transaction.merchant) },
                subtitle = { Text(transaction.detail) },
                value = { Text(transaction.amount) },
                leading = { TransactionIcon(transaction) },
                onClick = {},
                onClickLabel = "Open transaction",
            )
        }
    }
}

/** Destinations of the sample app's tab bar. */
internal enum class SampleDestination(val label: String, val icon: ImageVector) {
    Home("Home", ArecaIcons.Home),
    Activity("Activity", ArecaIcons.Activity),
    Wallets("Wallets", ArecaIcons.Wallet),
    Account("Account", ArecaIcons.User),
}

/**
 * The sample app's tab bar.
 *
 * @param selected The destination on screen.
 * @param modifier Modifier applied to the bar.
 * @param floating Whether to draw the floating bar rather than the docked one.
 */
@Composable
internal fun SampleTabBar(selected: SampleDestination, modifier: Modifier = Modifier, floating: Boolean = false) {
    val items = SampleDestination.entries.map { destination ->
        TabBarItem(value = destination) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(destination.icon, contentDescription = null)
                Text(destination.label, variant = TextVariant.LabelSmall, maxLines = 1)
            }
        }
    }
    if (floating) {
        FloatingTabBar(
            items = items,
            selectedValue = selected,
            onValueSelected = {},
            modifier = modifier.fillMaxWidth(),
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
    } else {
        TabBar(
            items = items,
            selectedValue = selected,
            onValueSelected = {},
            modifier = modifier.fillMaxWidth(),
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
    }
}

/** `foundation-buttons`: styles, meanings, sizes, states and icon buttons. */
internal val FoundationButtonsShot = DocShot(name = "foundation-buttons", width = 600.dp) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        DocGroup("Styles") {
            DocRow {
                Button(text = "Save changes", onClick = {})
                Button(text = "Preview", onClick = {}, style = ButtonStyle.soft())
                Button(text = "Export", onClick = {}, style = ButtonStyle.outlined())
                Button(text = "Cancel", onClick = {}, style = ButtonStyle.plain(ActionColor.Neutral))
            }
        }
        DocGroup("Meaning") {
            DocRow {
                Button(text = "Delete", onClick = {}, style = ButtonStyle.soft(ActionColor.Danger))
                Button(text = "Mark as paid", onClick = {}, style = ButtonStyle.soft(ActionColor.Success))
                Button(text = "Snooze", onClick = {}, style = ButtonStyle.soft(ActionColor.Warning))
                Button(text = "Duplicate", onClick = {}, style = ButtonStyle.outlined(ActionColor.Neutral))
            }
        }
        DocGroup("Sizes") {
            DocRow {
                Button(text = "Save", onClick = {}, size = ButtonSize.ExtraSmall)
                Button(text = "Save", onClick = {}, size = ButtonSize.Small)
                Button(text = "Save", onClick = {}, size = ButtonSize.Medium)
                Button(text = "Save", onClick = {}, size = ButtonSize.Large)
                Button(text = "Save", onClick = {}, size = ButtonSize.ExtraLarge)
            }
        }
        DocGroup("Icon, loading and disabled") {
            DocRow {
                Button(
                    text = "New invoice",
                    onClick = {},
                    style = ButtonStyle.soft(),
                    leading = { Icon(ArecaIcons.Plus, contentDescription = null) },
                )
                Button(text = "Saving", onClick = {}, style = ButtonStyle.soft(), loading = true)
                Button(text = "Send", onClick = {}, style = ButtonStyle.soft(), enabled = false)
            }
        }
        DocGroup("Icon buttons") {
            DocRow {
                IconButton(ArecaIcons.Plus, contentDescription = "Add", onClick = {})
                IconButton(ArecaIcons.Edit, contentDescription = "Edit", onClick = {}, style = IconButtonStyle.soft())
                IconButton(
                    ArecaIcons.Share,
                    contentDescription = "Share",
                    onClick = {},
                    style = IconButtonStyle.outlined(ActionColor.Neutral),
                )
                IconButton(
                    ArecaIcons.Trash,
                    contentDescription = "Delete",
                    onClick = {},
                    style = IconButtonStyle.soft(ActionColor.Danger),
                )
                IconButton(
                    ArecaIcons.Plus,
                    contentDescription = "Add",
                    onClick = {},
                    style = IconButtonStyle.soft(),
                    size = IconButtonSize.Small,
                )
                IconButton(
                    ArecaIcons.Plus,
                    contentDescription = "Add",
                    onClick = {},
                    style = IconButtonStyle.soft(),
                    size = IconButtonSize.ExtraSmall,
                )
            }
        }
    }
}

/** Currencies the select field offers. */
private val CurrencyOptions = listOf(
    SelectOption(key = "EUR", value = "EUR", label = "Euro (EUR)"),
    SelectOption(key = "USD", value = "USD", label = "US dollar (USD)"),
    SelectOption(key = "GBP", value = "GBP", label = "Pound sterling (GBP)"),
)

/** `foundation-fields`: text, password, search, select and amount fields, one with an error. */
internal val FoundationFieldsShot = DocShot(name = "foundation-fields", width = 720.dp) {
    DocColumns(
        {
            DocGroup(gap = ArecaTheme.spacing.xl2) {
                FieldGroup(label = "Full name") { field ->
                    TextField(
                        value = "Ada Lovelace",
                        onValueChange = {},
                        field = field,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FieldGroup(
                    label = "Email",
                    required = true,
                    error = "That doesn't look like an email address.",
                ) { field ->
                    TextField(
                        value = "ada@example",
                        onValueChange = {},
                        field = field,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FieldGroup(label = "Password", assistive = "12 or more characters, with a number.") { field ->
                    PasswordField(
                        value = "Areca#Palm2026",
                        onValueChange = {},
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        {
            DocGroup(gap = ArecaTheme.spacing.xl2) {
                FieldGroup(label = "Search") { field ->
                    SearchField(
                        value = "",
                        onValueChange = {},
                        field = field,
                        placeholder = { Text("Search transactions") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FieldGroup(label = "Currency") { field ->
                    SelectField(
                        values = setOf("EUR"),
                        onChange = {},
                        options = CurrencyOptions,
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FieldGroup(label = "Amount", assistive = "Up to €5,000 a day.") { field ->
                    AffixTextField(
                        value = "1,250.00",
                        onValueChange = {},
                        field = field,
                        prefix = { Text("€") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )
}

/** Colours a label can take, as the swatch picker offers them. */
@Composable
private fun labelShades(): kotlin.collections.List<ColorShade> {
    val colors = ArecaTheme.baseColors
    return listOf(
        ColorShade(
            id = "brand",
            name = "Brand",
            items = listOf(
                ColorItem("brand40", "Brand 40", colors.brand50, colors.brand40),
                ColorItem("brand60", "Brand 60", colors.brand70, colors.brand60),
            ),
        ),
        ColorShade(
            id = "green",
            name = "Green",
            items = listOf(
                ColorItem("green40", "Green 40", colors.green50, colors.green40),
                ColorItem("green60", "Green 60", colors.green70, colors.green60),
            ),
        ),
        ColorShade(
            id = "red",
            name = "Red",
            items = listOf(
                ColorItem("red40", "Red 40", colors.red50, colors.red40),
                ColorItem("red60", "Red 60", colors.red70, colors.red60),
            ),
        ),
        ColorShade(
            id = "blue",
            name = "Blue",
            items = listOf(
                ColorItem("blue40", "Blue 40", colors.blue50, colors.blue40),
                ColorItem("blue60", "Blue 60", colors.blue70, colors.blue60),
            ),
        ),
    )
}

/** Categories the chips filter by. */
private val Categories = listOf("Groceries", "Transport", "Dining", "Bills", "Travel")

/**
 * `foundation-selection`: checkbox, radio and toggle rows, sliding selectors, swatches, a stepper
 * and chips.
 */
internal val FoundationSelectionShot = DocShot(name = "foundation-selection", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Checkbox, radio and toggle") {}
            Column {
                CheckboxListItem(
                    checked = true,
                    onCheckedChange = {},
                    title = { Text("Email me receipts") },
                )
                CheckboxListItem(
                    checked = false,
                    onCheckedChange = {},
                    title = { Text("Weekly summary") },
                )
                RadioListItem(selected = true, onSelect = {}, title = { Text("Bill monthly") })
                RadioListItem(
                    selected = false,
                    onSelect = {},
                    title = { Text("Bill yearly") },
                    subtitle = { Text("Two months free") },
                )
                ToggleListItem(state = ToggleState.On, onToggle = {}, title = { Text("Unlock with Face ID") })
            }
            DocGroup("Sliding selector") {
                SlidingSelector(
                    items = listOf("Any level", "Top", "Heart", "Base").map { SelectorItem(it) { Text(it) } },
                    selected = "Heart",
                    onSelect = {},
                    layout = SelectorLayout.Hug,
                )
                SlidingSelector(
                    items = listOf(
                        SelectorItem("Has", thumb = ArecaTheme.colors.fgBase) { Text("Has") },
                        SelectorItem("Avoid", thumb = ArecaTheme.colors.bgDangerBase) { Text("Avoid") },
                    ),
                    selected = "Avoid",
                    onSelect = {},
                    mode = SelectorMode.Optional,
                    style = SlidingSelectorStyle.Pill,
                    layout = SelectorLayout.Hug,
                )
            }
        },
        {
            DocGroup("Swatches") {
                val shades = labelShades()
                SwatchPicker(
                    items = shades,
                    selected = shades.first().items.last(),
                    onChange = {},
                    layout = SwatchPickerLayout.GroupedColumns,
                )
            }
            DocGroup("Stepper") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Guests", variant = TextVariant.BodyMedium, modifier = Modifier.weight(1f))
                    NumberStepper(
                        value = 2,
                        onValueChange = {},
                        modifier = Modifier.width(144.dp),
                        minValue = 1,
                        maxValue = 12,
                    )
                }
            }
            DocGroup("Badge select") {
                FlowBadgeSelect(
                    options = Categories.map { BadgeOption(label = { Text(it) }, value = it) },
                    onSelect = {},
                    selected = listOf("Groceries", "Dining"),
                    selectionMode = BadgeSelectionMode.Multiple,
                    style = BadgeStyle.FilledChip,
                    size = BadgeSize.Chip,
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                )
            }
        },
    )
}

/** `foundation-lists`: a list grouped by day beside settings sections. */
internal val FoundationListsShot = DocShot(name = "foundation-lists", width = 720.dp) {
    DocColumns(
        {
            DocGroup("List") {}
            TransactionList(SampleTransactions)
        },
        {
            DocGroup("Settings") {}
            AutoLockSample()
            DependentSettingsSample()
        },
    )
}

/** `foundation-navigation`: a top bar, underline and segmented tabs, and a tab bar, on one screen. */
internal val FoundationNavigationShot = DocShot(
    name = "foundation-navigation",
    width = 400.dp,
    height = 640.dp,
    padded = false,
) {
    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        topBar = {
            TopBar(
                title = "Activity",
                leading = { BackAction(onClick = {}) },
                trailing = {
                    SearchAction(onClick = {})
                    FilterAction(onClick = {}, badge = "2")
                },
            )
        },
        bottomBar = { SampleTabBar(selected = SampleDestination.Activity) },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Tabs(
                tabs = listOf("All", "Income", "Spending").map { TabItem(it) { Text(it) } },
                selectedValue = "All",
                onValueSelected = {},
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(ArecaTheme.spacing.xl))
            Tabs(
                tabs = listOf("Week", "Month", "Year").map { TabItem(it) { Text(it) } },
                selectedValue = "Month",
                onValueSelected = {},
                modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
                variant = TabsVariant.Segmented,
                size = TabsSize.Segmented,
                fillWidth = true,
            )
            Spacer(Modifier.height(ArecaTheme.spacing.xl))
            TransactionList(SampleTransactions)
        }
    }
}

/** The screen the overlay images open over: invoices in a list under a bar. */
@Composable
private fun InvoicesScreen() {
    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        topBar = { TopBar(title = "Invoices", leading = { BackAction(onClick = {}) }) },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TransactionList(SampleTransactions.take(4))
        }
    }
}

/** `foundation-overlays`: a confirmation dialog and a confirm sheet, each open over a screen. */
internal val FoundationOverlaysShot = DocShot(
    name = "foundation-overlays",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) {
            InvoicesScreen()
            ConfirmationDialog(
                title = { Text("Delete 3 invoices?") },
                text = { Text("They're removed from every report. Paid invoices stay in your bank's records.") },
                confirmText = { Text("Delete invoices") },
                onConfirm = {},
                declineText = { Text("Cancel") },
                onDecline = {},
                style = ConfirmationDialogStyle.default(confirm = ButtonStyle.solid(ActionColor.Danger)),
                leading = {
                    FeaturedIcon(
                        imageVector = ArecaIcons.AlertTriangle,
                        contentDescription = null,
                        style = FeaturedIconStyle.soft(ActionColor.Danger),
                    )
                },
            )
        },
        DocPanel(width = 352.dp, padded = false) {
            InvoicesScreen()
            ConfirmSheet(
                visible = true,
                icon = ArecaIcons.LogOut,
                title = "Sign out of other devices?",
                description = "You'll stay signed in on this phone. Everywhere else asks for your password again.",
                confirmText = "Sign out",
                onConfirm = {},
                onDismiss = {},
            )
        },
    ),
    height = 600.dp,
)

/** `foundation-feedback`: notices, progress, skeletons, a toast and a status scene. */
internal val FoundationFeedbackShot = DocShot(name = "foundation-feedback", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Notice") {
                Notice(title = "Some rows were skipped", icon = ArecaIcons.AlertTriangle, tone = NoticeTone.Warning) {
                    NoticeText("12 rows had no date and were left out of the import.")
                }
            }
            DocGroup("Progress") {
                ProgressBar(
                    progress = 0.64f,
                    label = "Uploading 16 of 25 receipts",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            DocGroup("Skeleton") {
                Column {
                    ListItemSkeleton(showLeading = true, showTrailing = true)
                    ListItemSkeleton(showLeading = true, showTrailing = true, titleWidthFraction = 0.3f)
                }
            }
            DocGroup("Toast") {
                Toast(
                    id = 0L,
                    label = "Changes saved",
                    caption = "Your workspace is up to date",
                    leading = { Icon(ArecaIcons.Check, contentDescription = null) },
                )
            }
        },
        {
            DocGroup("Status scene") {
                Card(modifier = Modifier.fillMaxWidth().height(500.dp), style = CardStyle.Outlined) { padding ->
                    StatusScene(
                        title = "Import complete",
                        modifier = Modifier.padding(padding),
                        mark = { StatusMark(state = StatusMarkState.Success, contentDescription = null) },
                        message = "3,112 transactions were added to 4 wallets.",
                        footnote = "18 rows matched transactions you already had and were skipped.",
                        actions = {
                            StatusSceneActions(
                                primary = "Done",
                                onPrimary = {},
                                secondary = "View wallets",
                                onSecondary = {},
                            )
                        },
                    )
                }
            }
        },
    )
}

/**
 * One colour token: a chip of the colour over its name.
 *
 * @param name The token's name in `ArecaTheme.colors`.
 * @param color Its value in the theme in scope.
 */
@Composable
private fun TokenChip(name: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color, ArecaTheme.shapes.md)
                .border(1.dp, ArecaTheme.colors.borderSubtle, ArecaTheme.shapes.md),
        )
        Text(name, variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle, maxLines = 1)
    }
}

/**
 * A row of colour tokens sharing the width.
 *
 * @param tokens Name and colour of each.
 */
@Composable
private fun TokenRow(tokens: kotlin.collections.List<Pair<String, Color>>) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        tokens.forEach { (name, color) -> TokenChip(name, color, Modifier.weight(1f)) }
    }
}

/** `theme-colors`: grounds, the tone families, ink and borders, and the accent scale. */
internal val ThemeColorsShot = DocShot(name = "theme-colors", width = 720.dp) {
    val c = ArecaTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        DocGroup("Grounds") {
            TokenRow(
                listOf(
                    "bgCanvas" to c.bgCanvas,
                    "bgSoft" to c.bgSoft,
                    "bgSubtle" to c.bgSubtle,
                    "bgMuted" to c.bgMuted,
                    "bgSurface" to c.bgSurface,
                    "bgContrast" to c.bgContrast,
                ),
            )
        }
        DocGroup("Tones: muted, base, intense") {
            TokenRow(
                listOf(
                    "bgBrandMuted" to c.bgBrandMuted,
                    "bgDangerMuted" to c.bgDangerMuted,
                    "bgSuccessMuted" to c.bgSuccessMuted,
                    "bgWarnMuted" to c.bgWarnMuted,
                    "bgInfoMuted" to c.bgInfoMuted,
                    "bgNeutralMuted" to c.bgNeutralMuted,
                ),
            )
            TokenRow(
                listOf(
                    "bgBrandBase" to c.bgBrandBase,
                    "bgDangerBase" to c.bgDangerBase,
                    "bgSuccessBase" to c.bgSuccessBase,
                    "bgWarnBase" to c.bgWarnBase,
                    "bgInfoBase" to c.bgInfoBase,
                    "bgNeutralBase" to c.bgNeutralBase,
                ),
            )
            TokenRow(
                listOf(
                    "bgBrandIntense" to c.bgBrandIntense,
                    "bgDangerIntense" to c.bgDangerIntense,
                    "bgSuccessIntense" to c.bgSuccessIntense,
                    "bgWarnIntense" to c.bgWarnIntense,
                    "bgInfoIntense" to c.bgInfoIntense,
                    "bgNeutralIntense" to c.bgNeutralIntense,
                ),
            )
        }
        DocGroup("Ink and borders") {
            TokenRow(
                listOf(
                    "fgBase" to c.fgBase,
                    "fgSubtle" to c.fgSubtle,
                    "fgMuted" to c.fgMuted,
                    "fgDisabled" to c.fgDisabled,
                    "borderStrong" to c.borderStrong,
                    "borderBase" to c.borderBase,
                ),
            )
        }
        DocGroup("Accent scale, accentScale[0] to [7]") {
            TokenRow(c.accentScale.mapIndexed { index, color -> "$index" to color })
        }
    }
}
