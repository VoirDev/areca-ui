package dev.voir.areca.playground.docs

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.Chip
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.IconToggleButton
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.feedback.FallbackTone
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.SkeletonBlock
import dev.voir.areca.foundation.feedback.SkeletonGroup
import dev.voir.areca.foundation.field.AffixTextField
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.field.select.SelectFlowModal
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.BottomActionBar
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.ChoiceCard
import dev.voir.areca.foundation.layout.PanelLayout
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.list.ListItemColumn
import dev.voir.areca.foundation.list.NavigationListItem
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalTopBar
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsSelectRow
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.settings.SettingsTextFieldSuffix
import dev.voir.areca.foundation.settings.SettingsToggleRow
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.foundation.text.AmountSymbolPlacement
import dev.voir.areca.foundation.text.AmountText
import dev.voir.areca.foundation.text.Heading
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.icons.Car
import dev.voir.areca.icons.Coffee
import dev.voir.areca.icons.Film
import dev.voir.areca.icons.Globe
import dev.voir.areca.icons.Heart
import dev.voir.areca.icons.HeartFill
import dev.voir.areca.icons.HelpCircle
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.Palette
import dev.voir.areca.icons.PiggyBank
import dev.voir.areca.icons.Plane
import dev.voir.areca.icons.Plus
import dev.voir.areca.icons.Receipt
import dev.voir.areca.icons.ShoppingCart
import dev.voir.areca.icons.Tag
import dev.voir.areca.icons.User
import dev.voir.areca.icons.Wallet
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.theme.SurfaceLayout
import dev.voir.areca.theme.contentInsets
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.theme.verticalContentInsets
import dev.voir.areca.window.InputMode

/*
 * The documentation images for what 1.1 adds: the two surface layouts, sheets, choice cards and
 * chips, amounts, empty and loading faces, hues, and the screen layouts.
 */

/**
 * A budget screen in the given surface layout, as an app that chose it draws every screen.
 *
 * The theme is mounted again with the layout, in the frame's direction, because the layout is
 * decided once at an app's root.
 *
 * @param layout The surface layout the app picked.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BudgetScreen(layout: SurfaceLayout) {
    val dark = ArecaTheme.isDark
    ArecaThemeProvider(darkTheme = dark, surfaceLayout = layout, inputMode = InputMode.Touch) {
        BaseLayout(
            topBar = { TopBar(title = "Groceries budget", leading = { BackAction(onClick = {}) }) },
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { cardPadding ->
                    Column(
                        modifier = Modifier.padding(cardPadding),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                    ) {
                        Stat(label = "Left this month", value = "€312", note = "€388 of €700 spent")
                        ProgressBar(progress = 0.55f, modifier = Modifier.fillMaxWidth())
                    }
                }
                Settings {
                    Section(header = { Text("Alerts") }) {
                        SettingsToggleRow(
                            checked = true,
                            onCheckedChange = {},
                            title = { Text("Warn me at 80%") },
                        )
                        SettingsSelectRow(
                            title = { Text("Starts again") },
                            options = listOf("Weekly", "Monthly", "Yearly"),
                            selected = "Monthly",
                            onSelect = {},
                            optionLabel = { it },
                        )
                    }
                }
                DocGroup(gap = ArecaTheme.spacing.xl) {
                    FieldGroup(label = "Note") { field ->
                        TextField(
                            value = "Weekly shop and household",
                            onValueChange = {},
                            field = field,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    ) {
                        Chip(label = "Groceries", selected = true, onSelectedChange = {})
                        Chip(label = "Household", selected = true, onSelectedChange = {})
                        Chip(label = "Pharmacy", onSelectedChange = {})
                    }
                }
            }
        }
    }
}

/** `surfaces`: the same screen in the Grouped surface layout and in the Plain one. */
internal val SurfacesShot = DocShot(
    name = "surfaces",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) { BudgetScreen(SurfaceLayout.Grouped) },
        DocPanel(width = 352.dp, padded = false) { BudgetScreen(SurfaceLayout.Plain) },
    ),
    height = 470.dp,
)

