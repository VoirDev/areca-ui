package dev.voir.areca.playground.foundation.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.list.LazyList
import dev.voir.areca.foundation.list.List
import dev.voir.areca.foundation.list.ListEntry
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.list.ListItemColor
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.ListSize
import dev.voir.areca.foundation.list.ListStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

private val Countries = listOf("Germany", "France", "Japan", "Australia", "Canada")

/**
 * Showcase for [List] and [LazyList].
 *
 * One page: the same `ListEntry` model, laid out eagerly and lazily. Both compose `ListItem` rows.
 */
@Composable
fun ListShowcase() = ShowcaseScaffold(
    title = "List",
    summary = "Rows and dividers declared as data, laid out eagerly or lazily.",
    module = "foundation",
) {
    Playground {
        val lazyRows by booleanControl(
            key = "lazy",
            default = false,
            description = "LazyList only composes what is on screen.",
        )
        val separated by booleanControl(
            key = "separated",
            default = true,
            description = "The list draws a hairline at every boundary between two rows.",
        )
        val grouped by booleanControl(
            key = "grouped",
            default = true,
            label = "card surface",
            description = "The list owns the surface; the first and last row take its corners.",
        )
        val withTrailing by booleanControl("trailing chevrons", default = true)

        preview {
            val entries = countryEntries(withTrailing)

            if (lazyRows) {
                LazyList(
                    entries = entries,
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    separated = separated,
                )
            } else {
                List(
                    entries = entries,
                    modifier = Modifier.fillMaxWidth(),
                    separated = separated,
                    listStyle = ListStyle.default(
                        bg = if (grouped) {
                            ArecaTheme.colors.bgSurface
                        } else {
                            Color.Unspecified
                        },
                        border = if (grouped) {
                            ArecaTheme.colors.borderBase
                        } else {
                            Color.Unspecified
                        },
                    ),
                    listSize = ListSize.default(
                        shape = if (grouped) ArecaTheme.shapes.md else RectangleShape,
                        borderWidth = if (grouped) 1.dp else 0.dp,
                        separatorInset = if (grouped) ArecaTheme.spacing.xl else 0.dp,
                    ),
                )
            }
        }
    }

    Anatomy(description = "A list is a flat sequence of entries, of which there are two kinds.") {
        slot(
            name = "ListEntry.Item",
            description = "One row, carrying the arguments ListItem would have been called with.",
        ) {
            List(modifier = Modifier.fillMaxWidth(), separated = true) {
                item(key = "a", title = { Text("Plain row") })
                item(
                    key = "b",
                    title = { Text("Clickable row") },
                    onClick = {},
                    trailing = { ListItemChevron() },
                )
            }
        }
        slot(
            name = "ListEntry.Divider",
            description = "A meaningful break, optionally labelled — not the hairline between " +
                "two rows, which the list draws itself.",
        ) {
            List(modifier = Modifier.fillMaxWidth()) {
                item(key = "a", title = { Text("Sent") })
                divider(label = "Earlier")
                item(key = "b", title = { Text("Archived") })
            }
        }
    }

    Variants(
        title = "Per-row overrides",
        description = "An entry can carry its own style, so one row can differ from its neighbours.",
        layout = VariantLayout.Vertical,
    ) {
        variant("One destructive row") {
            val dangerStyle = ListItemStyle.plain(ListItemColor.Danger)

            List(
                modifier = Modifier.fillMaxWidth(),
                separated = true,
                listStyle = ListStyle.default(bg = ArecaTheme.colors.bgSurface),
                listSize = ListSize.default(shape = ArecaTheme.shapes.md),
            ) {
                item(key = "rename", title = { Text("Rename workspace") }, onClick = {})
                item(key = "duplicate", title = { Text("Duplicate workspace") }, onClick = {})
                item(
                    key = "delete",
                    title = { Text("Delete workspace") },
                    onClick = {},
                    style = dangerStyle,
                )
            }
        }
        variant("Mixed states") {
            List(modifier = Modifier.fillMaxWidth(), separated = true) {
                item(key = "a", title = { Text("Resting") }, onClick = {})
                item(key = "b", title = { Text("Selected") }, selected = true, onClick = {})
                item(key = "c", title = { Text("Disabled") }, enabled = false, onClick = {})
            }
        }
    }

    EdgeCases {
        case(
            name = "Selection across rows",
            expectation = "Selection is the caller's state; the list only renders the flags.",
        ) {
            var selected by remember { mutableStateOf(Countries.first()) }

            List(modifier = Modifier.fillMaxWidth(), separated = true) {
                Countries.forEach { country ->
                    item(
                        key = country,
                        title = { Text(country) },
                        selected = selected == country,
                        onClick = { selected = country },
                    )
                }
            }
        }
        case(
            name = "Disabled list",
            expectation = "Every row is disabled, whatever its own flag says.",
        ) {
            List(modifier = Modifier.fillMaxWidth(), separated = true, enabled = false) {
                item(key = "a", title = { Text("Says enabled") }, onClick = {})
                item(key = "b", title = { Text("Says enabled too") }, onClick = {})
            }
        }
        case(
            name = "Empty list",
            expectation = "Renders nothing at all; the empty state belongs to the caller.",
        ) {
            List(entries = emptyList(), modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Many rows in a LazyList",
            expectation = "Only the visible rows compose; the first row draws no line above it.",
        ) {
            LazyList(
                entries = (1..200).map { index ->
                    ListEntry.Item(key = index, title = { Text("Row $index") })
                },
                modifier = Modifier.fillMaxWidth().height(240.dp),
                separated = true,
            )
        }
    }
}

/**
 * Builds the playground entries.
 *
 * @param withTrailing Whether each row carries a trailing chevron.
 * @return One row per country.
 */
private fun countryEntries(withTrailing: Boolean): kotlin.collections.List<ListEntry> =
    Countries.map { country ->
        ListEntry.Item(
            key = country,
            title = { Text(country) },
            onClick = {},
            trailing = if (withTrailing) ({ ListItemChevron() }) else null,
        )
    }
