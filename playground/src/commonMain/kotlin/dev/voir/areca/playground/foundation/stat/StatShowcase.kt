package dev.voir.areca.playground.foundation.stat

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** Showcase for [Stat] and [StatGrid]. */
@Composable
fun StatShowcase() = ShowcaseScaffold(
    title = "Stat",
    summary = "A figure with what it is and what it means, alone or in a grid of figures.",
    module = "foundation",
) {
    Playground {
        val label by textControl("label", default = "Spent")
        val value by textControl("value", default = "€2,140")
        val note by textControl("note", default = "a month", description = "Empty for none.")
        val large by booleanControl("Large", default = false, description = "StatSize.Large")
        val pressable by booleanControl("onClick", default = false)

        preview {
            Stat(
                label = label,
                value = value,
                note = note.ifBlank { null },
                onClick = if (pressable) ({}) else null,
                onClickLabel = "Open spending",
                size = if (large) StatSize.Large else StatSize.Default,
            )
        }
    }

    Sizes(layout = VariantLayout.Vertical) {
        variant("Default", detail = "a figure in a grid") {
            Stat(label = "Earned", value = "€4,180", note = "a month")
        }
        variant("Large", detail = "the figure a section is about, with its change") {
            Stat(
                label = "October so far",
                value = "€2,340",
                note = "€218 under your usual pace",
                size = StatSize.Large,
                trailing = { Badge(style = BadgeStyle.Neutral) { Text("−9%") } },
            )
        }
    }

    States {
        variant("Read only") { Stat(label = "Kept", value = "41%", note = "of income") }
        variant("Pressable", detail = "Opens what the figure is about; no visible container.") {
            Stat(label = "Kept", value = "41%", note = "of income", onClick = {}, onClickLabel = "Open income")
        }
        variant("No note") { Stat(label = "A day", value = "€71.20") }
    }

    Recipes {
        recipe(
            name = "Your usual month",
            description = "Four figures in two columns, values level across each row.",
        ) {
            StatGrid {
                Stat(label = "Spent", value = "€2,140", note = "a month", onClick = {})
                Stat(label = "Earned", value = "€4,180", note = "a month", onClick = {})
                Stat(label = "Kept", value = "49%", note = "of income")
                Stat(label = "A day", value = "€71.20", note = "weekends 1.8× weekdays")
            }
        }
    }

    EdgeCases {
        case(name = "Odd count", expectation = "The last row holds one stat at the start edge.") {
            StatGrid {
                Stat(label = "Spent", value = "€2,140")
                Stat(label = "Earned", value = "€4,180")
                Stat(label = "Kept", value = "49%")
            }
        }
        case(name = "Long note", expectation = "The note wraps; its row grows and the next row stays aligned.") {
            StatGrid {
                Stat(label = "A day", value = "€71.20", note = "weekends cost 1.8 times as much as weekdays this quarter")
                Stat(label = "Kept", value = "49%", note = "of income")
            }
        }
        case(name = "Narrow container", expectation = "Constrained to 160dp; a long figure stays on one line.") {
            Stat(label = "Spent this year", value = "€25,680.40", modifier = Modifier.width(160.dp))
        }
        case(name = "Right to left", expectation = "The grid fills from the right.") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                StatGrid {
                    Stat(label = "Spent", value = "€2,140")
                    Stat(label = "Earned", value = "€4,180")
                    Stat(label = "Kept", value = "49%")
                }
            }
        }
    }
}
