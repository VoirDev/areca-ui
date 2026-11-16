package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.list.CheckmarkListItem
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Plus
import dev.voir.areca.theme.ArecaTheme

/**
 * Stock bodies for [SelectFlowModal] and [SelectField].
 *
 * Each of these fills a slot on those composables and can be replaced wholesale. They are public
 * so that a caller who only wants to change one part — a richer option row, say — can keep the
 * rest rather than reimplementing the sheet.
 *
 * The copy defaults to [SelectFlowModalLabels.English]. Pass your own [SelectFlowModalLabels] in an
 * app that ships in more than one language.
 */
public object SelectFlowModalDefaults {
    /**
     * The option list: one [CheckmarkListItem] per option, marked on the chosen ones.
     *
     * @param Value Type of the values being selected.
     * @param options Options to render, in display order.
     * @param values Currently selected values.
     * @param onSelect Called with the option the user acted on. The caller works out what the
     *   selection becomes.
     * @param modifier Modifier applied to the list.
     * @param state Scroll state, hoisted so a caller can scroll to the current selection.
     * @param contentPadding Inset around the items, inside the scrolling area.
     * @param verticalArrangement Arrangement of the items along the list.
     * @param horizontalAlignment Alignment of the items across the list.
     */
    @Composable
    public fun <Value> List(
        options: List<SelectOption<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
        modifier: Modifier = Modifier,
        state: LazyListState = rememberLazyListState(),
        contentPadding: PaddingValues = PaddingValues(0.dp),
        verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    ) {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
        ) {
            items(options, key = { item -> item.key }) { option ->
                val selected = values.contains(option.value)

                CheckmarkListItem(
                    selected = selected,
                    // Every activation is reported: whether choosing a chosen option clears it is
                    // the field's decision, not the row's.
                    onClick = { onSelect(option) },
                    title = { Text(option.label) },
                    enabled = option.enabled,
                )
            }
        }
    }

    /**
     * The sectioned option list: each [SelectOptionSection] under its [SectionHeader], an indexed
     * section broken under letter headers, and the [CreateOptionRow] last.
     *
     * Rows are keyed by section and option together, so one option may be listed in several
     * sections. A section with no options is skipped, header and all.
     *
     * @param Value Type of the values being selected.
     * @param sections Sections to render, in display order.
     * @param values Currently selected values.
     * @param onSelect Called with the option the user acted on. The caller works out what the
     *   selection becomes.
     * @param modifier Modifier applied to the list.
     * @param labels Wording of the letter header for labels that do not start with a letter, and of
     *   the create row, built from [query].
     * @param query What was typed, named by the create row.
     * @param onCreateOption Called when the create row is pressed, or `null` to draw no such row.
     * @param state Scroll state, hoisted so a caller can scroll to the current selection.
     * @param contentPadding Inset around the items, inside the scrolling area.
     */
    @Composable
    public fun <Value> SectionedList(
        sections: List<SelectOptionSection<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
        modifier: Modifier = Modifier,
        labels: SelectFlowModalLabels = SelectFlowModalLabels.English,
        query: String = "",
        onCreateOption: (() -> Unit)? = null,
        state: LazyListState = rememberLazyListState(),
        contentPadding: PaddingValues = PaddingValues(0.dp),
    ) {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
        ) {
            sections.forEach { section ->
                if (section.options.isEmpty()) return@forEach

                val title = section.title
                if (title != null) {
                    item(key = sectionHeaderKey(section.key), contentType = SectionHeaderContentType) {
                        SectionHeader(text = title)
                    }
                }

                if (section.indexed) {
                    indexRuns(section.options, other = labels.indexOther).forEachIndexed { index, run ->
                        item(
                            key = letterHeaderKey(section.key, run.letter, index),
                            contentType = LetterHeaderContentType,
                        ) {
                            SectionHeader(text = run.letter, letter = true)
                        }
                        optionItems(section.key, run.options, values, onSelect)
                    }
                } else {
                    optionItems(section.key, section.options, values, onSelect)
                }
            }

            if (onCreateOption != null) {
                item(key = CreateOptionKey) {
                    CreateOptionRow(text = labels.createOption(query), onClick = onCreateOption)
                }
            }
        }
    }

    /**
     * The header above a section of a [SectionedList], or above a run of an indexed section.
     *
     * Carries the heading role, so a screen reader can jump from section to section.
     *
     * @param text Title of the section, or the letter of the run.
     * @param modifier Modifier applied to the header.
     * @param letter Whether this heads a run of an indexed section rather than a section: quieter,
     *   so the letters read as an index inside the section rather than as sections of their own.
     */
    @Composable
    internal fun SectionHeader(
        text: String,
        modifier: Modifier = Modifier,
        letter: Boolean = false,
    ) {
        Text(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = ArecaTheme.spacing.horizontalContentInsets,
                    end = ArecaTheme.spacing.horizontalContentInsets,
                    top = if (letter) ArecaTheme.spacing.sm else ArecaTheme.spacing.lg,
                    bottom = ArecaTheme.spacing.xs,
                )
                .semantics { heading() },
            style = if (letter) ArecaTheme.typography.caption else ArecaTheme.typography.labelMedium,
            color = if (letter) ArecaTheme.colors.fgMuted else ArecaTheme.colors.fgSubtle,
        )
    }

    /**
     * The row that adds what was typed as an option of the user's own — "Add “Lenovo” as your own".
     *
     * A plus in the brand color marks it as an action rather than one of the options.
     *
     * @param text Caption of the row, naming what will be added.
     * @param onClick Called when the row is pressed. The sheet is not closed for you.
     * @param modifier Modifier applied to the row.
     */
    @Composable
    public fun CreateOptionRow(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ListItem(
            modifier = modifier,
            title = { Text(text) },
            onClick = onClick,
            leading = {
                Icon(
                    imageVector = BundledIcons.Plus,
                    contentDescription = null,
                    tint = ArecaTheme.colors.fgBrand,
                )
            },
        )
    }

    /**
     * The search field above the option list.
     *
     * @param value Current query.
     * @param placeholder Hint shown while the query is empty.
     * @param onValueChange Called with the new query on every edit.
     * @param modifier Modifier applied to the search row.
     * @param style Color palette and shape for the field. Pill-shaped by default, so the
     *   search reads as a control rather than as another boxed field.
     * @param size Metrics for the field. Smaller than the list rows on purpose: the search is
     *   how you get to the options, not one of them.
     */
    @Composable
    public fun Search(
        value: String,
        placeholder: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        style: FieldStyle = FieldStyle.outlined(shape = CircleShape),
        size: FieldSize = FieldSize.Small,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        ) {
            SearchField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder) },
                style = style,
                size = size,
            )
        }
    }

    /**
     * The body shown while options are being fetched.
     *
     * @param query Current search text, named in the message so it is clear what is being waited
     *   on. Blank means the first load rather than a search.
     * @param modifier Modifier applied to the body.
     * @param labels Wording of the message.
     */
    @Composable
    public fun LoadingContent(
        query: String,
        modifier: Modifier = Modifier,
        labels: SelectFlowModalLabels = SelectFlowModalLabels.English,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        ) {
            Text(
                text = if (query.isBlank()) labels.loading else labels.searching(query),
                color = ArecaTheme.colors.fgMuted,
            )
        }
    }

    /**
     * The body shown when there are no options to offer.
     *
     * @param query Current search text. It separates the two reasons the list can be empty —
     *   nothing matched what was typed, or there was nothing there in the first place.
     * @param modifier Modifier applied to the body.
     * @param labels Wording of the message.
     */
    @Composable
    public fun EmptyContent(
        query: String,
        modifier: Modifier = Modifier,
        labels: SelectFlowModalLabels = SelectFlowModalLabels.English,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        ) {
            Text(
                text = if (query.isBlank()) labels.empty else labels.noResults(query),
                color = ArecaTheme.colors.fgMuted,
            )
        }
    }
}

