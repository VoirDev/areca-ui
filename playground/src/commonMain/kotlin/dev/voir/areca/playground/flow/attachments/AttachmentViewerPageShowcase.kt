package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.attachments.viewer.AttachmentViewerPage
import dev.voir.areca.flow.attachments.viewer.AttachmentViewerStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** Showcase for [AttachmentViewerPage]. */
@Composable
fun AttachmentViewerPageShowcase() = ShowcaseScaffold(
    title = "Viewer page",
    summary = "One file in the viewer, drawn as a zoomable picture, a column of PDF pages, or stated " +
        "when it cannot be drawn.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pinch or double-tap a picture; scroll a PDF. Drawn here on the viewer's ground.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Photo,
            options = SampleFileOption.entries,
            optionLabel = { it.label },
        )
        val state by enumControl(
            key = "state",
            default = SampleStateOption.Ready,
            options = SampleStateOption.entries,
            optionLabel = { it.label },
        )
        val actions by booleanControl(
            key = "actions",
            default = true,
            description = "onRetry, onOpenElsewhere and onSave: without them the page only states.",
        )

        preview {
            val style = AttachmentViewerStyle.default()

            AttachmentViewerPage(
                item = state.applyTo(file.item),
                modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                onRetry = if (actions) ({}) else null,
                onOpenElsewhere = if (actions) ({}) else null,
                onSave = if (actions) ({}) else null,
                style = style,
            )
        }
    }

    States(
        description = "The whereabouts of the bytes decide what is under the file.",
        layout = VariantLayout.Grid,
    ) {
        SampleStateOption.entries.forEach { option ->
            variant(name = option.label) {
                val style = AttachmentViewerStyle.default()

                AttachmentViewerPage(
                    item = option.applyTo(AttachmentSamples.invoice),
                    modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                    onRetry = {},
                    style = style,
                    waitingText = "It will download once you’re back online.",
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "Document",
            expectation = "Only another application can open it: the ways to get it there.",
        ) {
            val style = AttachmentViewerStyle.default()

            AttachmentViewerPage(
                item = AttachmentSamples.contract,
                modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                onOpenElsewhere = {},
                onSave = {},
                style = style,
                elsewhereText = "This kind of file can’t be shown here.",
            )
        }
        case(
            name = "Picture that fails",
            expectation = "Stated like a document rather than drawn as a broken picture.",
        ) {
            val style = AttachmentViewerStyle.default()

            AttachmentViewerPage(
                item = SampleFileOption.BrokenPhoto.item,
                modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                onOpenElsewhere = {},
                style = style,
            )
        }
        case(
            name = "PDF the renderer cannot open",
            expectation = "No renderer for this model: offered elsewhere, like any document.",
        ) {
            val style = AttachmentViewerStyle.default()

            AttachmentViewerPage(
                item = AttachmentSamples.invoice.copy(model = SampleFile.Document),
                modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                onOpenElsewhere = {},
                style = style,
            )
        }
        case(
            name = "Nothing to offer",
            expectation = "No callbacks: the file is stated, with no buttons under it.",
        ) {
            val style = AttachmentViewerStyle.default()

            AttachmentViewerPage(
                item = AttachmentSamples.contract,
                modifier = Modifier.fillMaxWidth().height(PageHeight).background(style.bg),
                style = style,
            )
        }
    }
}

/** Height every page on this showcase is drawn at, about a phone's viewer between its bars. */
private val PageHeight = 460.dp
