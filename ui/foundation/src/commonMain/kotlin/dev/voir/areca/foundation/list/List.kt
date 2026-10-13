package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.layout.DividerSize
import dev.voir.areca.foundation.layout.DividerStyle
import dev.voir.areca.foundation.layout.LocalDividerSize
import dev.voir.areca.foundation.layout.LocalDividerStyle
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/** Marks the receiver of the list DSL so its scope cannot be mixed up with another. */
@DslMarker
public annotation class ListDsl

/**
 * Receiver of the content of a [List] or a [LazyList], collecting the entries it declares.
 */
@ListDsl
public class ListScope internal constructor() {

    /** Entries declared so far, in the order they were declared. */
    internal val entries = mutableListOf<ListEntry>()

    /**
     * Declares a row.
     *
     * @param key Stable identity of the row.
     * @param title Main copy of the row; ignored when [content] is provided.
     * @param subtitle Supporting copy below [title].
     * @param overline Small copy above [title].
     * @param value Inline value rendered at the end of the row.
     * @param enabled Whether the row accepts input.
     * @param selected Whether the row is one of the selected rows.
     * @param highlightSelected Whether a selected row draws its selected background and rail.
     * @param nestingLevel Indentation steps to add.
     * @param style Colors of this row, overriding the style the list provides.
     * @param onClick Click handler; omit for a row that only displays.
     * @param onClickLabel Accessibility description of what the click does.
     * @param leading Slot before the text block.
     * @param trailing Slot after the text block.
     * @param bottom Content below the main row line, inside the row hit area.
     * @param content Replacement for the whole text block.
     */
    public fun item(
        key: Any,
        title: (@Composable () -> Unit)? = null,
        subtitle: (@Composable () -> Unit)? = null,
        overline: (@Composable () -> Unit)? = null,
        value: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        selected: Boolean = false,
        highlightSelected: Boolean = true,
        nestingLevel: Int = 0,
        style: ListItemStyle? = null,
        onClick: (() -> Unit)? = null,
        onClickLabel: String? = null,
        leading: (@Composable BoxScope.() -> Unit)? = null,
        trailing: (@Composable BoxScope.() -> Unit)? = null,
        bottom: (@Composable ColumnScope.() -> Unit)? = null,
        content: (@Composable BoxScope.() -> Unit)? = null,
    ) {
        entries += ListEntry.Item(
            key = key,
            title = title,
            subtitle = subtitle,
            overline = overline,
            value = value,
            enabled = enabled,
            selected = selected,
            highlightSelected = highlightSelected,
            nestingLevel = nestingLevel,
            style = style,
            onClick = onClick,
            onClickLabel = onClickLabel,
            leading = leading,
            trailing = trailing,
            bottom = bottom,
            content = content,
        )
    }

    /**
     * Declares a meaningful break between groups of rows.
     *
     * @param key Stable identity of the divider; defaults to its position among the entries.
     * @param label Copy rendered inside the line, naming the group that follows.
     * @param style Colors of the divider, or `null` to use the list's.
     * @param size Metrics of the divider, or `null` to use the list's.
     */
    public fun divider(
        key: Any = "divider:${entries.size}",
        label: String? = null,
        style: DividerStyle? = null,
        size: DividerSize? = null,
    ) {
        entries += ListEntry.Divider(
            key = key,
            label = label,
            style = style,
            size = size,
        )
    }
}

/**
 * A list of rows composed eagerly, for a set small enough to fit in one pass.
 *
 * The list owns the group surface and the hairlines between its rows, so rows stay square and
 * transparent and inherit the group's corners. Reach for [LazyList] once the set is long enough that
 * composing all of it is wasteful.
 *
 * @param modifier Modifier applied to the list container.
 * @param itemStyle Colors given to every row; a row can override it.
 * @param itemSize Metrics given to every row.
 * @param dividerStyle Colors of the divider entries.
 * @param dividerSize Metrics of the divider entries.
 * @param listStyle Colors of the list's own surface: background, border and separators.
 * @param listSize Metrics of the list's own surface: shape, border width and separator geometry.
 * @param separated Whether to draw a hairline at every boundary between two rows. On by default,
 * like every list container in this package.
 * @param enabled Whether the rows accept input; `false` disables every row.
 * @param contentPadding Inset around the list, outside its background.
 * @param content Entries of the list.
 */