/** The screen the sheet images open over: a month of transactions under a bar. */
@Composable
private fun TransactionsScreen() {
    BaseLayout(
        topBar = { TopBar(title = "Transactions", leading = { BackAction(onClick = {}) }) },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TransactionList(SampleTransactions)
        }
    }
}

/** A category a transaction is filed under, as the category picker offers it. */
private class SampleCategory(
    val key: String,
    val label: String,
    val description: String,
    val icon: ImageVector,
    val hue: @Composable () -> Color,
)

/** The categories, each with the hue it keeps on every screen. */
private val SampleCategories = listOf(
    SampleCategory("groceries", "Groceries", "Food and household", ArecaIcons.ShoppingCart) { ArecaTheme.hues.green },
    SampleCategory("dining", "Dining", "Cafés and takeaway", ArecaIcons.Coffee) { ArecaTheme.hues.orange },
    SampleCategory("transport", "Transport", "Fuel, taxis and transit", ArecaIcons.Car) { ArecaTheme.hues.blue },
    SampleCategory("bills", "Bills", "Rent, power and phone", ArecaIcons.Receipt) { ArecaTheme.hues.indigo },
    SampleCategory("travel", "Travel", "Flights and stays", ArecaIcons.Plane) { ArecaTheme.hues.teal },
    SampleCategory("fun", "Going out", "Cinema, concerts and games", ArecaIcons.Film) { ArecaTheme.hues.purple },
    SampleCategory("health", "Health", "Pharmacy and appointments", ArecaIcons.Heart) { ArecaTheme.hues.pink },
)

/**
 * `foundation-sheets`: a form sheet with flat settings rows, and a category picker with search,
 * marks and descriptions, each open over a screen.
 */
internal val FoundationSheetsShot = DocShot(
    name = "foundation-sheets",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) {
            TransactionsScreen()
            FlowModal(
                visible = true,
                onDismissRequest = {},
                header = { FlowModalTopBar(title = "New invoice", onClose = {}) },
                footer = { FlowModalActionBar(confirmText = "Create invoice", onConfirm = {}) },
            ) {
                Settings {
                    Section {
                        SettingsRow(
                            title = { Text("Client") },
                            value = { Text("Ada Lovelace") },
                            onClick = {},
                        )
                        SettingsTextFieldRow(
                            value = "1,250.00",
                            onValueChange = {},
                            placeholder = "0.00",
                            title = "Amount",
                            suffix = { SettingsTextFieldSuffix("EUR", onClick = {}) },
                        )
                        SettingsSelectRow(
                            title = { Text("Due") },
                            options = listOf("On receipt", "In 14 days", "In 30 days"),
                            selected = "In 14 days",
                            onSelect = {},
                            optionLabel = { it },
                        )
                    }
                    Section {
                        SettingsToggleRow(
                            checked = true,
                            onCheckedChange = {},
                            title = { Text("Send a reminder") },
                            subtitle = { Text("Three days before it's due") },
                        )
                    }
                }
            }
        },
        DocPanel(width = 352.dp, padded = false) {
            TransactionsScreen()
            val picker = rememberFlowModal()
            LaunchedEffect(picker) { picker.show(partial = false) }
            SelectFlowModal(
                controller = picker,
                title = "Category",
                values = setOf("dining"),
                options = SampleCategories.map { category ->
                    SelectOption(
                        key = category.key,
                        value = category.key,
                        label = category.label,
                        description = category.description,
                        leading = { SettingsRowIcon(category.icon, hue = category.hue()) },
                    )
                },
                onQueryChange = {},
                searchPlaceholder = "Search categories",
            )
        },
    ),
    height = 680.dp,
)

