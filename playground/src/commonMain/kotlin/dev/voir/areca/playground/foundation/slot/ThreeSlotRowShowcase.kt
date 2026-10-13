package dev.voir.areca.playground.foundation.slot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.SlotSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** How the three slots divide the available width. */
private enum class SlotStrategy(val label: String) {
    Default("start wraps, center takes the rest, end wraps"),
    Centered("sides wrap, center holds the row's centre line"),
    Fill("all three share the width equally"),
    Wrap("all three wrap, centred as a group"),
}

/** Showcase for [SlotRow]. */
@Composable
fun ThreeSlotRowShowcase() = ShowcaseScaffold(
    title = "Three slot row",
    summary = "Row primitive that divides its width between start, center and end slots.",
    module = "foundation",
) {
    Playground {
        val strategy by enumControl(
            key = "layout",
            default = SlotStrategy.Default,
            options = SlotStrategy.entries,
            optionLabel = { it.name },
        )
        val gap by numberControl(
            key = "gap",
            default = 0f,
            range = 0f..32f,
            step = 4f,
            format = { "${it.toInt()}dp" },
        )
        val withStart by booleanControl("start", default = true, label = "start slot")
        val withEnd by booleanControl("end", default = true, label = "end slot")

        preview {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = strategy.resolve(gap.dp),
                start = if (withStart) ({ SlotBlock("start", ArecaTheme.colors.bgBrandMuted) }) else null,
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
                end = if (withEnd) ({ SlotBlock("end", ArecaTheme.colors.bgSuccessMuted) }) else null,
            )
        }
    }

    Anatomy(description = "Three positional slots. Only center is required.") {
        slot(name = "start", description = "Leading affordance — back button, icon, avatar.") {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                start = { SlotBlock("start", ArecaTheme.colors.bgBrandMuted) },
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
            )
        }
        slot(name = "center", description = "The row's subject. Required.") {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
            )
        }
        slot(name = "end", description = "Trailing actions — buttons, chevron, status.") {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
                end = { SlotBlock("end", ArecaTheme.colors.bgSuccessMuted) },
            )
        }
    }

    Variants(
        title = "Layout strategies",
        description = "How SlotRowLayout divides the width. The blocks are equal width on purpose.",
        layout = VariantLayout.Vertical,
    ) {
        for (strategy in SlotStrategy.entries) {
            variant(name = strategy.name, detail = strategy.label) {
                SlotRow(
                    modifier = Modifier.fillMaxWidth(),
                    layout = strategy.resolve(0.dp),
                    start = { SlotBlock("start", ArecaTheme.colors.bgBrandMuted) },
                    center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
                    end = { SlotBlock("end", ArecaTheme.colors.bgSuccessMuted) },
                )
            }
        }
        variant(name = "Fixed start", detail = "SlotSize.Fixed(96.dp)") {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout(start = SlotSize.Fixed(96.dp)),
                start = { SlotBlock("96dp", ArecaTheme.colors.bgBrandMuted) },
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
                end = { SlotBlock("end", ArecaTheme.colors.bgSuccessMuted) },
            )
        }
    }

    Recipes {
        recipe(
            name = "Screen header",
            description = "The most common use: back affordance, title, single action.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout(
                    centerAlignment = Alignment.Center,
                    startGap = ArecaTheme.spacing.lg,
                    endGap = ArecaTheme.spacing.lg,
                ),
                start = { Text(text = "‹ Back", variant = TextVariant.LabelMedium, color = TextColor.Brand) },
                center = { Text(text = "Order #4821", variant = TextVariant.LabelLarge) },
                end = { Text(text = "Edit", variant = TextVariant.LabelMedium, color = TextColor.Brand) },
            )
        }
        recipe(
            name = "Balanced toolbar",
            description = "Centered keeps the middle on the row's centre line without cutting the controls.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout.Centered,
                start = { Text(text = "Cancel", variant = TextVariant.LabelMedium, color = TextColor.Subtle) },
                center = { Text(text = "Filters", variant = TextVariant.LabelLarge) },
                end = { Text(text = "Apply", variant = TextVariant.LabelMedium, color = TextColor.Brand) },
            )
        }
    }

    EdgeCases {
        case(
            name = "Uneven slot widths with Default",
            expectation = "Centre is pushed off true centre — this is why Centered exists.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                start = { Text(text = "A very long start label", variant = TextVariant.BodySmall) },
                center = { Text(text = "Centre", variant = TextVariant.LabelLarge) },
                end = { Text(text = "OK", variant = TextVariant.BodySmall) },
            )
        }
        case(
            name = "The same content with Centered",
            expectation = "Centre sits on the row's centre line; both side labels keep their width.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout.Centered,
                start = { Text(text = "A very long start label", variant = TextVariant.BodySmall) },
                center = { Text(text = "Centre", variant = TextVariant.LabelLarge) },
                end = { Text(text = "OK", variant = TextVariant.BodySmall) },
            )
        }
        case(
            name = "A wide trailing label under Fill",
            expectation = "Equal thirds cut the trailing label off — the reason Centered replaces " +
                "Fill for anything with controls.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout.Fill,
                start = { Text(text = "Cancel", variant = TextVariant.LabelMedium, color = TextColor.Subtle) },
                center = { Text(text = "New expense", variant = TextVariant.LabelLarge) },
                end = { Text(text = "Save changes", variant = TextVariant.LabelMedium, color = TextColor.Brand, maxLines = 1) },
            )
        }
        case(
            name = "The same row under Centered",
            expectation = "The trailing label keeps its full width; the title yields instead.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                layout = SlotRowLayout.Centered,
                start = { Text(text = "Cancel", variant = TextVariant.LabelMedium, color = TextColor.Subtle) },
                center = { Text(text = "New expense", variant = TextVariant.LabelLarge, maxLines = 1) },
                end = { Text(text = "Save changes", variant = TextVariant.LabelMedium, color = TextColor.Brand, maxLines = 1) },
            )
        }
        case(
            name = "Centre only",
            expectation = "Fills the row; the omitted slots take no width.",
        ) {
            SlotRow(
                modifier = Modifier.fillMaxWidth(),
                center = { SlotBlock("center", ArecaTheme.colors.bgInfoMuted) },
            )
        }
    }
}

@Composable
private fun SlotStrategy.resolve(gap: Dp): SlotRowLayout = when (this) {
    SlotStrategy.Default -> SlotRowLayout(gap = gap)
    SlotStrategy.Centered -> SlotRowLayout.Centered.copy(gap = gap, startGap = gap, endGap = gap)
    SlotStrategy.Fill -> SlotRowLayout.Fill.copy(gap = gap, startGap = gap, endGap = gap)
    SlotStrategy.Wrap -> SlotRowLayout.wrap(gap = gap)
}

@Composable
private fun SlotBlock(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(ArecaTheme.radius.sm))
            .background(color)
            .padding(horizontal = ArecaTheme.spacing.lg, vertical = ArecaTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, variant = TextVariant.LabelSmall)
    }
}
