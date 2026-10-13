package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.DividerDash
import dev.voir.areca.foundation.layout.DividerSize
import dev.voir.areca.foundation.layout.DividerTextPosition
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.layout.LocalDividerSize
import dev.voir.areca.foundation.layout.LocalDividerSize
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Showcase for [HorizontalDivider] and [VerticalDivider]. */
@Composable
fun DividerShowcase() = ShowcaseScaffold(
    title = "Divider",
    summary = "Separates content, optionally carrying a label and a dashed line.",
    module = "foundation",
) {
    Playground {
        val label by textControl("text", default = "OR", placeholder = "Leave empty for a plain rule")
        val position by enumControl(
            key = "textPosition",
            default = DividerTextPosition.Center,
            options = DividerTextPosition.entries,
            optionLabel = { it.name },
        )
        val dashed by booleanControl("dashed", default = false)

        val size = (LocalDividerSize.current ?: DividerSize.Default).let { base ->
            if (dashed) base.copy(dash = DividerDash(length = 8.dp, gap = 6.dp)) else base
        }

        preview {
            if (label.isEmpty()) {
                HorizontalDivider(size = size, modifier = Modifier.fillMaxWidth())
            } else {
                HorizontalDivider(
                    text = label,
                    textPosition = position,
                    size = size,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    Variants(
        title = "Horizontal",
        description = "Where the label sits along the rule, and the dashed treatment.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Plain") {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }
        for (position in DividerTextPosition.entries) {
            variant("Text / ${position.name}") {
                HorizontalDivider(
                    text = position.name.uppercase(),
                    textPosition = position,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        variant("Dashed", detail = "8dp dash, 6dp gap") {
            HorizontalDivider(
                text = "DASHED",
                size = DividerSize.Default.copy(dash = DividerDash(length = 8.dp, gap = 6.dp)),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Variants(
        title = "Vertical",
        description = "The same treatments rotated; each cell is 180dp tall.",
        layout = VariantLayout.Grid,
    ) {
        variant("Plain") {
            VerticalColumnHost { VerticalDivider() }
        }
        for (position in DividerTextPosition.entries) {
            variant("Text / ${position.name}") {
                VerticalColumnHost {
                    VerticalDivider(text = position.name.uppercase(), textPosition = position)
                }
            }
        }
        variant("Dashed", detail = "8dp dash, 6dp gap") {
            VerticalColumnHost {
                VerticalDivider(
                    text = "DASHED",
                    size = DividerSize.Default.copy(dash = DividerDash(length = 8.dp, gap = 6.dp)),
                )
            }
        }
    }
}

/** A vertical divider needs a bounded height to draw into; the cells do not provide one. */
@Composable
private fun VerticalColumnHost(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.height(180.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}
