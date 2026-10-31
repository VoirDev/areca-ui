package dev.voir.areca.playground.foundation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.TooltipBox
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.icons.Download
import dev.voir.areca.icons.Search
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.window.InputMode

/**
 * Showcase for [TooltipBox] and the tooltip an [IconButton] shows under a pointer.
 *
 * A tooltip appears only while the pointer rests on its element, so every preview here is a window
 * to point at rather than a picture of the label.
 */
@Composable
fun TooltipShowcase() = ShowcaseScaffold(
    title = "Tooltip",
    summary = "A short label naming a control when the pointer rests on it or keyboard focus reaches it.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Rest the pointer on the badge or a button for half a second; Tab to them from the keyboard. " +
            "Under touch nothing shows.",
    ) {
        val text by textControl(key = "text", default = "Synced 2 minutes ago")
        val placement by enumControl(
            key = "placement",
            default = AnchoredPlacement.BelowCenter,
            options = AnchoredPlacement.entries,
            optionLabel = { it.name },
            description = "Where the label sits; it flips when it would leave the window.",
        )
        val enabled by booleanControl(key = "enabled", default = true)
        val input by enumControl(
            key = "input",
            default = InputMode.Pointer,
            options = InputMode.entries,
            optionLabel = { it.name },
        )

        preview {
            WindowFrame(preset = WindowPreset.Laptop, input = input, maxHeight = 200.dp) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TooltipBox(text = text, placement = placement, enabled = enabled) {
                        Badge(style = BadgeStyle.Success) { Text("Synced") }
                    }
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Pointer", detail = "shows after 500ms at rest, at once on keyboard focus") {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 120.dp) { Toolbar() }
        }
        variant("Touch", detail = "never shows; a long press belongs to the element") {
            WindowFrame(preset = WindowPreset.Tablet, input = InputMode.Touch, maxHeight = 120.dp) { Toolbar() }
        }
    }

    Recipes {
        recipe(
            name = "Toolbar of icon buttons",
            description = "Each IconButton shows its content description: no TooltipBox needed.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 160.dp) {
                TopBar(title = "Statements", trailing = { Toolbar() })
            }
        }
    }

    EdgeCases {
        case(
            name = "Long label",
            expectation = "The label wraps at 280dp rather than running across the window.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 200.dp) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TooltipBox(
                        text = "Last synced 2 minutes ago from the Main account; the next sync runs when the app " +
                            "comes back to the foreground.",
                    ) {
                        Badge(style = BadgeStyle.Success) { Text("Synced") }
                    }
                }
            }
        }
        case(
            name = "At the window's bottom edge",
            expectation = "A label asked for below flips above the element instead of leaving the window.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 200.dp) {
                Box(Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.BottomCenter) {
                    TooltipBox(text = "Synced 2 minutes ago") {
                        Badge(style = BadgeStyle.Success) { Text("Synced") }
                    }
                }
            }
        }
    }
}

/** A row of icon buttons, each named by its content description. */
@Composable
private fun Toolbar() {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val style = IconButtonStyle.plain(ActionColor.Neutral)
        IconButton(ArecaIcons.Search, contentDescription = "Search statements", onClick = {}, style = style)
        IconButton(ArecaIcons.Download, contentDescription = "Download as PDF", onClick = {}, style = style)
        IconButton(ArecaIcons.Share, contentDescription = "Share", onClick = {}, style = style)
        IconButton(ArecaIcons.Bell, contentDescription = "Payment reminders", onClick = {}, style = style)
    }
}