/** `foundation-choice`: a plan picker of choice cards, toggle and filter chips, and icon toggles. */
internal val FoundationChoiceShot = DocShot(name = "foundation-choice", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Choice cards") {
                Column(
                    modifier = Modifier.fillMaxWidth().selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                ) {
                    ChoiceCard(
                        selected = true,
                        onClick = {},
                        badge = { Badge(style = BadgeStyle.Brand) { Text("Best value") } },
                        trailing = { AmountText(Amount(whole = "39", fraction = "99", symbol = "€")) },
                    ) {
                        Text("Yearly", variant = TextVariant.LabelLarge)
                        Text("€3.33 a month", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                    }
                    ChoiceCard(
                        selected = false,
                        onClick = {},
                        trailing = { AmountText(Amount(whole = "4", fraction = "99", symbol = "€")) },
                    ) {
                        Text("Monthly", variant = TextVariant.LabelLarge)
                        Text("Cancel any time", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                    }
                    ChoiceCard(
                        selected = false,
                        onClick = {},
                        trailing = { AmountText(Amount(whole = "89", fraction = "00", symbol = "€")) },
                    ) {
                        Text("Lifetime", variant = TextVariant.LabelLarge)
                        Text("Pay once, keep it", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                    }
                }
            }
        },
        {
            DocGroup("Chips") {
                ChipRows()
            }
            DocGroup("Icon toggle buttons") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { _ ->
                ListItemColumn {
                    listOf(
                        Triple("Corner Grocery", "12 visits this month", true),
                        Triple("Coffee Lab", "4 visits this month", false),
                    ).forEach { (merchant, visits, favourite) ->
                        ListItem(
                            title = { Text(merchant) },
                            subtitle = { Text(visits) },
                            onClick = {},
                            onClickLabel = "Open merchant",
                            trailing = {
                                IconToggleButton(
                                    checked = favourite,
                                    onCheckedChange = {},
                                    imageVector = ArecaIcons.Heart,
                                    checkedImageVector = ArecaIcons.HeartFill,
                                    contentDescription = "Favourite",
                                )
                            },
                        )
                    }
                }
            }
        },
    )
}

/** Toggle chips over the filters already applied, each with a cross. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipRows() {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Chip(label = "Unpaid", selected = true, onSelectedChange = {})
            Chip(label = "Overdue", onSelectedChange = {})
            Chip(label = "This month", onSelectedChange = {})
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Chip(
                label = "Groceries",
                onRemove = {},
                removeContentDescription = "Remove Groceries",
                leading = { Icon(ArecaIcons.ShoppingCart, contentDescription = null) },
            )
            Chip(label = "Over €50", onRemove = {}, removeContentDescription = "Remove Over €50")
        }
    }
}

/** One line of history with its amount drawn as an amount: income, spending or hidden. */
private class AmountLine(val title: String, val detail: String, val amount: Amount, val tone: AmountTone)

/** How an amount on a line is inked. */
private enum class AmountTone { Income, Expense, Plain }

/** A few lines whose amounts show the sign, the symbol either side and a grouping of their own. */
private val AmountLines = listOf(
    AmountLine("Northwind Ltd", "Salary · 08:00", Amount("4,250", "00", symbol = "€", sign = "+"), AmountTone.Income),
    AmountLine("Corner Grocery", "Groceries · 09:41", Amount("42", "80", symbol = "€", sign = "−"), AmountTone.Expense),
    AmountLine(
        "Lakeside flat",
        "Rent · 1 Oct",
        Amount("8 450", "00", decimalMark = ",", symbol = "kr", symbolPlacement = AmountSymbolPlacement.After, sign = "−"),
        AmountTone.Expense,
    ),
    AmountLine("Weekend trip", "Travel · 28 Sep", Amount("120,000", symbol = "¥", sign = "−"), AmountTone.Plain),
)

