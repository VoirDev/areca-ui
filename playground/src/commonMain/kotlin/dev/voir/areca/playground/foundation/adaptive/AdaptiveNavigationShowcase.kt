package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveNavigation
import dev.voir.areca.foundation.adaptive.AdaptiveNavigationLayout
import dev.voir.areca.foundation.adaptive.AdaptiveNavigationScope
import dev.voir.areca.foundation.adaptive.AdaptiveNavigationStyle
import dev.voir.areca.foundation.adaptive.AdaptiveToolbar
import dev.voir.areca.foundation.adaptive.AdaptiveToolbarAction
import dev.voir.areca.foundation.adaptive.EdgeButton
import dev.voir.areca.foundation.adaptive.EdgeButtonEmphasis
import dev.voir.areca.foundation.adaptive.EdgeColumn
import dev.voir.areca.foundation.adaptive.EdgeColumnSide
import dev.voir.areca.foundation.adaptive.EdgeStatus
import dev.voir.areca.foundation.adaptive.NavDestination
import dev.voir.areca.foundation.adaptive.NavRail
import dev.voir.areca.foundation.adaptive.NavRailStyle
import dev.voir.areca.foundation.adaptive.VerticalTabBar
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.Activity
import dev.voir.areca.icons.Archive
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.CloudOff
import dev.voir.areca.icons.Copy
import dev.voir.areca.icons.DotsHorizontal
import dev.voir.areca.icons.Download
import dev.voir.areca.icons.Goal
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.Plus
import dev.voir.areca.icons.Repeat
import dev.voir.areca.icons.Search
import dev.voir.areca.icons.Settings
import dev.voir.areca.icons.Share
import dev.voir.areca.icons.Wallet
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.InputMode

/**
 * Showcase for [AdaptiveNavigation], the [EdgeColumn] it draws on medium windows and wider, and the
 * [NavRail] of its rail style.
 *
 * The sample is a personal-finance app's shell: five destinations, a "New transaction" action, a
 * sync status, and a screen that says which navigation it was given.
 */
