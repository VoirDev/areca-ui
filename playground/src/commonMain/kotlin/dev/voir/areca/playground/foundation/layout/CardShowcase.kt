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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardFooterStyle
import dev.voir.areca.foundation.layout.CardSize
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.ArrowLeftRight
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

/** Footer presets offered by [CardFooterStyle], as a selectable list. */
private enum class CardFooterOption {
    /** No footer. */
    None,

    /** [CardFooterStyle.plain]: no fill, a hairline above. */
    Plain,

    /** [CardFooterStyle.soft]: the family's muted tint. */
    Soft,

    /** [CardFooterStyle.solid]: the family's base fill. */
    Solid,
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
        val footer by enumControl(
            key = "footer",
            default = CardFooterOption.None,
            options = CardFooterOption.entries,
            optionLabel = { it.name },
        )
        val footerColor by enumControl(
            key = "footerColor",
            default = ActionColor.Brand,
            options = ActionColor.entries,
            optionLabel = { it.name },
            description = "Color family of a soft or solid footer.",
        )

        preview {
            Card(
                style = style.resolve(),
                enabled = enabled,
                active = active,
                onClick = if (clickable) ({}) else null,
                modifier = Modifier.fillMaxWidth(),
                footerStyle = footer.resolve(footerColor),
                footer = if (footer == CardFooterOption.None) {
                    null
                } else {
                    {
                        Text("Renews on 12 November", modifier = Modifier.weight(1f))
                        Button(onClick = {}) { Text("Manage") }
                    }
                },
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

    Variants(
        title = "Footers",
        description = "A strip along the bottom for the card's status and actions. Copy and " +
            "buttons in it take the footer's ink, so a filled footer reads without restyling.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Plain", detail = "CardFooterStyle.plain()") {
            FooterSample(CardFooterStyle.plain(), "Updated 2 minutes ago", action = "Refresh")
        }
        for (color in ActionColor.entries.filter { it != ActionColor.NeutralDark }) {
            variant(name = "Soft / ${color.name}", detail = "CardFooterStyle.soft(${color.name})") {
                FooterSample(CardFooterStyle.soft(color), color.footerStatus(), action = "Review")
            }
        }
        for (color in listOf(ActionColor.Brand, ActionColor.Danger, ActionColor.NeutralDark)) {
            variant(name = "Solid / ${color.name}", detail = "CardFooterStyle.solid(${color.name})") {
                FooterSample(CardFooterStyle.solid(color), color.footerStatus(), action = "Review")
            }
        }
        variant(name = "Custom", detail = "CardFooterStyle.of(bg, content)") {
            FooterSample(
                style = CardFooterStyle.of(
                    bg = ArecaTheme.colors.bgInfoMuted,
                    content = ArecaTheme.colors.fgInfoText,
                ),
                status = "New export format available",
                action = "Learn more",
            )
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
            name = "Subscription card",
            description = "The plan in the body; its renewal and the way to change it in the footer.",
        ) {
            Card(
                style = CardStyle.Outlined,
                modifier = Modifier.fillMaxWidth(),
                footerStyle = CardFooterStyle.soft(ActionColor.Brand),
                footer = {
                    Text("Renews on 12 November", modifier = Modifier.weight(1f))
                    Button(onClick = {}) { Text("Manage") }
                },
            ) { padding ->
                CardBody(padding, title = "Team plan", subtitle = "$12 per seat a month · 14 seats")
            }
        }
        recipe(
            name = "Failed payment",
            description = "The danger footer carries the problem and the one action that fixes it.",
        ) {
            Card(
                style = CardStyle.Outlined,
                modifier = Modifier.fillMaxWidth(),
                footerStyle = CardFooterStyle.soft(ActionColor.Danger),
                footer = {
                    Text("Payment failed on 3 November", modifier = Modifier.weight(1f))
                    Button(onClick = {}) { Text("Update card") }
                },
            ) { padding ->
                CardBody(padding, title = "Team plan", subtitle = "Visa ending 4242")
            }
        }
        recipe(
            name = "Upgrade offer",
            description = "A solid brand footer is the card's call to action — one per screen.",
        ) {
            Card(
                style = CardStyle.Outlined,
                modifier = Modifier.fillMaxWidth(),
                footerStyle = CardFooterStyle.solid(ActionColor.Brand),
                footer = {
                    Text("Two months free on yearly billing", modifier = Modifier.weight(1f))
                    Button(onClick = {}) { Text("Upgrade") }
                },
            ) { padding ->
                CardBody(padding, title = "Free plan", subtitle = "3 of 3 projects used")
            }
        }
        recipe(
            name = "Comparison banner",
            description = "A soft card that is one sentence and a way in: icon, the comparison, chevron.",
        ) {
            Card(
                style = CardStyle.softNeutral(),
                size = CardSize.default(padding = PaddingValues(ArecaTheme.spacing.lg)),
                onClick = {},
                onClickLabel = "Compare periods",
                modifier = Modifier.fillMaxWidth(),
            ) { padding ->
                Row(
                    modifier = Modifier.padding(padding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    Icon(ArecaIcons.ArrowLeftRight, contentDescription = null, size = IconSize.Small)
                    Text(
                        "€218 less than September by the 23rd",
                        variant = TextVariant.BodySmall,
                        modifier = Modifier.weight(1f),
                    )
                    Chevron(direction = ChevronDirection.Right, color = ArecaTheme.colors.fgSubtle)
                }
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
 * A card with a body and a footer of [style], as the footer variants draw it.
 *
 * @param style Footer colors.
 * @param status Line of status at the start of the footer.
 * @param action Label of the footer's button.
 */
@Composable
private fun FooterSample(style: CardFooterStyle, status: String, action: String) {
    Card(
        style = CardStyle.Outlined,
        modifier = Modifier.fillMaxWidth(),
        footerStyle = style,
        footer = {
            Text(status, modifier = Modifier.weight(1f))
            Button(onClick = {}) { Text(action) }
        },
    ) { padding ->
        CardBody(padding, title = "Team plan", subtitle = "$12 per seat a month")
    }
}

/**
 * A line of footer status that suits this color family.
 *
 * @return Realistic status copy for a footer of this color.
 */
private fun ActionColor.footerStatus(): String = when (this) {
    ActionColor.Brand -> "Renews on 12 November"
    ActionColor.Danger -> "Payment failed on 3 November"
    ActionColor.Success -> "Payment received"
    ActionColor.Warning -> "Trial ends in 3 days"
    ActionColor.Neutral -> "Billed to the workspace"
    ActionColor.NeutralDark -> "Your plan changes on 1 December"
}

/**
 * Resolves the footer preset this option names, in [color] where the preset takes one.
 *
 * @param color Color family of a soft or solid footer.
 * @return The matching [CardFooterStyle].
 */
@Composable
private fun CardFooterOption.resolve(color: ActionColor): CardFooterStyle = when (this) {
    CardFooterOption.None, CardFooterOption.Plain -> CardFooterStyle.plain()
    CardFooterOption.Soft -> CardFooterStyle.soft(color)
    CardFooterOption.Solid -> CardFooterStyle.solid(color)
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
