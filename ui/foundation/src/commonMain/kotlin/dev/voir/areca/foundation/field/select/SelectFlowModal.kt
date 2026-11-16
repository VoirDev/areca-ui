package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalSize
import dev.voir.areca.foundation.overlay.FlowModalStyle
import dev.voir.areca.foundation.overlay.LocalFlowModalSize
import dev.voir.areca.foundation.overlay.LocalFlowModalStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.foundation.overlay.FlowModalTopBar

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
 * To group the options under titles — "Yours", "Popular", "All" by letter — and offer to add what
 * was typed, use the overload that takes [SelectOptionSection]s.
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
 * @param labels Copy of the search field and the stock loading and empty faces. Defaults to
 *   [LocalSelectFlowModalLabels], then [SelectFlowModalLabels.English]: provide the app's once.
 * @param title The sheet's title. With no [header], it is drawn as a
 *   [dev.voir.areca.foundation.overlay.FlowModalTopBar] with a close cross.
 * @param header Fixed content above the list, in place of the one built from [title].
 * @param headerBottom Content under the title in the stock header, such as a pill naming the
 *   region a catalogue is drawn from. It sits in the header's `bottom` slot, so it stays fixed while
 *   the list scrolls and the stock close — which clears focus and hides the keyboard before the
 *   sheet — is kept. Without it, an app that wants a line under the title has to rebuild the header
 *   and that close. Ignored when [header] is passed or [title] is `null`.
 * @param footer Fixed content below the list. Receives the current draft, which is what an
 *   apply button should commit.
 * @param closeOnSelect Whether choosing an option closes the sheet. By default a single choice
 *   with no footer closes on the pick — the language, the theme.
 * @param clearable Whether pressing the chosen option of a single choice clears it. By default a
 *   single choice is required: pressing the chosen option keeps it (and closes the sheet when
 *   [closeOnSelect]). Multiple choices always toggle.
 * @param onQueryChange Called with the new query as it is typed. Passing null leaves the sheet
 *   without a search field.
 * @param searchPlaceholder Hint shown in the search field while the query is empty. Defaults to
 *   [SelectFlowModalLabels.search].
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
    labels: SelectFlowModalLabels = LocalSelectFlowModalLabels.current ?: SelectFlowModalLabels.English,
    title: String? = null,
    header: (@Composable () -> Unit)? = null,
    headerBottom: (@Composable () -> Unit)? = null,
    footer: (@Composable (draftValues: Set<Value>) -> Unit)? = null,
    closeOnSelect: Boolean = selectionMode == SelectSelectionMode.Single && footer == null,
    clearable: Boolean = false,
    onQueryChange: ((String) -> Unit)? = null,
    searchPlaceholder: String = labels.search,
    emptyContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.EmptyContent(query = query, labels = labels)
    },
    loadingContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.LoadingContent(query = query, labels = labels)
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
    SelectFlowModalFrame(
        controller = controller,
        values = values,
        modifier = modifier,
        onChange = onChange,
        state = state,
        selectionMode = selectionMode,
        style = style,
        size = size,
        title = title,
        header = header,
        headerBottom = headerBottom,
        footer = footer,
        closeOnSelect = closeOnSelect,
        clearable = clearable,
        onQueryChange = onQueryChange,
        searchPlaceholder = searchPlaceholder,
        isEmpty = options.isEmpty(),
        emptyContent = emptyContent,
        loadingContent = loadingContent,
    ) { draftValues, onSelect ->
        content(options, draftValues, onSelect)
    }
}