@Composable
fun AdaptiveNavigationShowcase() = ShowcaseScaffold(
    title = "Adaptive navigation",
    summary = "An app's top-level destinations as a bottom bar, an edge column or a rail, whichever the window calls for.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick a window and see which navigation it gets; the selection survives every change.") {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.TabletLandscape,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val style by enumControl(
            key = "style",
            default = AdaptiveNavigationStyle.EdgeColumn,
            options = AdaptiveNavigationStyle.entries,
            optionLabel = { it.name },
        )
        val side by enumControl(
            key = "edgeSide",
            default = EdgeColumnSide.End,
            options = EdgeColumnSide.entries,
            optionLabel = { it.name },
        )
        var sidebar by booleanControl(
            key = "sidebarVisible",
            default = false,
            description = "The floating sidebar beside the column, or the sidebar in place of the rail on a large window.",
        )
        val toggle by booleanControl(
            key = "onSidebarVisibleChange",
            default = true,
            description = "Off passes null: no toggle, and sidebarVisible stays as given.",
        )
        val search by booleanControl(
            key = "onSearch",
            default = true,
            description = "The search button under the tabs of the column.",
        )
        val status by booleanControl(
            key = "status",
            default = true,
            description = "The sync status at the head of the column and at the foot of the rail.",
        )
        val header by booleanControl(
            key = "header",
            default = true,
            description = "The primary action: after the toggle in the column, above the destinations of the rail and the sidebar.",
        )

        preview {
            WindowFrame(preset = preset) {
                FinanceShell(
                    preset = preset,
                    style = style,
                    side = side,
                    sidebarVisible = sidebar,
                    onSidebarVisibleChange = if (toggle) ({ sidebar = it }) else null,
                    showSearch = search,
                    showStatus = status,
                    showHeader = header,
                )
            }
        }
    }

    Variants(
        title = "Every window",
        description = "A bottom bar on a phone, a rail on a phone on its side, the edge column from medium up, its clusters either side of a tabletop fold.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { FinanceShell(preset = preset) }
            }
        }
    }

    Variants(
        title = "Rail style",
        description = "AdaptiveNavigationStyle.Rail: a rail from medium up, a sidebar of labelled rows from 1200dp.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in listOf(WindowPreset.FoldUnfolded, WindowPreset.TabletLandscape, WindowPreset.Laptop)) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) {
                    FinanceShell(preset = preset, style = AdaptiveNavigationStyle.Rail, sidebarVisible = true)
                }
            }
        }
    }

    Variants(
        title = "Edge column parts",
        description = "EdgeButton, VerticalTabBar and EdgeStatus on their own, for a shell of your own.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("EdgeButtonEmphasis.Plain", detail = "a toolbar action") {
            EdgeButton(imageVector = ArecaIcons.Share, contentDescription = "Share", onClick = {})
        }
        variant("EdgeButtonEmphasis.Primary", detail = "the screen's one primary action") {
            EdgeButton(
                imageVector = ArecaIcons.Plus,
                contentDescription = "New transaction",
                onClick = {},
                emphasis = EdgeButtonEmphasis.Primary,
            )
        }
        variant("EdgeButtonEmphasis.Toggled", detail = "the sidebar toggle while it is open") {
            EdgeButton(
                imageVector = ArecaIcons.Settings,
                contentDescription = "Hide navigation",
                onClick = {},
                emphasis = EdgeButtonEmphasis.Toggled,
            )
        }
        variant("VerticalTabBar", detail = "icons only, labels announced") {
            TabsSample()
        }
        variant("EdgeStatus", detail = "attention = false") {
            EdgeStatus(imageVector = ArecaIcons.Check, contentDescription = "Synced")
        }
        variant("EdgeStatus", detail = "attention = true") {
            EdgeStatus(imageVector = ArecaIcons.CloudOff, contentDescription = "Offline, 2 changes waiting", attention = true)
        }
    }

    Variants(
        title = "NavRail",
        description = "The rail on its own, for a layout of your own: labels or icons alone, a footer, and its colours.",
        layout = VariantLayout.Grid,
    ) {
        variant("showLabels = true", detail = "header: New transaction") {
            RailSample(showLabels = true)
        }
        variant("showLabels = false", detail = "labels still announced") {
            RailSample(showLabels = false)
        }
        variant("footer", detail = "pinned to the bottom") {
            RailSample(showLabels = true, withFooter = true)
        }
        variant("NavRailStyle.default(…)", detail = "bgSubtle, a rounded-square pill") {
            RailSample(
                showLabels = true,
                style = NavRailStyle.default(
                    bg = ArecaTheme.colors.bgSubtle,
                    indicatorShape = ArecaTheme.shapes.md,
                ),
            )
        }
    }

    Sizes(
        description = "EdgeColumnSize.Default and NavRailSize.Default follow the input: larger under touch.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("Column, touch", detail = "48dp buttons in a 56dp column") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Touch) { TabsSample() }
        }
        variant("Column, pointer", detail = "40dp buttons in a 48dp column") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Pointer) { TabsSample() }
        }
        variant("Rail, touch", detail = "80dp, 56 × 32dp pill") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Touch) {
                RailSample(showLabels = true)
            }
        }
        variant("Rail, pointer", detail = "72dp, 48 × 28dp pill") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Pointer) {
                RailSample(showLabels = true)
            }
        }
    }

    States(description = "Selection and availability of a destination, the same in every form, and the open sidebar.") {
        variant("Sidebar open", detail = "the tabs give way to labelled rows") {
            WindowFrame(preset = WindowPreset.TabletLandscape, maxHeight = 300.dp) {
                FinanceShell(preset = WindowPreset.TabletLandscape, sidebarVisible = true)
            }
        }
        variant("Selected", detail = "Wallets") {
            TabsSample(selected = FinanceTab.Wallets)
        }
        variant("No selection", detail = "selectedValue = null") {
            TabsSample(selected = null)
        }
        variant("Disabled destination", detail = "Subscriptions, enabled = false") {
            TabsSample(disabled = FinanceTab.Subscriptions)
        }
        variant("Disabled action", detail = "EdgeButton, enabled = false") {
            EdgeButton(imageVector = ArecaIcons.Share, contentDescription = "Share", onClick = {}, enabled = false)
        }
    }

    EdgeCases(description = "Windows where the navigation is not what the device suggests, and screens that crowd the column.") {
        case(
            name = "Phone on its side",
            expectation = "852dp wide but 393dp tall: a rail without labels, since the column's two clusters would not fit.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                FinanceShell(preset = WindowPreset.PhoneLandscape)
            }
        }
        case(
            name = "Foldable propped up on a table",
            expectation = "A fold across the middle: status and back stay above it; the tabs, the actions and search below it.",
        ) {
            WindowFrame(preset = WindowPreset.FoldTabletop, maxHeight = 320.dp) {
                FinanceShell(preset = WindowPreset.FoldTabletop)
            }
        }
        case(
            name = "Desktop window dragged narrow",
            expectation = "420dp under a pointer is Compact: the bottom bar, and the toolbar becomes a bar over the screen.",
        ) {
            WindowFrame(preset = WindowPreset.NarrowWindow, maxHeight = 320.dp) {
                FinanceShell(preset = WindowPreset.NarrowWindow)
            }
        }
        case(
            name = "Pushed screen",
            expectation = "Back takes the toggle's place at the head of the column; held from below, the screen's actions sit at its foot, between the tabs and search.",
        ) {
            WindowFrame(preset = WindowPreset.FoldUnfolded, maxHeight = 320.dp) {
                FinanceShell(preset = WindowPreset.FoldUnfolded, pushed = true)
            }
        }
        case(
            name = "More actions than room, on its side",
            expectation = "Header reach: labelled buttons beside the title — Export, Share, and More holding Archive and Duplicate.",
        ) {
            WindowFrame(preset = WindowPreset.TabletLandscape, maxHeight = 300.dp) {
                FinanceShell(preset = WindowPreset.TabletLandscape, manyActions = true, showHeader = false)
            }
        }
        case(
            name = "More actions than room, held upright",
            expectation = "Thumb reach: round buttons at the foot of the column — Export, Share, and More holding the rest.",
        ) {
            WindowFrame(preset = WindowPreset.Tablet, maxHeight = 320.dp) {
                FinanceShell(preset = WindowPreset.Tablet, manyActions = true, showHeader = false)
            }
        }
        case(
            name = "Foldable on its side",
            expectation = "841 × 701dp is expanded and landscape but nearly square: held from below, so the actions go to the foot of the column.",
        ) {
            WindowFrame(preset = WindowPreset.FoldSideways, maxHeight = 280.dp) {
                FinanceShell(preset = WindowPreset.FoldSideways)
            }
        }
        case(
            name = "Six destinations",
            expectation = "The capsule shows four tabs and More, which lists Goals and Settings.",
        ) {
            TabsSample(extra = true)
        }
        case(
            name = "Rail style, sidebar hidden without a toggle",
            expectation = "sidebarVisible = false with no callback: the rail on a laptop, and no toggle to undo it.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 300.dp) {
                FinanceShell(preset = WindowPreset.Laptop, style = AdaptiveNavigationStyle.Rail, sidebarVisible = false)
            }
        }
        case(
            name = "Long label in the rail",
            expectation = "\"Subscriptions & bills\" truncates to one line under its icon rather than widening the rail.",
        ) {
            RailSample(showLabels = true, longLabel = true)
        }
    }
}