// Lazy keys are strings: on Android a key must fit in a Bundle, which a data class does not. Each
// part is prefixed with its length so that no two different pairs of keys spell the same string.

/**
 * A key part prefixed with its length.
 *
 * @param part Section or option key, by its string form.
 * @return The part, unambiguous next to others.
 */
private fun keyPart(part: Any): String = part.toString().let { text -> "${text.length}.$text" }

/**
 * Lazy key of a section's header.
 *
 * @param section Key of the section.
 * @return The key.
 */
internal fun sectionHeaderKey(section: Any): String = "select-section:${keyPart(section)}"

/**
 * Lazy key of a letter header inside an indexed section.
 *
 * The run's position is part of it because an unsorted section can head two runs with one letter.
 *
 * @param section Key of the section.
 * @param letter Header of the run.
 * @param run Position of the run in the section.
 * @return The key.
 */
internal fun letterHeaderKey(section: Any, letter: String, run: Int): String =
    "select-letter:${keyPart(section)}:${keyPart(letter)}:$run"

/**
 * Lazy key of an option row, unique even when the option is listed in several sections.
 *
 * @param section Key of the section.
 * @param option Key of the option.
 * @return The key.
 */
internal fun optionKey(section: Any, option: Any): String =
    "select-option:${keyPart(section)}:${keyPart(option)}"

/** Lazy key of the create-option row. */
private const val CreateOptionKey = "select-create-option"

/** Content type of section headers. */
private const val SectionHeaderContentType = "select-section-header"

/** Content type of letter headers. */
private const val LetterHeaderContentType = "select-letter-header"

/** Content type of option rows. */
private const val OptionContentType = "select-option"

/**
 * Adds one [CheckmarkListItem] per option of a section.
 *
 * @param Value Type of the values being selected.
 * @param section Key of the section the options belong to.
 * @param options Options to add.
 * @param values Currently selected values.
 * @param onSelect Called with the option the user acted on.
 */
private fun <Value> LazyListScope.optionItems(
    section: Any,
    options: List<SelectOption<Value>>,
    values: Set<Value>,
    onSelect: (SelectOption<Value>) -> Unit,
) {
    items(
        items = options,
        key = { option -> optionKey(section, option.key) },
        contentType = { OptionContentType },
    ) { option ->
        CheckmarkListItem(
            selected = values.contains(option.value),
            onClick = { onSelect(option) },
            title = { Text(option.label) },
            enabled = option.enabled,
        )
    }
}
