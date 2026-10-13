package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.row.AttachmentRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** What the playground row reads as its first line. */
private enum class RowTitle(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The word its owner chose. */
    Label("label"),

    /** The file's own name. */
    FileName("fileName"),
}

/**
 * The line under a sample file's title: its name when the title is not, and its size.
 *
 * @receiver The file.
 * @param title What the title shows.
 * @return The line.
 */
private fun AttachmentItem.sampleSubtitle(title: RowTitle): String = listOfNotNull(
    fileName.takeIf { title == RowTitle.Label },
    size,
).joinToString(" · ")

/** Showcase for [AttachmentRow]. */
@Composable
fun AttachmentRowShowcase() = ShowcaseScaffold(
    title = "Row",
    summary = "One file as a settings-list row, for places where files are read by name rather " +
        "than recognised by sight.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press the row; a file still being added shows the list's spinner and ignores presses.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Pdf,
            options = SampleFileOption.entries,
            optionLabel = { it.label },
        )
        val state by enumControl(
            key = "state",
            default = SampleStateOption.Ready,
            options = SampleStateOption.entries,
            optionLabel = { it.label },
        )
        val title by enumControl(
            key = "title",
            default = RowTitle.Label,
            options = RowTitle.entries,
            optionLabel = { it.label },
        )
        val subtitle by booleanControl("subtitle", default = true)
        val pressable by booleanControl("onClick", default = true)
        val enabled by booleanControl("enabled", default = true)

        preview {
            var presses by remember { mutableIntStateOf(0) }
            val item = state.applyTo(file.item)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        AttachmentRow(
                            item = item,
                            onClick = if (pressable) ({ presses++ }) else null,
                            title = if (title == RowTitle.Label) item.label else item.fileName,
                            subtitle = item.sampleSubtitle(title).takeIf { subtitle },
                            enabled = enabled,
                        )
                    }
                }

                Text(
                    text = "Pressed $presses times",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    States(
        description = "Where the bytes are is said at the row's end; the square carries no marks.",
        layout = VariantLayout.Vertical,
    ) {
        SampleStateOption.entries.forEach { option ->
            variant(name = option.label) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        val item = option.applyTo(AttachmentSamples.receipt)
                        AttachmentRow(item = item, onClick = {}, subtitle = item.sampleSubtitle(RowTitle.Label))
                    }
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Documents on a record",
            description = "A section of files read by name, beside the record's other sections.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Documents") }) {
                    (AttachmentSamples.expenseFiles + AttachmentSamples.statement).forEach { item ->
                        AttachmentRow(
                            item = item,
                            onClick = {},
                            title = item.fileName,
                            subtitle = item.sampleSubtitle(RowTitle.FileName).let { size -> "${item.label} · $size" },
                        )
                    }
                }
            }
        }
    }

    EdgeCases(layout = VariantLayout.Vertical) {
        case(
            name = "Very long file name",
            expectation = "Shortened in the middle, so the extension stays readable.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AttachmentRow(
                        item = AttachmentSamples.statement.copy(
                            fileName = "Statement for the joint current account, September 2026, final copy.pdf",
                        ),
                        onClick = {},
                        title = "Statement for the joint current account, September 2026, final copy.pdf",
                        subtitle = AttachmentSamples.statement.size,
                    )
                }
            }
        }
        case(
            name = "Title only",
            expectation = "No subtitle: the row keeps its height and centres the title.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section { AttachmentRow(item = AttachmentSamples.contract, onClick = null) }
            }
        }
    }
}