/**
 * The finance app's top-level destinations.
 *
 * @property label Name of the destination.
 * @property figureLabel What the destination's headline figure is.
 * @property figure The headline figure its screen opens on.
 */
private enum class FinanceTab(val label: String, val figureLabel: String, val figure: String) {
    Home("Home", "Balance", "€4,182.36"),
    Activity("Activity", "Spent in October", "€1,043.39"),
    Wallets("Wallets", "Across 3 wallets", "€12,640.00"),
    Subscriptions("Subscriptions", "Due this month", "€64.93"),
    More("More", "Plan", "Personal"),
}

/**
 * The destinations, as every form of the navigation shows them.
 *
 * @param disabled Destination drawn as unavailable, or `null` for none.
 * @param longLabel Whether Subscriptions takes a label too long for the rail.
 * @return The destinations, in order.
 */
private fun financeDestinations(
    disabled: FinanceTab? = null,
    longLabel: Boolean = false,
): List<NavDestination<FinanceTab>> = FinanceTab.entries.map { tab ->
    NavDestination(
        value = tab,
        label = if (longLabel && tab == FinanceTab.Subscriptions) "Subscriptions & bills" else tab.label,
        icon = when (tab) {
            FinanceTab.Home -> ArecaIcons.Home
            FinanceTab.Activity -> ArecaIcons.Activity
            FinanceTab.Wallets -> ArecaIcons.Wallet
            FinanceTab.Subscriptions -> ArecaIcons.Repeat
            FinanceTab.More -> ArecaIcons.DotsHorizontal
        },
        enabled = tab != disabled,
    )
}

