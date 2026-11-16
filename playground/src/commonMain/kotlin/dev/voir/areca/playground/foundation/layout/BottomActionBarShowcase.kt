package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.BottomActionBar
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The three shapes an action bar takes. */
private enum class BottomActionBarShape {
    Action,
    Count,
    Unavailable,
}

/** Inset-free, so previews are not padded by the window the playground runs in. */
private val NoInsets = WindowInsets(0, 0, 0, 0)

private val Items = listOf(
    "Cordless drill", "Camping stove", "Projector", "Sewing machine", "Tent, 4 person",
    "Label printer", "Ladder", "Espresso machine", "Snowboard", "Toolbox",
)

/** Showcase for [BottomActionBar]. */
@Composable
fun BottomActionBarShowcase() = ShowcaseScaffold(
    title = "Bottom action bar",
    summary = "A screen's bottom bar with its primary action, a count, or the reason it is not available yet.",
    module = "foundation",
) {
    Playground(description = "Pass it as BaseLayout's bottomBar; here it stands alone.") {
        val shape by enumControl(
            key = "shape",
            default = BottomActionBarShape.Action,
            options = BottomActionBarShape.entries,
            optionLabel = { it.name },
        )
        val actionText by textControl("actionText", default = "Save")
        val loading by booleanControl("loading", default = false)
        val showDivider by booleanControl("showDivider", default = true)

        preview {
            BottomActionBar(
                actionText = actionText,
                onAction = {},
                enabled = shape != BottomActionBarShape.Unavailable,
                loading = loading,
                hint = if (shape == BottomActionBarShape.Unavailable) "Add a name to save" else null,
                showDivider = showDivider,
                windowInsets = NoInsets,
                leading = if (shape == BottomActionBarShape.Count) ({ Text("3 selected") }) else null,
            )
        }
    }

    Variants(
        title = "Shapes",
        description = "What is passed decides the shape: an action alone, a leading count, or a hint.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "One action", detail = "actionText only — the button runs across the bar") {
            BottomActionBar(actionText = "Add item", onAction = {}, windowInsets = NoInsets)
        }
        variant(name = "Count and action", detail = "leading = { Text(…) } — the button keeps its own width") {
            BottomActionBar(
                actionText = "Move",
                onAction = {},
                windowInsets = NoInsets,
                leading = { Text("3 selected") },
            )
        }
        variant(name = "Not available yet", detail = "enabled = false with a hint saying what is missing") {
            BottomActionBar(
                actionText = "Save",
                onAction = {},
                enabled = false,
                hint = "Add a name to save",
                windowInsets = NoInsets,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Default") {
            BottomActionBar(actionText = "Save", onAction = {}, windowInsets = NoInsets)
        }
        variant(name = "Loading", detail = "loading = true — the spinner replaces the caption") {
            BottomActionBar(actionText = "Save", onAction = {}, loading = true, windowInsets = NoInsets)
        }
        variant(name = "Disabled with a count", detail = "Nothing selected yet") {
            BottomActionBar(
                actionText = "Move",
                onAction = {},
                enabled = false,
                hint = "Select items to move",
                windowInsets = NoInsets,
                leading = { Text("0 selected") },
            )
        }
    }

    Recipes {
        recipe(
            name = "Selection screen",
            description = "BaseLayout pads the list by the bar's height, so the last row scrolls clear of the button.",
        ) {
            var selected by remember { mutableStateOf(setOf("Projector", "Ladder")) }
            Box(modifier = Modifier.fillMaxWidth().height(360.dp)) {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = { TopBar(title = "Move items") },
                    bottomBar = {
                        BottomActionBar(
                            actionText = "Move",
                            onAction = {},
                            enabled = selected.isNotEmpty(),
                            hint = if (selected.isEmpty()) "Select items to move" else null,
                            windowInsets = NoInsets,
                            leading = { Text("${selected.size} selected") },
                        )
                    },
                ) { padding ->
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding) {
                        items(Items, key = { it }) { item ->
                            CheckboxListItem(
                                checked = item in selected,
                                onCheckedChange = { checked ->
                                    selected = if (checked) selected + item else selected - item
                                },
                                title = { Text(item) },
                            )
                        }
                    }
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Long count",
            expectation = "The count ellipsises in the room the button leaves; the button stays whole.",
        ) {
            BottomActionBar(
                actionText = "Move to another place",
                onAction = {},
                windowInsets = NoInsets,
                leading = {
                    Text("128 items selected across 4 places", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
            )
        }
        case(
            name = "Long hint",
            expectation = "The hint wraps to two lines, then ellipsises.",
        ) {
            BottomActionBar(
                actionText = "Save",
                onAction = {},
                enabled = false,
                hint = "Add a name, a place and at least one photo before saving this item to your catalogue",
                windowInsets = NoInsets,
            )
        }
    }
}
