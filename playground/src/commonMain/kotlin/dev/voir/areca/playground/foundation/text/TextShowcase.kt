package dev.voir.areca.playground.foundation.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Heading
import dev.voir.areca.foundation.text.HeadingSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Heading sizes, as a selectable list. */
private enum class HeadingSizeOption { ExtraSmall, Small, Medium, Large, ExtraLarge }

/**
 * Showcase for [Text] and [Heading].
 *
 * One page: `Heading` is the composed form — a title with optional supportive copy and content —
 * and both resolve through the same typography tokens.
 */
@Composable
fun TextShowcase() = ShowcaseScaffold(
    title = "Text",
    summary = "Typography primitives: named text sizes and colors, and the composed heading block.",
    module = "foundation",
) {
    Playground {
        val content by textControl("text", default = "The quick brown fox jumps over the lazy dog")
        val size by enumControl(
            key = "size",
            default = TextVariant.BodyMedium,
            options = TextVariant.entries,
            optionLabel = { it.name },
        )
        val color by enumControl(
            key = "color",
            default = TextColor.Base,
            options = TextColor.entries,
            optionLabel = { it.name },
        )

        preview {
            Text(text = content, variant = size, color = color)
        }
    }

    Variants(
        title = "Sizes",
        description = "Every named step, in the order the scale runs.",
        layout = VariantLayout.Vertical,
    ) {
        for (variant in TextVariant.entries) {
            variant(name = variant.name) {
                Text(text = "The quick brown fox", variant = variant)
            }
        }
    }

    Variants(
        title = "Colors",
        description = "Semantic foregrounds. OnContrast and OnFill are shown on their own grounds.",
        layout = VariantLayout.Grid,
    ) {
        for (color in TextColor.entries) {
            variant(name = color.name) {
                Text(text = color.name, variant = TextVariant.BodyMedium, color = color)
            }
        }
    }

    Anatomy(
        title = "Heading anatomy",
        description = "A heading is a title plus two optional parts.",
    ) {
        slot(name = "title", description = "The heading itself. Required.") {
            Heading(title = { Text("Billing") })
        }
        slot(
            name = "supportive",
            description = "A sentence under the title, in a quieter style.",
        ) {
            Heading(
                title = { Text("Billing") },
                supportive = { Text("Manage your plan, payment method and invoices.") },
            )
        }
        slot(
            name = "content",
            description = "Trailing content on the heading row — usually an action.",
        ) {
            Heading(
                title = { Text("Billing") },
                supportive = { Text("Manage your plan and invoices.") },
                content = { Text(text = "Change plan", variant = TextVariant.LabelMedium, color = TextColor.Brand) },
            )
        }
    }

    Variants(
        title = "Heading sizes",
        description = "Each step scales the title and the supportive copy together.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in HeadingSizeOption.entries) {
            variant(name = option.name) {
                Heading(
                    size = option.resolve(),
                    title = { Text("Section title") },
                    supportive = { Text("Supporting copy at the matching step.") },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Long text wrapping",
            expectation = "Wraps at the container width; line height comes from the size token.",
        ) {
            Text(
                text = "Deleted items are recoverable for thirty days, after which they are " +
                    "permanently removed from backups and cannot be restored by support.",
                variant = TextVariant.BodyMedium,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Truncation",
            expectation = "maxLines with ellipsis overflow cuts rather than wrapping.",
        ) {
            Text(
                text = "A single line that is far too long for the space it has been given",
                variant = TextVariant.BodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(240.dp),
            )
        }
        case(
            name = "Size against color",
            expectation = "Size and color are independent tokens; neither implies the other.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Text("Heading size, danger color", variant = TextVariant.HeadingSmall, color = TextColor.Danger)
                Text("Caption size, brand color", variant = TextVariant.Caption, color = TextColor.Brand)
            }
        }
    }
}

@Composable
private fun HeadingSizeOption.resolve(): HeadingSize = when (this) {
    HeadingSizeOption.ExtraSmall -> HeadingSize.ExtraSmall
    HeadingSizeOption.Small -> HeadingSize.Small
    HeadingSizeOption.Medium -> HeadingSize.Medium
    HeadingSizeOption.Large -> HeadingSize.Large
    HeadingSizeOption.ExtraLarge -> HeadingSize.ExtraLarge
}