/**
 * The app's shell, written as a consumer would: [AdaptiveNavigation] around the selected screen,
 * with the selection held above it so it survives a change of form.
 *
 * @param preset Window the shell is drawn in, for its insets.
 * @param style Which forms a medium window and wider takes.
 * @param side The edge the column runs along.
 * @param sidebarVisible Whether the sidebar shows.
 * @param onSidebarVisibleChange Called when the person toggles the sidebar, or `null` for no toggle;
 *   the shell keeps the state itself when left at its default.
 * @param showSearch Whether the column carries search.
 * @param showStatus Whether the navigation carries the sync status.
 * @param showHeader Whether the navigation carries the "New transaction" action.
 * @param pushed Whether a transaction is open over the destination's screen.
 * @param manyActions Whether the screen has more actions than there is room for, beside the title or in the column.
 */
@Composable
private fun FinanceShell(
    preset: WindowPreset,
    style: AdaptiveNavigationStyle = AdaptiveNavigationStyle.EdgeColumn,
    side: EdgeColumnSide = EdgeColumnSide.End,
    sidebarVisible: Boolean = false,
    onSidebarVisibleChange: ((Boolean) -> Unit)? = null,
    showSearch: Boolean = true,
    showStatus: Boolean = true,
    showHeader: Boolean = true,
    pushed: Boolean = false,
    manyActions: Boolean = false,
) {
    var tab by remember { mutableStateOf(FinanceTab.Home) }
    var sidebar by remember(sidebarVisible) { mutableStateOf(sidebarVisible) }
    var open by remember(pushed) { mutableStateOf(pushed) }
    val destinations = remember { financeDestinations() }

    AdaptiveNavigation(
        destinations = destinations,
        selectedValue = tab,
        onValueSelected = { tab = it },
        modifier = Modifier.windowInsetsPadding(preset.windowInsets),
        style = style,
        edgeSide = side,
        sidebarVisible = if (onSidebarVisibleChange != null) sidebarVisible else sidebar,
        onSidebarVisibleChange = onSidebarVisibleChange ?: { sidebar = it },
        onSearch = if (showSearch) ({}) else null,
        status = if (showStatus) ({ SyncStatus() }) else null,
        header = if (showHeader) ({ NewTransactionAction() }) else null,
        footer = { Text("Synced just now", variant = TextVariant.BodySmall, color = TextColor.Muted) },
    ) {
        FinanceScreen(tab = tab, open = open, onBack = { open = false }, manyActions = manyActions)
    }
}

/** The sync status: an icon in the column, a line at the foot of the rail. */
@Composable
private fun AdaptiveNavigationScope.SyncStatus() {
    if (layout == AdaptiveNavigationLayout.EdgeColumn) {
        EdgeStatus(imageVector = ArecaIcons.Check, contentDescription = "Synced just now")
    } else {
        IconButton(
            imageVector = ArecaIcons.Check,
            contentDescription = "Synced just now",
            onClick = null,
            style = IconButtonStyle.plain(ActionColor.Neutral),
        )
    }
}

/** The primary action: an edge button in the column, a labelled button in the sidebar, an icon button on the rail. */
@Composable
private fun AdaptiveNavigationScope.NewTransactionAction() {
    when (layout) {
        AdaptiveNavigationLayout.EdgeColumn -> EdgeButton(
            imageVector = ArecaIcons.Plus,
            contentDescription = "New transaction",
            onClick = {},
            emphasis = EdgeButtonEmphasis.Primary,
        )
        AdaptiveNavigationLayout.Sidebar -> Button(text = "New transaction", onClick = {}, modifier = Modifier.fillMaxWidth())
        else -> IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = {})
    }
}

/**
 * The selected destination's screen: its toolbar, its headline figure, and which navigation and
 * width class it was given. In the bottom-bar form the toolbar carries the action itself.
 *
 * @param tab The selected destination.
 * @param open Whether a transaction is open over it, with back in its toolbar.
 * @param onBack Called when the person leaves the open transaction.
 * @param manyActions Whether the screen has more actions than there is room for, beside the title or in the column.
 */