/** `foundation-amounts`: amounts at a few sizes, either side of their symbol, masked and inked, beside stats. */
internal val FoundationAmountsShot = DocShot(name = "foundation-amounts", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Amount text") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                ) {
                    Text("Total balance", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                    AmountText(
                        amount = Amount("12,480", "50", symbol = "€"),
                        style = ArecaTheme.typography.display,
                    )
                    Row(
                        modifier = Modifier.padding(top = ArecaTheme.spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
                    ) {
                        Column {
                            Text("Savings", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                            AmountText(
                                amount = Amount("6,200", "00", symbol = "€"),
                                style = ArecaTheme.typography.headingSmall,
                                masked = true,
                            )
                        }
                        Column {
                            Text("Travel fund", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                            AmountText(
                                amount = Amount("18 300", "00", decimalMark = ",", symbol = "kr", symbolPlacement = AmountSymbolPlacement.After),
                                style = ArecaTheme.typography.headingSmall,
                            )
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { _ ->
                ListItemColumn {
                    AmountLines.forEach { line ->
                        val color = when (line.tone) {
                            AmountTone.Income -> ArecaTheme.colors.fgSuccessText
                            AmountTone.Expense -> ArecaTheme.colors.fgDangerText
                            AmountTone.Plain -> Color.Unspecified
                        }
                        ListItem(
                            title = { Text(line.title) },
                            subtitle = { Text(line.detail) },
                            value = {
                                if (color == Color.Unspecified) {
                                    AmountText(line.amount)
                                } else {
                                    AmountText(line.amount, color = color)
                                }
                            },
                        )
                    }
                }
            }
        },
        {
            DocGroup("Stat") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                Stat(
                    label = "Spent in October",
                    value = "€1,720",
                    modifier = Modifier.padding(padding),
                    note = "€480 under your usual month",
                    size = StatSize.Large,
                    trailing = { Badge(style = BadgeStyle.Success) { Text("−22%") } },
                )
            }
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                StatGrid(modifier = Modifier.padding(padding)) {
                    Stat(label = "Income", value = "€4,250")
                    Stat(label = "Spent", value = "€1,720")
                    Stat(label = "Saved", value = "€2,530", note = "Into savings")
                    Stat(label = "Savings rate", value = "60%", note = "Of income")
                }
            }
        },
    )
}

/**
 * `foundation-states`: the stock empty and failed faces in cards, and loading placeholders shaped
 * like what they stand in for.
 */
internal val FoundationStatesShot = DocShot(name = "foundation-states", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Empty") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                Fallback(
                    title = "No invoices match",
                    modifier = Modifier.padding(padding),
                    body = "Nothing unpaid over €50 this month.",
                    tone = FallbackTone.Empty,
                    actionText = "Clear filters",
                    onAction = {},
                )
            }
            DocGroup("Failed") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                Fallback(
                    title = "Couldn't load transactions",
                    modifier = Modifier.padding(padding),
                    body = "Check your connection and try again.",
                    tone = FallbackTone.Failure,
                    actionText = "Try again",
                    onAction = {},
                )
            }
        },
        {
            DocGroup("Loading") {}
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                SkeletonGroup(
                    contentDescription = "Loading your month",
                    modifier = Modifier.padding(padding),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                    ) {
                        SkeletonBlock(Modifier.size(48.dp), shape = ArecaTheme.shapes.full, animated = false)
                        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                            SkeletonBlock(Modifier.width(140.dp).height(14.dp), animated = false)
                            SkeletonBlock(Modifier.width(88.dp).height(12.dp), animated = false)
                        }
                    }
                    SkeletonBlock(
                        Modifier.padding(top = ArecaTheme.spacing.xl).fillMaxWidth().height(120.dp),
                        shape = ArecaTheme.shapes.md,
                        animated = false,
                    )
                    Row(
                        modifier = Modifier.padding(top = ArecaTheme.spacing.lg),
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    ) {
                        listOf(84.dp, 64.dp, 96.dp).forEach { chip ->
                            SkeletonBlock(
                                Modifier.width(chip).height(32.dp),
                                shape = ArecaTheme.shapes.full,
                                animated = false,
                            )
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
                SkeletonGroup(
                    contentDescription = "Loading transactions",
                    modifier = Modifier.padding(padding),
                ) {
                    listOf(0.55f, 0.4f, 0.65f).forEachIndexed { index, title ->
                        Row(
                            modifier = Modifier.padding(top = if (index == 0) 0.dp else ArecaTheme.spacing.xl),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                        ) {
                            SkeletonBlock(Modifier.size(40.dp), shape = ArecaTheme.shapes.tile, animated = false)
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                            ) {
                                SkeletonBlock(Modifier.fillMaxWidth(title).height(14.dp), animated = false)
                                SkeletonBlock(Modifier.fillMaxWidth(title * 0.6f).height(12.dp), animated = false)
                            }
                            SkeletonBlock(Modifier.width(56.dp).height(14.dp), animated = false)
                        }
                    }
                }
            }
        },
    )
}