/**
 * A [SelectFlowModal] whose options are grouped into titled sections — "Yours", "Popular",
 * "Similar", "All" — with an optional last row that adds what was typed as an option of the user's
 * own.
 *
 * Everything the flat overload says about the draft, [onChange], [footer] and searching holds here:
 * the modal filters nothing, so hand back sections that already match [SelectFlowModalState.query].
 * A section with no options is not drawn, so a search can empty "Popular" and keep "All".
 *
 * **Adding your own.** Pass [onCreateOption] and, whenever the trimmed query is not blank, a last
 * row captioned [SelectFlowModalLabels.createOption] offers to add it. The row is shown even when
 * nothing matched — that is when it is wanted most — instead of [emptyContent]. Pressing it hands
 * over the query and does nothing else: add the option, select it through [values] if it should be
 * selected, and close the sheet if that is the flow.
 *
 * Works the same under either [selectionMode], with or without search.
 *
 * @param Value Type of the values being selected.
 * @param controller Controller that shows and hides the sheet.
 * @param values Values selected outside the modal. Changing them resets the draft.
 * @param sections Sections to offer, in display order. Already filtered by the caller when
 *   searching. One option may sit in several sections and is marked chosen in each.
 * @param modifier Modifier applied to the sheet.
 * @param onChange Called with the new draft every time an option is toggled.
 * @param state Search state. Remembered per call site by [rememberSelectFlowModalState].
 * @param selectionMode Whether one value or many can be held at once.
 * @param style Colors and shape for the sheet. Defaults to [LocalFlowModalStyle], then
 *   [FlowModalStyle.Default].
 * @param size Height and inset behavior for the sheet. Defaults to [LocalFlowModalSize], then
 *   [FlowModalSize.Default].
 * @param labels Copy of the search field, the stock faces, the letter index and the create row.
 *   Defaults to [LocalSelectFlowModalLabels], then [SelectFlowModalLabels.English].
 * @param title The sheet's title. With no [header], it is drawn as a
 *   [dev.voir.areca.foundation.overlay.FlowModalTopBar] with a close cross.
 * @param header Fixed content above the list, in place of the one built from [title].
 * @param headerBottom Content under the title in the stock header; see the flat overload.
 * @param footer Fixed content below the list. Receives the current draft.
 * @param closeOnSelect Whether choosing an option closes the sheet; see the flat overload.
 * @param clearable Whether pressing the chosen option of a single choice clears it; see the flat
 *   overload.
 * @param onQueryChange Called with the new query as it is typed. Passing null leaves the sheet
 *   without a search field, and so without a create row.
 * @param searchPlaceholder Hint shown in the search field while the query is empty. Defaults to
 *   [SelectFlowModalLabels.search].
 * @param onCreateOption Called with the trimmed query when the create row is pressed. Passing null
 *   draws no create row.
 * @param emptyContent Body shown when every section is empty and no create row is offered, given the
 *   current query. Null renders nothing.
 * @param loadingContent Body shown while [SelectFlowModalState.isSearching] is set. Null keeps the
 *   previous sections on screen instead.
 * @param content Renders the sections. Receives the sections, the draft selection, a callback to
 *   invoke with the option the user acted on, and the create action — `null` while no create row is
 *   due — already bound to the query.
 */
@Composable
public fun <Value> SelectFlowModal(
    controller: FlowModalController,
    values: Set<Value>,
    sections: List<SelectOptionSection<Value>>,
    modifier: Modifier = Modifier,
    onChange: (Set<Value>) -> Unit = {},
    state: SelectFlowModalState = rememberSelectFlowModalState(),
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    labels: SelectFlowModalLabels = LocalSelectFlowModalLabels.current ?: SelectFlowModalLabels.English,
    title: String? = null,
    header: (@Composable () -> Unit)? = null,
    headerBottom: (@Composable () -> Unit)? = null,
    footer: (@Composable (draftValues: Set<Value>) -> Unit)? = null,
    closeOnSelect: Boolean = selectionMode == SelectSelectionMode.Single && footer == null,
    clearable: Boolean = false,
    onQueryChange: ((String) -> Unit)? = null,
    searchPlaceholder: String = labels.search,
    onCreateOption: ((query: String) -> Unit)? = null,
    emptyContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.EmptyContent(query = query, labels = labels)
    },
    loadingContent: (@Composable (query: String) -> Unit)? = { query ->
        SelectFlowModalDefaults.LoadingContent(query = query, labels = labels)
    },
    content: @Composable (
        sections: List<SelectOptionSection<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
        onCreate: (() -> Unit)?,
    ) -> Unit = { modalSections, selectedValues, onSelect, onCreate ->
        SelectFlowModalDefaults.SectionedList(
            sections = modalSections,
            values = selectedValues,
            onSelect = onSelect,
            labels = labels,
            query = state.query.trim(),
            onCreateOption = onCreate,
        )
    },
) {
    val query = state.query.trim()
    val create: (() -> Unit)? = if (onQueryChange != null && onCreateOption != null && query.isNotEmpty()) {
        { onCreateOption(query) }
    } else {
        null
    }

    SelectFlowModalFrame(
        controller = controller,
        values = values,
        modifier = modifier,
        onChange = onChange,
        state = state,
        selectionMode = selectionMode,
        style = style,
        size = size,
        title = title,
        header = header,
        headerBottom = headerBottom,
        footer = footer,
        closeOnSelect = closeOnSelect,
        clearable = clearable,
        onQueryChange = onQueryChange,
        searchPlaceholder = searchPlaceholder,
        isEmpty = create == null && sections.all { section -> section.options.isEmpty() },
        emptyContent = emptyContent,
        loadingContent = loadingContent,
    ) { draftValues, onSelect ->
        content(sections, draftValues, onSelect, create)
    }
}