@Composable
private fun AdaptiveNavigationScope.FinanceScreen(
    tab: FinanceTab,
    open: Boolean,
    onBack: () -> Unit,
    manyActions: Boolean,
) {
    val screen = ArecaTheme.window
    val actions = buildList {
        if (layout == AdaptiveNavigationLayout.BottomBar) {
            add(AdaptiveToolbarAction(ArecaIcons.Plus, "New transaction", onClick = {}, primary = true))
        }
        if (manyActions) {
            add(AdaptiveToolbarAction(ArecaIcons.Download, "Export", onClick = {}))
            add(AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = {}))
            add(AdaptiveToolbarAction(ArecaIcons.Archive, "Archive", onClick = {}))
            add(AdaptiveToolbarAction(ArecaIcons.Copy, "Duplicate", onClick = {}))
        } else {
            add(AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = {}))
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
        AdaptiveToolbar(
            title = if (open) "Lidl" else tab.label,
            onBack = if (open) onBack else null,
            actions = actions,
        )
        Column(
            modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Stat(
                label = if (open) "Groceries · Today" else tab.figureLabel,
                value = if (open) "−€48.20" else tab.figure,
                size = StatSize.Large,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Drawn with", variant = TextVariant.BodySmall, color = TextColor.Muted)
                Badge(style = BadgeStyle.Brand) { Text(layout.name) }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Screen · ${screen.size.width.value.toInt()}dp",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
                Badge(style = BadgeStyle.Neutral) { Text(screen.width.name) }
            }
        }
    }
}

/**
 * A standalone [VerticalTabBar], for the cells that show the capsule on its own.
 *
 * @param selected The selected destination, or `null` for none.
 * @param disabled Destination drawn as unavailable, or `null` for none.
 * @param extra Whether Goals and Settings join the destinations, past the capsule's room.
 */
@Composable
private fun TabsSample(
    selected: FinanceTab? = FinanceTab.Home,
    disabled: FinanceTab? = null,
    extra: Boolean = false,
) {
    var value by remember { mutableStateOf<Any?>(selected) }
    val destinations: List<NavDestination<Any>> = remember(disabled, extra) {
        val base: List<NavDestination<Any>> = financeDestinations(disabled).filter { it.value != FinanceTab.More }
            .map { NavDestination(it.value, it.label, it.icon, it.enabled) }
        if (extra) {
            base + listOf(
                NavDestination<Any>("Goals", "Goals", ArecaIcons.Goal),
                NavDestination<Any>("Settings", "Settings", ArecaIcons.Settings),
            )
        } else {
            base
        }
    }
    VerticalTabBar(destinations = destinations, selectedValue = value, onValueSelected = { value = it })
}

/**
 * A standalone [NavRail] at a fixed height, for the cells that show the rail on its own.
 *
 * @param showLabels Whether labels are drawn under the icons.
 * @param selected The selected destination, or `null` for none.
 * @param disabled Destination drawn as unavailable, or `null` for none.
 * @param withFooter Whether a settings button sits at the foot of the rail.
 * @param longLabel Whether Subscriptions takes a label too long for the rail.
 * @param style Colours of the rail.
 */
@Composable
private fun RailSample(
    showLabels: Boolean,
    selected: FinanceTab? = FinanceTab.Home,
    disabled: FinanceTab? = null,
    withFooter: Boolean = false,
    longLabel: Boolean = false,
    style: NavRailStyle = NavRailStyle.Default,
) {
    var value by remember { mutableStateOf(selected) }
    val destinations = remember(disabled, longLabel) { financeDestinations(disabled, longLabel) }
    Box(modifier = Modifier.height(480.dp)) {
        NavRail(
            destinations = destinations,
            selectedValue = value,
            onValueSelected = { value = it },
            style = style,
            showLabels = showLabels,
            header = {
                IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = {})
            },
            footer = if (withFooter) {
                {
                    IconButton(
                        imageVector = ArecaIcons.Settings,
                        contentDescription = "Settings",
                        onClick = {},
                        style = IconButtonStyle.plain(ActionColor.Neutral),
                    )
                }
            } else {
                null
            },
        )
    }
}