/** A hue by name, as the swatches label it. */
@Composable
private fun namedHues(): kotlin.collections.List<Pair<String, Color>> {
    val hues = ArecaTheme.hues
    return listOf(
        "blue" to hues.blue,
        "indigo" to hues.indigo,
        "purple" to hues.purple,
        "pink" to hues.pink,
        "red" to hues.red,
        "orange" to hues.orange,
        "yellow" to hues.yellow,
        "green" to hues.green,
        "teal" to hues.teal,
        "gray" to hues.gray,
    )
}

/** Glyphs the hue swatches carry, one kind of thing to each. */
private val HueGlyphs = listOf(
    ArecaIcons.Car,
    ArecaIcons.Receipt,
    ArecaIcons.Film,
    ArecaIcons.Heart,
    ArecaIcons.Tag,
    ArecaIcons.Coffee,
    ArecaIcons.PiggyBank,
    ArecaIcons.ShoppingCart,
    ArecaIcons.Plane,
    ArecaIcons.Home,
)

/** `theme-hues`: every hue solid and soft, a settings menu on solid tiles and categories on soft ones. */
internal val ThemeHuesShot = DocShot(name = "theme-hues", width = 720.dp) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        DocGroup("ArecaTheme.hues: solidHue over softHue") {
            Card(modifier = Modifier.fillMaxWidth()) { padding ->
                Row(modifier = Modifier.fillMaxWidth().padding(padding)) {
                    namedHues().forEachIndexed { index, (name, hue) ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        ) {
                            FeaturedIcon(
                                imageVector = HueGlyphs[index],
                                contentDescription = null,
                                style = FeaturedIconStyle.solidHue(hue),
                            )
                            FeaturedIcon(
                                imageVector = HueGlyphs[index],
                                contentDescription = null,
                                style = FeaturedIconStyle.softHue(hue),
                            )
                            Text(name, variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle, maxLines = 1)
                        }
                    }
                }
            }
        }
        DocColumns(
            {
                DocGroup("Settings menu") {}
                Settings {
                    Section {
                        SettingsRow(
                            title = { Text("Account") },
                            leading = { SettingsRowIcon(ArecaIcons.User, hue = ArecaTheme.hues.blue) },
                            onClick = {},
                        )
                        SettingsRow(
                            title = { Text("Notifications") },
                            leading = { SettingsRowIcon(ArecaIcons.Bell, hue = ArecaTheme.hues.red) },
                            value = { Text("On") },
                            onClick = {},
                        )
                        SettingsRow(
                            title = { Text("Appearance") },
                            leading = { SettingsRowIcon(ArecaIcons.Palette, hue = ArecaTheme.hues.purple) },
                            value = { Text("System") },
                            onClick = {},
                        )
                        SettingsRow(
                            title = { Text("Privacy") },
                            leading = { SettingsRowIcon(ArecaIcons.Lock, hue = ArecaTheme.hues.gray) },
                            onClick = {},
                        )
                    }
                    Section {
                        SettingsRow(
                            title = { Text("Language") },
                            leading = { SettingsRowIcon(ArecaIcons.Globe, hue = ArecaTheme.hues.teal) },
                            value = { Text("English") },
                            onClick = {},
                        )
                        SettingsRow(
                            title = { Text("Help") },
                            leading = { SettingsRowIcon(ArecaIcons.HelpCircle, hue = ArecaTheme.hues.orange) },
                            onClick = {},
                        )
                    }
                }
            },
            {
                DocGroup("Categories") {}
                Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { _ ->
                    ListItemColumn {
                        listOf(
                            Triple(SampleCategories[0], "€412", 0),
                            Triple(SampleCategories[1], "€186", 1),
                            Triple(SampleCategories[2], "€94", 2),
                            Triple(SampleCategories[3], "€1,120", 3),
                            Triple(SampleCategories[4], "€0", 4),
                        ).forEach { (category, spent, _) ->
                            ListItem(
                                title = { Text(category.label) },
                                subtitle = { Text(category.description) },
                                value = { Text(spent) },
                                leading = {
                                    FeaturedIcon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        style = FeaturedIconStyle.softHue(category.hue()),
                                        size = FeaturedIconSize.Small,
                                    )
                                },
                            )
                        }
                    }
                }
            },
        )
    }
}

