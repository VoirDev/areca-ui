package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import dev.voir.areca.foundation.layout.DividerTextPosition
import dev.voir.areca.foundation.layout.DividerSize
import dev.voir.areca.foundation.layout.DividerStyle

/**
 * One entry of a list built from data rather than from composable calls.
 *
 * The model exists so the same entries can be handed to either [List] or [LazyList] — an eagerly
 * composed list and a lazy one — without the call site being rewritten when a list outgrows the
 * screen.
 */
public sealed interface ListEntry {

    /** Stable identity of the entry, used as the key of the lazy item that renders it. */
    public val key: Any

    /**
     * A row, carrying the arguments [ListItem] would have been called with.
     *
     * @property key Stable identity of the row.
     * @property title Main copy of the row; ignored when [content] is provided.
     * @property subtitle Supporting copy below [title].
     * @property overline Small copy above [title].
     * @property value Inline value rendered at the end of the row.
     * @property enabled Whether the row accepts input.
     * @property selected Whether the row is one of the selected rows.
     * @property highlightSelected Whether a selected row draws its selected background and rail.
     * @property nestingLevel Indentation steps to add.
     * @property style Colors of this row, overriding the style the list provides. Use it for the one
     * row that differs — a destructive action among ordinary ones.
     * @property onClick Click handler; `null` for a row that only displays.
     * @property onClickLabel Accessibility description of what the click does.
     * @property leading Slot before the text block.
     * @property trailing Slot after the text block.
     * @property bottom Content below the main row line, inside the row hit area.
     * @property content Replacement for the whole text block.
     */
    public data class Item(
        override val key: Any,
        public val title: (@Composable () -> Unit)? = null,
        public val subtitle: (@Composable () -> Unit)? = null,
        public val overline: (@Composable () -> Unit)? = null,
        public val value: (@Composable () -> Unit)? = null,
        public val enabled: Boolean = true,
        public val selected: Boolean = false,
        public val highlightSelected: Boolean = true,
        public val nestingLevel: Int = 0,
        public val style: ListItemStyle? = null,
        public val onClick: (() -> Unit)? = null,
        public val onClickLabel: String? = null,
        public val leading: (@Composable BoxScope.() -> Unit)? = null,
        public val trailing: (@Composable BoxScope.() -> Unit)? = null,
        public val bottom: (@Composable ColumnScope.() -> Unit)? = null,
        public val content: (@Composable BoxScope.() -> Unit)? = null,
    ) : ListEntry

    /**
     * A deliberate break between groups of rows, optionally labelled.
     *
     * This is not the hairline between two adjacent rows — that belongs to the list, which draws it
     * for every boundary when asked. Use a divider entry where the break carries meaning.
     *
     * @property key Stable identity of the divider.
     * @property style Colors of the divider, or `null` to use the list's.
     * @property size Metrics of the divider, or `null` to use the list's.
     * @property label Copy rendered inside the divider line, naming the group that follows.
     * @property labelPosition Where [label] sits along the line.
     */
    public data class Divider(
        override val key: Any,
        public val style: DividerStyle? = null,
        public val size: DividerSize? = null,
        public val label: String? = null,
        public val labelPosition: DividerTextPosition = DividerTextPosition.Center,
    ) : ListEntry
}
