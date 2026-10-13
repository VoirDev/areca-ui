package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalSize
import dev.voir.areca.foundation.overlay.FlowModalStyle
import dev.voir.areca.foundation.overlay.LocalFlowModalSize
import dev.voir.areca.foundation.overlay.LocalFlowModalStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * A sheet for choosing from a list, for when [SelectField]'s dropdown is not enough room.
 *
 * Selection is kept as a draft inside the modal. Every toggle updates that draft, is reported
 * through [onChange], and is handed to [footer] — so a caller can either commit each change as it
 * happens (act on [onChange], no footer) or wait for a decision (ignore [onChange], apply the
 * draft from a footer button). Once the sheet has fully closed, and whenever [values] change from
 * outside, the draft is reset to what the caller holds; a dismissed sheet therefore abandons its
 * draft rather than quietly keeping it for next time, and every opening starts from [values].
 *
 * Searching is opt-in and the modal does none of it: pass [onQueryChange] to get a search field,
 * then filter or fetch and hand back the matching [options]. Set [SelectFlowModalState.isSearching]
 * while results are outstanding and [loadingContent] takes over the body.
 *
 * @param Value Type of the values being selected.
 * @param controller Controller that shows and hides the sheet.
 * @param values Values selected outside the modal. Changing them resets the draft.
 * @param options Options to offer, in display order. Already filtered by the caller when
 *   searching.
 * @param modifier Modifier applied to the sheet.
 * @param onChange Called with the new draft every time an option is toggled. Ignore it when the
 *   selection should only take effect from [footer].
 * @param state Search state. Remembered per call site by [rememberSelectFlowModalState].
 * @param selectionMode Whether one value or many can be held at once.
 * @param style Colors and shape for the sheet. Defaults to [LocalFlowModalStyle], then
 *   [FlowModalStyle.Default].
 * @param size Height and inset behavior for the sheet. Defaults to [LocalFlowModalSize], then
 *   [FlowModalSize.Default].
 * @param header Fixed content above the list, typically a
 *   [dev.voir.areca.foundation.topbar.TopBar] with a title and a close control.
 * @param footer Fixed content below the list. Receives the current draft, which is what an
 *   apply button should commit.
 * @param onQueryChange Called with the new query as it is typed. Passing null leaves the sheet
 *   without a search field.
 * @param searchPlaceholder Hint shown in the search field while the query is empty.
 * @param emptyContent Body shown when [options] is empty, given the current query so it can say
 *   whether nothing matched or there was nothing to begin with. Null renders nothing.
 * @param loadingContent Body shown while [SelectFlowModalState.isSearching] is set. Null keeps
 *   the previous options on screen instead.
 * @param content Renders the option list. Receives the options, the draft selection, and a
 *   callback to invoke with the option the user acted on.
 */
@Composable
public fun <Value> SelectFlowModal(
    controller: FlowModalController,
    values: Set<Value>,
    options: List<SelectOption<Value>>,
    modifier: Modifier = Modifier,
    onChange: (Set<Value>) -> Unit = {},
    state: SelectFlowModalState = rememberSelectFlowModalState(),
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable (draftValues: Set<Value>) -> Unit)? = null,
    onQueryChange: ((String) -> Unit)? = null,
    searchPlaceholder: String = "Search",
    emptyContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.EmptyContent(query = query)
    },
    loadingContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.LoadingContent(query = query)
    },
    content: @Composable (
        options: List<SelectOption<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
    ) -> Unit = { modalOptions, selectedValues, onSelect ->
        SelectFlowModalDefaults.List(
            options = modalOptions,
            values = selectedValues,
            onSelect = onSelect,
        )
    },
) {
    // Keyed on the caller's values so a selection changed from outside replaces the draft in the
    // same composition, rather than one frame after the sheet has drawn the stale one.
    val draft = remember(values) { SelectFlowModalDraft(values) }
    val draftValues = draft.values

    // The modal stays composed while the controller hides and shows the sheet, so closing is
    // watched explicitly: once the sheet is fully hidden, unapplied edits are dropped and the next
    // opening starts from the caller's values again.
    LaunchedEffect(controller, draft) {
        snapshotFlow { controller.isVisible }.collect { visible ->
            if (!visible) draft.discard()
        }
    }

    FlowModal(
        controller = controller,
        modifier = modifier,
        header = header,
        style = style,
        size = size,
        footer = { footer?.invoke(draftValues) },
    ) {
        Column {
            if (onQueryChange != null) {
                SelectFlowModalDefaults.Search(
                    value = state.query,
                    placeholder = searchPlaceholder,
                    onValueChange = { query ->
                        state.query = query
                        onQueryChange(query)
                    },
                )

                Spacer(Modifier.height(ArecaTheme.spacing.md))
            }

            when {
                state.isSearching && loadingContent != null -> {
                    loadingContent(state.query)
                }

                options.isEmpty() && emptyContent != null -> {
                    emptyContent(state.query)
                }

                else -> {
                    content(options, draftValues) { option ->
                        onChange(draft.select(option, selectionMode))
                    }
                }
            }
        }
    }
}