/** A month's spending by category, as the panel page lists it. */
private val CategorySpend = listOf(
    Triple(0, "€412", "38%"),
    Triple(3, "€320", "30%"),
    Triple(1, "€186", "17%"),
    Triple(2, "€94", "9%"),
    Triple(5, "€62", "6%"),
)

/** A spending screen: the month's total on the page over its breakdown on a panel, with a floating action. */
@Composable
private fun SpendingPanelScreen() {
    BaseLayout(
        topBar = { TopBar(title = "Spending") },
        bottomBar = { SampleTabBar(selected = SampleDestination.Activity) },
        floatingAction = {
            Button(
                text = "Add expense",
                onClick = {},
                leading = { Icon(ArecaIcons.Plus, contentDescription = null) },
            )
        },
    ) { padding ->
        PanelLayout(
            contentPadding = padding,
            header = {
                Stat(
                    label = "Spent in October",
                    value = "€1,074",
                    modifier = Modifier.contentInsets(),
                    note = "€480 under your usual month",
                    size = StatSize.Large,
                )
            },
            panel = {
                Column(
                    modifier = Modifier.verticalContentInsets(),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    // The rows bring their own inset, so only the heading takes the gutter.
                    Heading(title = { Text("By category") }, modifier = Modifier.horizontalContentInsets())
                    ListItemColumn {
                        CategorySpend.forEach { (index, spent, share) ->
                            val category = SampleCategories[index]
                            NavigationListItem(
                                onClick = {},
                                title = { Text(category.label) },
                                subtitle = { Text("$share of the month") },
                                value = { Text(spent) },
                                onClickLabel = "Open category",
                                leading = {
                                    FeaturedIcon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        style = FeaturedIconStyle.softHue(category.hue()),
                                        size = FeaturedIconSize.Small,
                                    )
                                },
                            )
                        }
                    }
                }
            },
        )
    }
}

/** A new-wallet form whose docked action stays soft until the form is complete. */
@Composable
private fun NewWalletScreen() {
    BaseLayout(
        topBar = { TopBar(title = "New wallet", leading = { BackAction(onClick = {}) }) },
        bottomBar = {
            BottomActionBar(
                actionText = "Create wallet",
                onAction = {},
                caption = "You can rename it or change its currency later.",
                ready = false,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            DocGroup(gap = ArecaTheme.spacing.xl2) {
                FieldGroup(label = "Name", required = true) { field ->
                    TextField(
                        value = "",
                        onValueChange = {},
                        field = field,
                        singleLine = true,
                        placeholder = { Text("Holiday savings") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            DocGroup {
                FieldGroup(label = "Opening balance", assistive = "What the wallet holds today.") { field ->
                    AffixTextField(
                        value = "250.00",
                        onValueChange = {},
                        field = field,
                        prefix = { Text("€") },
                        placeholder = { Text("0.00") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Settings {
                Section(header = { Text("Details") }) {
                    SettingsSelectRow(
                        title = { Text("Currency") },
                        options = listOf("Euro (EUR)", "US dollar (USD)"),
                        selected = "Euro (EUR)",
                        onSelect = {},
                        optionLabel = { it },
                        leading = { SettingsRowIcon(ArecaIcons.Wallet, hue = ArecaTheme.hues.green) },
                    )
                    SettingsToggleRow(
                        checked = false,
                        onCheckedChange = {},
                        title = { Text("Shared with Ada") },
                        leading = { SettingsRowIcon(ArecaIcons.User, hue = ArecaTheme.hues.blue) },
                    )
                }
            }
        }
    }
}

/**
 * `foundation-layouts`: a panel page with a floating action above the tab bar, and a form whose
 * bottom action bar carries a caption and waits, soft, for the form to be complete.
 */
internal val FoundationLayoutsShot = DocShot(
    name = "foundation-layouts",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) { SpendingPanelScreen() },
        DocPanel(width = 352.dp, padded = false) { NewWalletScreen() },
    ),
    height = 680.dp,
)
