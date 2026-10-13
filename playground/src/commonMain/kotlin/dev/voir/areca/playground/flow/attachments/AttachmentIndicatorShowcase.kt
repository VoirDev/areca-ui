package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.attachments.part.AttachmentIndicator
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentIndicator]. */
@Composable
fun AttachmentIndicatorShowcase() = ShowcaseScaffold(
    title = "Indicator",
    summary = "A paperclip, and optionally how many, on something that has files attached.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "It takes the colour and text style of the line it sits in.") {
        val count by textControl(
            key = "count",
            default = "3",
            description = "Empty draws the paperclip alone.",
        )
        val description by textControl(
            key = "contentDescription",
            default = "3 attachments",
            description = "Read out in place of the glyph and the count.",
        )

        preview {
            AttachmentIndicator(count = count.ifEmpty { null }, contentDescription = description)
        }
    }

    Sizes(layout = VariantLayout.Horizontal) {
        variant(name = "ExtraSmall", detail = "12dp, the default") { AttachmentIndicator(count = "3") }
        variant(name = "Small", detail = "16dp") { AttachmentIndicator(count = "3", size = IconSize.Small) }
    }

    Recipes {
        recipe(
            name = "Activity row",
            description = "Ahead of the subtitle, so the title and the amount stay the pair the list is read by.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Grocery store", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        subtitle = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AttachmentIndicator(contentDescription = "Has attachments")
                                Text("Food · 14:32", maxLines = 1)
                            }
                        },
                        value = { Text("−42.80 €") },
                    )
                    SettingsRow(
                        title = { Text("Train tickets", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        subtitle = { Text("Travel · 09:05", maxLines = 1) },
                        value = { Text("−118.00 €") },
                    )
                }
            }
        }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "Large count",
            expectation = "The count as worded; the paperclip never scales with it.",
        ) {
            AttachmentIndicator(count = "20+", contentDescription = "More than 20 attachments")
        }
        case(
            name = "Muted line",
            expectation = "Coloured by the line around it unless a colour is passed.",
        ) {
            AttachmentIndicator(count = "2", color = ArecaTheme.colors.fgMuted)
        }
    }
}
