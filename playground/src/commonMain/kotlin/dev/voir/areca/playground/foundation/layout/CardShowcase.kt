package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardSize
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Style presets offered by [CardStyle], as a selectable list. */
private enum class CardStyleOption {
    /** [CardStyle.Default]: plain surface, tinted towards its copy when pressed. */
    Default,

    /** [CardStyle.Outlined]. */
    Outlined,

    /** [CardStyle.SoftNeutral]. */
    SoftNeutral,

    /** [CardStyle.SolidBrand]. */
    SolidBrand,

    /** [CardStyle.GhostNeutral]. */
    GhostNeutral,
}

/** Showcase for [Card]. */
@Composable
fun CardShowcase() = ShowcaseScaffold(
    title = "Card",
    summary = "Grouping surface that can be static, clickable, selectable or expandable.",
    module = "foundation",
) {
    Playground {
        val style by enumControl(
            key = "style",
            default = CardStyleOption.Default,
            options = CardStyleOption.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val active by booleanControl(
            key = "active",
            default = false,
            description = "The selected or open appearance.",
        )
        val clickable by booleanControl("clickable", default = true)

        preview {
            Card(
                style = style.resolve(),
                enabled = enabled,
                active = active,
                onClick = if (clickable) ({}) else null,
                modifier = Modifier.fillMaxWidth(),
            ) { padding ->
                CardBody(
                    padding = padding,
                    title = "Workspace settings",
                    subtitle = "Members, billing and access",
                )
            }
        }
    }

    Variants(
        title = "Styles",
        description = "Each preset carries its own background, border and indication.",
        layout = VariantLayout.Vertical,
    ) {
        for (style in CardStyleOption.entries) {
            variant(name = style.name) {
                Card(
                    style = style.resolve(),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { padding ->
                    CardBody(
                        padding = padding,
                        title = style.name,
                        subtitle = "Hover and press to see the indication",
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Enabled", detail = "CardStyle.Default, pressable") {
            Card(onClick = {}, modifier = Modifier.fillMaxWidth()) { padding ->
                CardBody(padding, "Enabled", "Press it: tints towards its copy, like a list row")
            }
        }
        variant("Active", detail = "selected or open") {
            Card(active = true, onClick = {}, modifier = Modifier.fillMaxWidth()) { padding ->
                CardBody(padding, "Active", "Uses the active background and border")
            }
        }
        variant("Disabled") {
            Card(enabled = false, onClick = {}, modifier = Modifier.fillMaxWidth()) { padding ->
                CardBody(padding, "Disabled", "Refuses input")
            }
        }
        variant("Static", detail = "onClick = null") {
            Card(style = CardStyle.Outlined, modifier = Modifier.fillMaxWidth()) { padding ->
                CardBody(padding, "Static", "No indication, no cursor change")
            }
        }
    }

    Recipes {
        recipe(
            name = "Expandable card",
            description = "The second Card overload: a header that stays and a body that animates.",
        ) {
            var expanded by remember { mutableStateOf(false) }

            Card(
                style = CardStyle.Outlined,
                expanded = expanded,
                active = expanded,
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(),
                expandedContent = { padding ->
                    Column(
                        modifier = Modifier.padding(
                            start = padding.calculateStartPadding(LocalLayoutDirection.current),
                            end = padding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = padding.calculateBottomPadding(),
                        ),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    ) {
                        Text(text = "Owner: Dana Whitfield", variant = TextVariant.BodySmall)
                        Text(text = "Created: 12 March 2025", variant = TextVariant.BodySmall)
                        Text(text = "Members: 14", variant = TextVariant.BodySmall)
                    }
                },
            ) { padding ->
                CardBody(
                    padding = padding,
                    title = "Design system",
                    subtitle = if (expanded) "Click to collapse" else "Click to expand",
                )
            }
        }
        recipe(
            name = "Selectable group",
            description = "Cards as a radio group — exactly one active at a time.",
        ) {
            var selected by remember { mutableStateOf("Monthly") }

            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                listOf("Monthly" to "$12 per seat", "Yearly" to "$120 per seat").forEach { plan ->
                    Card(
                        active = selected == plan.first,
                        onClick = { selected = plan.first },
                        modifier = Modifier.weight(1f),
                    ) { padding ->
                        CardBody(padding, title = plan.first, subtitle = plan.second)
                    }
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Long content",
            expectation = "Grows vertically; the card never clips or scrolls its own content.",
        ) {
            Card(style = CardStyle.Outlined, modifier = Modifier.fillMaxWidth()) { padding ->
                CardBody(
                    padding = padding,
                    title = "Retention policy",
                    subtitle = "Deleted items are recoverable for thirty days, after which they " +
                        "are permanently removed from backups and cannot be restored by support.",
                )
            }
        }
        case(
            name = "Pressable, no indication",
            expectation = "CardStyle.default(indication = null) still takes the press but draws no tint.",
        ) {
            Card(
                style = CardStyle.default(indication = null),
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            ) { padding ->
                CardBody(padding, "Open invoice", "Shows its press some other way, or not at all")
            }
        }
        case(
            name = "Custom padding",
            expectation = "Content padding is a parameter; the surface itself is unchanged.",
        ) {
            Card(
                style = CardStyle.Outlined,
                size = CardSize.default(padding = PaddingValues(ArecaTheme.spacing.xs)),
                modifier = Modifier.fillMaxWidth(),
            ) { padding ->
                CardBody(padding, "Tight padding", "4dp instead of the default")
            }
        }
    }
}

/**
 * The title and supporting line every card on this page carries.
 *
 * @param padding Content padding the card hands its content.
 * @param title Main line.
 * @param subtitle Supporting line under [title].
 */
@Composable
private fun CardBody(padding: PaddingValues, title: String, subtitle: String) {
    Column(
        modifier = Modifier.padding(padding),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        Text(text = title, variant = TextVariant.LabelLarge)
        Text(text = subtitle, variant = TextVariant.BodySmall, color = TextColor.Subtle)
    }
}

/**
 * Resolves the preset this option names from the current theme.
 *
 * @return The matching [CardStyle] preset.
 */
@Composable
private fun CardStyleOption.resolve(): CardStyle = when (this) {
    CardStyleOption.Default -> CardStyle.Default
    CardStyleOption.Outlined -> CardStyle.Outlined
    CardStyleOption.SoftNeutral -> CardStyle.SoftNeutral
    CardStyleOption.SolidBrand -> CardStyle.SolidBrand
    CardStyleOption.GhostNeutral -> CardStyle.GhostNeutral
}
