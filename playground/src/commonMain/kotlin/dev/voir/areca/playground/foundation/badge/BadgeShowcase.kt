package dev.voir.areca.playground.foundation.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.badge.NotificationBadge
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.ArrowDown
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Cross
import dev.voir.areca.icons.WarningSeal
import dev.voir.areca.icons.X
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Style presets offered by [BadgeStyle], as a selectable list. */
private enum class BadgeStyleOption {
    /** [BadgeStyle.Default]. */
    Default,

    /** [BadgeStyle.Brand]. */
    Brand,

    /** [BadgeStyle.Danger]. */
    Danger,

    /** [BadgeStyle.Warning]. */
    Warning,

    /** [BadgeStyle.Info]. */
    Info,

    /** [BadgeStyle.Success]. */
    Success,

    /** [BadgeStyle.Neutral]. */
    Neutral,

    /** [BadgeStyle.NeutralDark]. */
    NeutralDark,

    /** [BadgeStyle.Chip]: a filter chip — solid dark when chosen, the bare word when soft. */
    Chip,
}

/** Sizes offered by [BadgeSize], as a selectable list. */
private enum class BadgeSizeOption {
    /** [BadgeSize.ExtraSmall]. */
    ExtraSmall,

    /** [BadgeSize.Small]. */
    Small,

    /** [BadgeSize.Medium]. */
    Medium,

    /** [BadgeSize.Large]. */
    Large,

    /** [BadgeSize.Chip]: as wide as its word, for a row of filter chips. */
    Chip,
}

