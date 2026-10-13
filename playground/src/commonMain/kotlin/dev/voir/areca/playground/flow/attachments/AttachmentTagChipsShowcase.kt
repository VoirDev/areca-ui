package dev.voir.areca.playground.flow.attachments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentTagChips]. */
@Composable
fun AttachmentTagChipsShowcase() = ShowcaseScaffold(
    title = "Tag chips",
    summary = "What a file is, chosen in one tap from every tag the product has.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press a chip; the tags are the product's own enum, worded by a function.") {
        val enabled by booleanControl("enabled", default = true)
        val preselected by booleanControl(
            key = "selected",
            default = false,
            description = "Start with a tag chosen, as on the options sheet; asking afresh starts with none.",
        )

        preview {
            var tag by remember(preselected) { mutableStateOf(SampleTag.Receipt.takeIf { preselected }) }

            AttachmentTagChips(
                tags = SampleTag.entries,
                selected = tag,
                onSelect = { chosen -> tag = chosen },
                label = { it.label },
                enabled = enabled,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "None chosen", detail = "selected = null") {
            AttachmentTagChips(tags = SampleTag.entries, selected = null, onSelect = {}, label = { it.label })
        }
        variant(name = "One chosen", detail = "The dark chip") {
            AttachmentTagChips(tags = SampleTag.entries, selected = SampleTag.Invoice, onSelect = {}, label = { it.label })
        }
        variant(name = "Disabled", detail = "enabled = false") {
            AttachmentTagChips(
                tags = SampleTag.entries,
                selected = SampleTag.Invoice,
                onSelect = {},
                label = { it.label },
                enabled = false,
            )
        }
    }

    EdgeCases(layout = VariantLayout.Vertical) {
        case(
            name = "Strings as tags",
            expectation = "Any type works; here a product's folder names.",
        ) {
            AttachmentTagChips(
                tags = listOf("Taxes 2026", "Car", "Home", "Health"),
                selected = "Car",
                onSelect = {},
                label = { it },
            )
        }
        case(
            name = "Grey sheet",
            expectation = "On a grey sheet the chips need the surface colour to read as chips.",
        ) {
            AttachmentTagChips(
                tags = SampleTag.entries,
                selected = null,
                onSelect = {},
                label = { it.label },
                bg = ArecaTheme.colors.bgSurface,
            )
        }
    }
}
