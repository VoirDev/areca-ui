package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.list.CheckmarkListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Stock bodies for [SelectFlowModal] and [SelectField].
 *
 * Each of these fills a slot on those composables and can be replaced wholesale. They are public
 * so that a caller who only wants to change one part — a richer option row, say — can keep the
 * rest rather than reimplementing the sheet.
 *
 * The copy here is English and not localized. Pass your own body for these slots in an app that
 * ships in more than one language.
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
     */
    @Composable
    public fun LoadingContent(
        query: String,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        ) {
            Text(
                text = if (query.isBlank()) "Loading..." else "Searching \"$query\"...",
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
     */
    @Composable
    public fun EmptyContent(
        query: String,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        ) {
            Text(
                text = if (query.isBlank()) "No items" else "No results for \"$query\"",
                color = ArecaTheme.colors.fgMuted,
            )
        }
    }
}