@Composable
public fun List(
    modifier: Modifier = Modifier,
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default,
    separated: Boolean = true,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit,
) {
    val scope = ListScope().apply(content)

    List(
        entries = scope.entries,
        modifier = modifier,
        itemStyle = itemStyle,
        itemSize = itemSize,
        dividerStyle = dividerStyle,
        dividerSize = dividerSize,
        listStyle = listStyle,
        listSize = listSize,
        separated = separated,
        enabled = enabled,
        contentPadding = contentPadding,
    )
}

/**
 * A list of rows composed eagerly from an entry model.
 *
 * @param entries Entries to render, in display order.
 * @param modifier Modifier applied to the list container.
 * @param itemStyle Colors given to every row; a row can override it.
 * @param itemSize Metrics given to every row.
 * @param dividerStyle Colors of the divider entries.
 * @param dividerSize Metrics of the divider entries.
 * @param listStyle Colors of the list's own surface: background, border and separators.
 * @param listSize Metrics of the list's own surface: shape, border width and separator geometry.
 * @param separated Whether to draw a hairline at every boundary between two rows. On by default,
 * like every list container in this package.
 * @param enabled Whether the rows accept input; `false` disables every row.
 * @param contentPadding Inset around the list, outside its background.
 */
@Composable
public fun List(
    entries: kotlin.collections.List<ListEntry>,
    modifier: Modifier = Modifier,
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default,
    separated: Boolean = true,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    ListItemColumn(
        modifier = modifier.padding(contentPadding),
        listStyle = listStyle,
        listSize = listSize,
        separated = separated,
        enabled = enabled,
    ) {
        entries.forEach { entry ->
            ListEntryContent(
                entry = entry,
                itemStyle = itemStyle,
                itemSize = itemSize,
                dividerStyle = dividerStyle,
                dividerSize = dividerSize,
            )
        }
    }
}

/**
 * A list of rows composed on demand, for a set too long to compose in one pass.
 *
 * Separators are drawn by each row rather than by a group layout, because a lazy list never has all
 * of its boundaries in hand at once. The first visible row draws no line above itself.
 *
 * @param modifier Modifier applied to the list container.
 * @param state Scroll state, hoisted so a caller can scroll to a row.
 * @param itemStyle Colors given to every row; a row can override it.
 * @param itemSize Metrics given to every row.
 * @param dividerStyle Colors of the divider entries.
 * @param dividerSize Metrics of the divider entries.
 * @param listStyle Colors of the list's own surface: background, border and separators.
 * @param listSize Metrics of the list's own surface: shape, border width and separator geometry.
 * @param separated Whether to draw a hairline at every boundary between two rows. On by default,
 * like every list container in this package.
 * @param enabled Whether the rows accept input; `false` disables every row.
 * @param contentPadding Inset around the entries, inside the scrolling area.
 * @param content Entries of the list.
 */
@Composable
public fun LazyList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default,
    separated: Boolean = true,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit,
) {
    val scope = ListScope().apply(content)

    LazyList(
        entries = scope.entries,
        modifier = modifier,
        state = state,
        itemStyle = itemStyle,
        itemSize = itemSize,
        dividerStyle = dividerStyle,
        dividerSize = dividerSize,
        listStyle = listStyle,
        listSize = listSize,
        separated = separated,
        enabled = enabled,
        contentPadding = contentPadding,
    )
}

/**
 * A list of rows composed on demand from an entry model.
 *
 * @param entries Entries to render, in display order.
 * @param modifier Modifier applied to the list container.
 * @param state Scroll state, hoisted so a caller can scroll to a row.
 * @param itemStyle Colors given to every row; a row can override it.
 * @param itemSize Metrics given to every row.
 * @param dividerStyle Colors of the divider entries.
 * @param dividerSize Metrics of the divider entries.
 * @param listStyle Colors of the list's own surface: background, border and separators.
 * @param listSize Metrics of the list's own surface: shape, border width and separator geometry.
 * @param separated Whether to draw a hairline at every boundary between two rows. On by default,
 * like every list container in this package.
 * @param enabled Whether the rows accept input; `false` disables every row.
 * @param contentPadding Inset around the entries, inside the scrolling area.
 */