/**
 * The sheet both [SelectFlowModal] overloads share: the draft, its reset on close, the search field
 * and the choice between the loading, empty and option bodies.
 *
 * @param Value Type of the values being selected.
 * @param controller Controller that shows and hides the sheet.
 * @param values Values selected outside the modal.
 * @param modifier Modifier applied to the sheet.
 * @param onChange Called with the new draft on every toggle.
 * @param state Search state.
 * @param selectionMode Whether one value or many can be held at once.
 * @param style Colors and shape for the sheet.
 * @param size Height and inset behavior for the sheet.
 * @param title Title of the stock header.
 * @param header Fixed content above the list, in place of the stock header.
 * @param headerBottom Content under the stock header's title.
 * @param footer Fixed content below the list, given the draft.
 * @param closeOnSelect Whether a single choice closes the sheet.
 * @param clearable Whether pressing the chosen option of a single choice clears it.
 * @param onQueryChange Query callback, or null for no search field.
 * @param searchPlaceholder Hint in the search field.
 * @param isEmpty Whether there is nothing to list, which shows [emptyContent].
 * @param emptyContent Body shown when [isEmpty].
 * @param loadingContent Body shown while searching.
 * @param body The options, given the draft and the select callback.
 */
@Composable
private fun <Value> SelectFlowModalFrame(
    controller: FlowModalController,
    values: Set<Value>,
    modifier: Modifier,
    onChange: (Set<Value>) -> Unit,
    state: SelectFlowModalState,
    selectionMode: SelectSelectionMode,
    style: FlowModalStyle,
    size: FlowModalSize,
    title: String?,
    header: (@Composable () -> Unit)?,
    headerBottom: (@Composable () -> Unit)?,
    footer: (@Composable (draftValues: Set<Value>) -> Unit)?,
    closeOnSelect: Boolean,
    clearable: Boolean,
    onQueryChange: ((String) -> Unit)?,
    searchPlaceholder: String,
    isEmpty: Boolean,
    emptyContent: (@Composable (query: String) -> Unit)?,
    loadingContent: (@Composable (query: String) -> Unit)?,
    body: @Composable (draftValues: Set<Value>, onSelect: (SelectOption<Value>) -> Unit) -> Unit,
) {
    // Keyed on the caller's values so a selection changed from outside replaces the draft in the
    // same composition, rather than one frame after the sheet has drawn the stale one.
    val draft = remember(values) { SelectFlowModalDraft(values) }
    val draftValues = draft.values

    // The modal stays composed while the controller hides and shows the sheet, so closing is
    // watched explicitly: once the sheet is fully hidden, unapplied edits are dropped and the next
    // opening starts from the caller's values again.
    // A closed sheet also forgets its search, telling the caller so its filter widens again: the
    // next opening shows every option, not the last query's leftovers.
    val currentOnQueryChange by rememberUpdatedState(onQueryChange)
    LaunchedEffect(controller, draft, state) {
        snapshotFlow { controller.isVisible }.collect { visible ->
            if (!visible) {
                draft.discard()
                if (state.query.isNotEmpty()) {
                    state.query = ""
                    currentOnQueryChange?.invoke("")
                }
            }
        }
    }

    // The keyboard of a focused search leaves with the sheet rather than staying over the screen.
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val close: () -> Unit = {
        focusManager.clearFocus()
        keyboard?.hide()
        controller.hide()
    }

    val stockHeader: (@Composable () -> Unit)? = header ?: title?.let { text ->
        { FlowModalTopBar(title = text, onClose = close, bottom = headerBottom) }
    }

    FlowModal(
        controller = controller,
        modifier = modifier,
        header = stockHeader,
        style = style,
        size = size,
        footer = { footer?.invoke(draftValues) },
    ) {
        // The sheet is as tall as the list, so a short one opens at its rows. A searchable one takes
        // the whole sheet, so it does not jump while a search narrows it.
        Column(modifier = if (onQueryChange != null) Modifier.fillMaxHeight() else Modifier) {
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

                isEmpty && emptyContent != null -> {
                    emptyContent(state.query)
                }

                else -> {
                    body(draftValues) { option ->
                        val single = selectionMode == SelectSelectionMode.Single
                        val keep = selectionMode.keepsChosen(draftValues, option, clearable)
                        if (!keep) {
                            val chosen = draft.select(option, selectionMode)
                            onChange(chosen)
                        }
                        if (closeOnSelect && single && (keep || draft.values.isNotEmpty())) close()
                    }
                }
            }
            Spacer(Modifier.height(ArecaTheme.spacing.xl))
        }
    }
}
