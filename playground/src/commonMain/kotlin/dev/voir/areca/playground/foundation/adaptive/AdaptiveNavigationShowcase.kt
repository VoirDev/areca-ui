package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.voir.areca.foundation.adaptive.NavDestination
import dev.voir.areca.foundation.adaptive.NavRail
import dev.voir.areca.foundation.adaptive.NavRailStyle
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
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Activity
import dev.voir.areca.icons.DotsHorizontal
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.Plus
import dev.voir.areca.icons.Repeat
import dev.voir.areca.icons.Settings
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
 * Showcase for [AdaptiveNavigation] and the [NavRail] it draws on medium and expanded windows.
 *
 * The sample is a personal-finance app's shell: five destinations, a "New transaction" action in the
 * rail or sidebar header, and a screen that says which navigation it was given.
 */
@Composable
fun AdaptiveNavigationShowcase() = ShowcaseScaffold(
    title = "Adaptive navigation",
    summary = "An app's top-level destinations as a bottom bar, a rail or a sidebar, whichever the window calls for.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick a window and see which navigation it gets; the selection survives every change.") {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Laptop,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        var collapsed by booleanControl(
            key = "sidebarCollapsed",
            default = false,
            description = "Large windows and wider only: draws the rail in place of the sidebar.",
        )
        val toggle by booleanControl(
            key = "onSidebarCollapsedChange",
            default = true,
            description = "Off passes null: no toggle at the foot, and sidebarCollapsed stays as given.",
        )
        val header by booleanControl(
            key = "header",
            default = true,
            description = "The primary action above the destinations of the rail and the sidebar.",
        )

        preview {
            WindowFrame(preset = preset) {
                FinanceShell(
                    preset = preset,
                    sidebarCollapsed = collapsed,
                    onSidebarCollapsedChange = if (toggle) ({ collapsed = it }) else null,
                    showHeader = header,
                )
            }
        }
    }

    Variants(
        title = "Every window",
        description = "A bottom bar on a phone, a rail from medium up and on a phone on its side, a sidebar from 1200dp.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { FinanceShell(preset = preset) }
            }
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
        description = "NavRailSize.Default follows the input: wider with a larger pill under touch.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("Touch", detail = "80dp, 56 × 32dp pill") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Touch) {
                RailSample(showLabels = true)
            }
        }
        variant("Pointer", detail = "72dp, 48 × 28dp pill") {
            ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = InputMode.Pointer) {
                RailSample(showLabels = true)
            }
        }
    }

    States(description = "Selection and availability of a destination, the same in every form.") {
        variant("Selected", detail = "Wallets") {
            RailSample(showLabels = true, selected = FinanceTab.Wallets)
        }
        variant("No selection", detail = "selectedValue = null") {
            RailSample(showLabels = true, selected = null)
        }
        variant("Disabled destination", detail = "Subscriptions, enabled = false") {
            RailSample(showLabels = true, disabled = FinanceTab.Subscriptions)
        }
    }

    EdgeCases(description = "Windows where the navigation is not what the device suggests.") {
        case(
            name = "Phone on its side",
            expectation = "852dp wide but 393dp tall: a rail without labels, since two bars would leave no screen.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                FinanceShell(preset = WindowPreset.PhoneLandscape)
            }
        }
        case(
            name = "Desktop window dragged narrow",
            expectation = "420dp under a pointer is Compact: the bottom bar, and the action moves into the screen.",
        ) {
            WindowFrame(preset = WindowPreset.NarrowWindow, maxHeight = 320.dp) {
                FinanceShell(preset = WindowPreset.NarrowWindow)
            }
        }
        case(
            name = "13\" tablet on its side",
            expectation = "1376dp is Large: the sidebar, even though the window is touched.",
        ) {
            WindowFrame(preset = WindowPreset.LargeTabletLandscape, maxHeight = 300.dp) {
                FinanceShell(preset = WindowPreset.LargeTabletLandscape)
            }
        }
        case(
            name = "Collapsed sidebar without a toggle",
            expectation = "sidebarCollapsed = true with no callback: the rail on a laptop, and no toggle to undo it.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 300.dp) {
                FinanceShell(preset = WindowPreset.Laptop, sidebarCollapsed = true, onSidebarCollapsedChange = null)
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
 * @param sidebarCollapsed Whether the sidebar is collapsed to the rail.
 * @param onSidebarCollapsedChange Called when the person toggles the sidebar, or `null` for no toggle.
 * @param showHeader Whether the rail and the sidebar carry the "New transaction" action.
 */
@Composable
private fun FinanceShell(
    preset: WindowPreset,
    sidebarCollapsed: Boolean = false,
    onSidebarCollapsedChange: ((Boolean) -> Unit)? = null,
    showHeader: Boolean = true,
) {
    var tab by remember { mutableStateOf(FinanceTab.Home) }
    val destinations = remember { financeDestinations() }

    AdaptiveNavigation(
        destinations = destinations,
        selectedValue = tab,
        onValueSelected = { tab = it },
        modifier = Modifier.windowInsetsPadding(preset.windowInsets),
        sidebarCollapsed = sidebarCollapsed,
        onSidebarCollapsedChange = onSidebarCollapsedChange,
        header = if (showHeader) ({ NewTransactionAction() }) else null,
    ) {
        FinanceScreen(tab = tab)
    }
}

/** The primary action: a labelled button in the sidebar, an icon button on the rail. */
@Composable
private fun AdaptiveNavigationScope.NewTransactionAction() {
    if (layout == AdaptiveNavigationLayout.Sidebar) {
        Button(text = "New transaction", onClick = {}, modifier = Modifier.fillMaxWidth())
    } else {
        IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = {})
    }
}

/**
 * The selected destination's screen: its title, its headline figure, and which navigation and
 * width class it was given. In the bottom-bar form the screen carries the action itself.
 *
 * @param tab The selected destination.
 */
@Composable
private fun AdaptiveNavigationScope.FinanceScreen(tab: FinanceTab) {
    val screen = ArecaTheme.window
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = tab.label,
            trailing = if (layout == AdaptiveNavigationLayout.BottomBar) {
                { AddAction(onClick = {}, contentDescription = "New transaction") }
            } else {
                null
            },
        )
        Column(
            modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Stat(label = tab.figureLabel, value = tab.figure, size = StatSize.Large)
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