/** Showcase for [Badge] and [NotificationBadge]. */
@Composable
fun BadgeShowcase() = ShowcaseScaffold(
    title = "Badge",
    summary = "Compact status marker, optionally clickable and carrying icons.",
    module = "foundation",
) {
    Playground {
        val label by textControl("label", default = "Active", placeholder = "Badge label")
        val style by enumControl(
            key = "style",
            default = BadgeStyleOption.Brand,
            options = BadgeStyleOption.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = BadgeSizeOption.Medium,
            options = BadgeSizeOption.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val soft by booleanControl(
            key = "soft",
            default = false,
            description = "Tinted rather than filled.",
        )
        val clickable by booleanControl("clickable", default = false)
        val withLeading by booleanControl("leading", default = false, label = "leading icon")
        val withTrailing by booleanControl("trailing", default = false, label = "trailing icon")

        preview {
            Badge(
                style = style.resolve(),
                size = size.resolve(),
                enabled = enabled,
                soft = soft,
                onClick = if (clickable) ({}) else null,
                leading = if (withLeading) {
                    { Icon(imageVector = ArecaIcons.Check, contentDescription = null) }
                } else {
                    null
                },
                trailing = if (withTrailing) {
                    { Icon(imageVector = ArecaIcons.ArrowDown, contentDescription = null) }
                } else {
                    null
                },
            ) {
                Text(label)
            }
        }
    }

    Variants(
        title = "Styles",
        description = "Filled on the left of each pair, soft on the right.",
        layout = VariantLayout.Grid,
    ) {
        for (style in BadgeStyleOption.entries) {
            variant(name = style.name) {
                Badge(style = style.resolve()) { Text(style.name) }
            }
            variant(name = "${style.name} / soft") {
                Badge(style = style.resolve(), soft = true) { Text(style.name) }
            }
        }
    }

    Sizes(description = "Each step changes padding, gap, icon size and text style together.") {
        for (size in BadgeSizeOption.entries) {
            variant(name = size.name) {
                Badge(size = size.resolve(), style = BadgeStyle.Brand) { Text(size.name) }
            }
        }
    }

    States {
        variant("Enabled") {
            Badge(style = BadgeStyle.Success) { Text("Enabled") }
        }
        variant("Soft") {
            Badge(style = BadgeStyle.Success, soft = true) { Text("Soft") }
        }
        variant("Disabled") {
            Badge(style = BadgeStyle.Success, enabled = false) { Text("Disabled") }
        }
        variant("Clickable", detail = "cursor changes on hover") {
            Badge(style = BadgeStyle.Info, onClick = {}) { Text("Clickable") }
        }
    }

    Variants(
        title = "With icons",
        description = "Leading and trailing slots take the badge's own icon size.",
        layout = VariantLayout.Grid,
    ) {
        variant("Leading") {
            Badge(
                style = BadgeStyle.Brand,
                leading = { Icon(imageVector = ArecaIcons.Check, contentDescription = null) },
            ) {
                Text("Leading")
            }
        }
        variant("Trailing") {
            Badge(
                style = BadgeStyle.Danger,
                trailing = { Icon(imageVector = ArecaIcons.Cross, contentDescription = null) },
            ) {
                Text("Trailing")
            }
        }
        variant("Both") {
            Badge(
                style = BadgeStyle.Warning,
                leading = { Icon(imageVector = ArecaIcons.WarningSeal, contentDescription = null) },
                trailing = { Icon(imageVector = ArecaIcons.ArrowDown, contentDescription = null) },
            ) {
                Text("Both")
            }
        }
    }

    Variants(
        title = "Notification badge",
        description = "The circular counter variant, for unread and pending counts.",
        layout = VariantLayout.Grid,
    ) {
        variant("Single digit") { NotificationBadge(text = "3") }
        variant("Double digit") { NotificationBadge(text = "12") }
        variant("Capped") { NotificationBadge(text = "9+") }
        variant("Danger") { NotificationBadge(text = "5", style = BadgeStyle.Danger) }
    }

    Recipes {
        recipe(
            name = "Removable choices",
            description = "What a filter has chosen, as solid chips with a swatch; pressing one " +
                "removes it, and its close mark reads as Remove.",
        ) {
            var chosen by remember { mutableStateOf(listOf("Groceries", "Restaurants", "Lisbon trip")) }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
            ) {
                chosen.forEach { name ->
                    Badge(
                        style = BadgeStyle.Chip,
                        size = BadgeSize.Chip,
                        onClick = { chosen = chosen - name },
                        leading = {
                            Box(
                                Modifier
                                    .size(ArecaTheme.spacing.lg)
                                    .background(ArecaTheme.colors.bgInfoBase, ArecaTheme.shapes.xs),
                            )
                        },
                        trailing = { Icon(ArecaIcons.X, contentDescription = "Remove") },
                    ) {
                        Text(name)
                    }
                }
            }
        }
        recipe(
            name = "Toggle chips with a key",
            description = "FlowBadgeSelect in multiple mode on BadgeStyle.FilledChip, each chip led " +
                "by its colour dot and ending in how often it is used.",
        ) {
            val tags = listOf("Lisbon trip" to 9, "Work lunches" to 11, "Shared with Alex" to 5, "Gifts" to 2)
            var selected by remember { mutableStateOf(listOf("Lisbon trip")) }

            FlowBadgeSelect(
                options = tags.map { (name, count) ->
                    BadgeOption(
                        label = { Text(name) },
                        value = name,
                        leading = { IndicatorDot(color = ArecaTheme.colors.bgBrandBase, contentDescription = null) },
                        trailing = { Text("$count") },
                    )
                },
                onSelect = { option ->
                    selected = if (option.value in selected) selected - option.value else selected + option.value
                },
                selected = selected,
                selectionMode = BadgeSelectionMode.Multiple,
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                style = BadgeStyle.FilledChip,
                size = BadgeSize.Chip,
            )
        }
        recipe(
            name = "Filter chips",
            description = "BadgeStyle.Chip with BadgeSize.Chip; the chosen chip is the one not soft.",
        ) {
            var chosen by remember { mutableStateOf("All") }

            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                listOf("All", "Income", "Expense", "Transfers").forEach { filter ->
                    Badge(
                        style = BadgeStyle.Chip,
                        size = BadgeSize.Chip,
                        soft = chosen != filter,
                        onClick = { chosen = filter },
                    ) {
                        Text(filter)
                    }
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Long label",
            expectation = "Grows with the text; it has a minimum width but no maximum.",
        ) {
            Badge(style = BadgeStyle.Brand) { Text("Awaiting reviewer approval") }
        }
        case(
            name = "Single character",
            expectation = "Holds the size's minimum width rather than collapsing to the glyph.",
        ) {
            Badge(style = BadgeStyle.Brand) { Text("1") }
        }
    }
}

/**
 * Resolves the preset this option names from the current theme.
 *
 * @return The matching [BadgeStyle] preset.
 */
@Composable
private fun BadgeStyleOption.resolve(): BadgeStyle = when (this) {
    BadgeStyleOption.Default -> BadgeStyle.Default
    BadgeStyleOption.Brand -> BadgeStyle.Brand
    BadgeStyleOption.Danger -> BadgeStyle.Danger
    BadgeStyleOption.Warning -> BadgeStyle.Warning
    BadgeStyleOption.Info -> BadgeStyle.Info
    BadgeStyleOption.Success -> BadgeStyle.Success
    BadgeStyleOption.Neutral -> BadgeStyle.Neutral
    BadgeStyleOption.NeutralDark -> BadgeStyle.NeutralDark
    BadgeStyleOption.Chip -> BadgeStyle.Chip
}

/**
 * Resolves the size step this option names from the current theme.
 *
 * @return The matching [BadgeSize] step.
 */
@Composable
private fun BadgeSizeOption.resolve(): BadgeSize = when (this) {
    BadgeSizeOption.ExtraSmall -> BadgeSize.ExtraSmall
    BadgeSizeOption.Small -> BadgeSize.Small
    BadgeSizeOption.Medium -> BadgeSize.Medium
    BadgeSizeOption.Large -> BadgeSize.Large
    BadgeSizeOption.Chip -> BadgeSize.Chip
}