@Composable
public fun LazyList(
    entries: kotlin.collections.List<ListEntry>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default,
    separated: Boolean = true,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    CompositionLocalProvider(
        LocalListEnabled provides (enabled && LocalListEnabled.current),
    ) {
        LazyColumn(
            // Rows scroll under the rounded edge, so the viewport clips to the list's shape.
            modifier = modifier
                .clip(listSize.shape)
                .listSurface(listStyle = listStyle, listSize = listSize),
            state = state,
            contentPadding = contentPadding,
        ) {
            items(count = entries.size, key = { index -> entries[index].key }) { index ->
                val entry = entries[index]
                // A divider entry is already a line; a hairline above it would double it up.
                val drawSeparator = separated &&
                    index > 0 &&
                    entry is ListEntry.Item &&
                    entries[index - 1] is ListEntry.Item

                ListEntryContent(
                    entry = entry,
                    itemStyle = itemStyle,
                    itemSize = itemSize,
                    dividerStyle = dividerStyle,
                    dividerSize = dividerSize,
                    modifier = Modifier.thenIf(drawSeparator) {
                        topSeparator(
                            color = listStyle.separator,
                            thickness = listSize.separatorThickness,
                            inset = listSize.separatorInset,
                        )
                    },
                )
            }
        }
    }
}

/**
 * Renders one entry with the list's styles, letting a row override the style it was given.
 *
 * @param entry Entry to render.
 * @param itemStyle Colors the list provides to its rows.
 * @param itemSize Metrics the list provides to its rows.
 * @param dividerStyle Colors the list provides to its dividers.
 * @param dividerSize Metrics the list provides to its dividers.
 * @param modifier Modifier applied to the rendered entry.
 */
@Composable
private fun ListEntryContent(
    entry: ListEntry,
    itemStyle: ListItemStyle,
    itemSize: ListItemSize,
    dividerStyle: DividerStyle,
    dividerSize: DividerSize,
    modifier: Modifier = Modifier,
) {
    when (entry) {
        is ListEntry.Item -> ListItem(
            modifier = modifier,
            title = entry.title,
            subtitle = entry.subtitle,
            overline = entry.overline,
            value = entry.value,
            enabled = entry.enabled,
            selected = entry.selected,
            highlightSelected = entry.highlightSelected,
            nestingLevel = entry.nestingLevel,
            style = entry.style ?: itemStyle,
            size = itemSize,
            onClick = entry.onClick,
            onClickLabel = entry.onClickLabel,
            leading = entry.leading,
            trailing = entry.trailing,
            bottom = entry.bottom,
            content = entry.content,
        )

        is ListEntry.Divider -> HorizontalDivider(
            modifier = modifier.layoutId(ListDividerLayoutId),
            style = entry.style ?: dividerStyle,
            size = entry.size ?: dividerSize,
            text = entry.label,
            textPosition = entry.labelPosition,
        )
    }
}

/**
 * Draws a hairline along the top edge of a row.
 *
 * A lazy list has no single layout that knows where all of its row boundaries are, so each row draws
 * the line above itself instead.
 *
 * @param color Color of the line.
 * @param thickness Thickness of the line, floored at one pixel so it never disappears on a
 * low-density screen.
 * @param inset Distance between the line and both row edges.
 * @return Modifier that draws the line.
 */
private fun Modifier.topSeparator(
    color: Color,
    thickness: Dp,
    inset: Dp,
): Modifier = drawWithContent {
    drawContent()

    if (color == Color.Unspecified || color.alpha <= 0.001f) return@drawWithContent

    val lineThickness = thickness.toPx().coerceAtLeast(1f)
    val insetPx = inset.toPx()
    val lineWidth = size.width - insetPx * 2f
    if (lineWidth <= 0f) return@drawWithContent

    // Drawn over the row, like the group layout does, so a hover tint runs underneath the line
    // rather than covering it.
    drawRect(
        color = color,
        topLeft = Offset(insetPx, 0f),
        size = Size(lineWidth, lineThickness),
    )
}
