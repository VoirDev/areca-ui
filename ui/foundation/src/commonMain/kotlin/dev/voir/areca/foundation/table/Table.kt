package dev.voir.areca.foundation.table

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.HorizontalScrollbarOverlay
import dev.voir.areca.foundation.layout.VerticalScrollbarOverlay
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * One column of a [Table] or [LazyTable].
 *
 * A fixed column ([width] set) is always exactly that wide. An auto column ([width] `null`) is as
 * wide as its widest header or cell content, clamped to [minWidth] and [maxWidth]. When all columns
 * together are narrower than the table, auto columns share the leftover space by [expandWeight];
 * when they are wider, the table scrolls horizontally instead of shrinking them.
 *
 * @param T Type of the row items.
 * @property id Stable identity of the column, unique within the table.
 * @property title Header text drawn by the default header renderer.
 * @property width Exact width of a fixed column, or `null` for an auto column.
 * @property minWidth Smallest width of an auto column, including cell padding.
 * @property maxWidth Largest width of an auto column, including cell padding, or `null` when
 *   unbounded. The default renderers ellipsize content wider than this.
 * @property expandWeight Share of leftover table width an auto column takes; `0` takes none.
 * @property value Value shown by the default cell renderer, rendered with `toString()`.
 * @property cell Custom cell renderer, replacing the default one.
 * @property header Custom header renderer, replacing the default one.
 */
@Immutable
public data class TableColumn<T>(
    public val id: String,
    public val title: String,
    public val width: Dp? = null,
    public val minWidth: Dp = 80.dp,
    public val maxWidth: Dp? = null,
    public val expandWeight: Float = 1f,
    public val value: (T) -> Any? = { "" },
    public val cell: (@Composable (T) -> Unit)? = null,
    public val header: (@Composable () -> Unit)? = null
)

/**
 * Colors of a [Table] or [LazyTable].
 *
 * @property headerBg Fill of the header row.
 * @property rowBg Fill of every body row.
 * @property divider Rules under the header and between rows of a separated table.
 *   [Color.Unspecified] draws none.
 */
@Immutable
public data class TableStyle(
    public val headerBg: Color,
    public val rowBg: Color,
    public val divider: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a muted header over canvas rows, ruled with the base border. */
        public val Default: TableStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param headerBg Fill of the header row.
         * @param rowBg Fill of every body row.
         * @param divider Rules under the header and between rows of a separated table.
         * @return The style.
         */
        @Composable
        public fun default(
            headerBg: Color = ArecaTheme.colors.bgMuted,
            rowBg: Color = ArecaTheme.colors.bgCanvas,
            divider: Color = ArecaTheme.colors.borderBase,
        ): TableStyle = TableStyle(headerBg = headerBg, rowBg = rowBg, divider = divider)
    }
}

/**
 * Metrics of a [Table] or [LazyTable].
 *
 * @property cellPadding Padding inside every body cell.
 * @property headerPadding Padding inside every header cell.
 * @property headerHeight Height of the header row.
 * @property rowMinHeight Smallest height of a body row; taller content grows the row.
 * @property dividerThickness Thickness of the rules drawn with [TableStyle.divider].
 */
@Immutable
public data class TableSize(
    public val cellPadding: PaddingValues,
    public val headerPadding: PaddingValues,
    public val headerHeight: Dp,
    public val rowMinHeight: Dp,
    public val dividerThickness: Dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /**
         * Theme default: 48dp rows under a 44dp header under touch; 36dp rows under a 36dp header
         * under a pointer.
         */
        public val Default: TableSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param cellPadding Padding inside every body cell.
         * @param headerPadding Padding inside every header cell.
         * @param headerHeight Height of the header row.
         * @param rowMinHeight Smallest height of a body row.
         * @param dividerThickness Thickness of the rules.
         * @return The size.
         */
        @Composable
        public fun default(
            cellPadding: PaddingValues = PaddingValues(
                vertical = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.md,
                    pointer = ArecaTheme.spacing.sm,
                ),
                horizontal = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.lg,
                    pointer = ArecaTheme.spacing.md,
                ),
            ),
            headerPadding: PaddingValues = PaddingValues(
                vertical = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.md,
                    pointer = ArecaTheme.spacing.sm,
                ),
                horizontal = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.lg,
                    pointer = ArecaTheme.spacing.md,
                ),
            ),
            headerHeight: Dp = ArecaTheme.sizing.rowMedium,
            rowMinHeight: Dp = ArecaTheme.sizing.controlMd,
            dividerThickness: Dp = 1.dp,
        ): TableSize = TableSize(
            cellPadding = cellPadding,
            headerPadding = headerPadding,
            headerHeight = headerHeight,
            rowMinHeight = rowMinHeight,
            dividerThickness = dividerThickness,
        )
    }
}

/** Default [TableStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTableStyle: ProvidableCompositionLocal<TableStyle?> = compositionLocalOf { null }

/** Default [TableSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTableSize: ProvidableCompositionLocal<TableSize?> = compositionLocalOf { null }

/**
 * A table whose body rows are composed on demand, for a data set too long to compose in one pass.
 *
 * The header stays put while the body scrolls vertically; header and body scroll horizontally
 * together when the columns are wider than the table. Column widths follow [TableColumn]: auto
 * columns grow to the widest content measured so far and never narrow while scrolling.
 *
 * Announced as a collection of `items.size + 1` rows — the header first — by `columns.size`
 * columns, with every header cell a heading.
 *
 * @param T Type of the row items.
 * @param items Rows of the body, in display order.
 * @param columns Columns of the table, in display order.
 * @param modifier Modifier applied to the table.
 * @param style Colors. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 * @param separated Whether to draw rules under the header and between body rows, in
 *   [TableStyle.divider].
 * @param key Stable identity of a row, or `null` to key by position.
 */
@Composable
public fun <T> LazyTable(
    items: List<T>,
    columns: List<TableColumn<T>>,
    modifier: Modifier = Modifier,
    style: TableStyle = LocalTableStyle.current ?: TableStyle.Default,
    size: TableSize = LocalTableSize.current ?: TableSize.Default,
    separated: Boolean = true,
    key: ((index: Int, item: T) -> Any)? = null,
) {
    WithTableState(columns = columns) {
        Column(modifier = modifier.tableSemantics(rowCount = items.size, columnCount = columns.size)) {
            TableHeader(columns = columns, style = style, size = size, drawRule = separated)
            val bodyState = rememberLazyListState()
            Box(modifier = Modifier.fillMaxWidth(), propagateMinConstraints = true) {
                LazyColumn(state = bodyState) {
                    itemsIndexed(
                        items = items,
                        key = if (key != null) { i, item -> key(i, item) } else null
                    ) { index, item ->
                        TableBodyRow(
                            columns = columns,
                            item = item,
                            itemIndex = index,
                            style = style,
                            size = size,
                            drawRule = separated && index > 0,
                        )
                    }
                }
                VerticalScrollbarOverlay(bodyState)
                HorizontalScrollbarOverlay(LocalTableState.current.horizontalScroll)
            }
        }
    }
}

/**
 * A table composed in one pass, for a data set small enough to compose whole.
 *
 * Header and body scroll horizontally together when the columns are wider than the table. Column
 * widths follow [TableColumn]. Reach for [LazyTable] once the rows are many.
 *
 * Announced as a collection of `items.size + 1` rows — the header first — by `columns.size`
 * columns, with every header cell a heading.
 *
 * @param T Type of the row items.
 * @param items Rows of the body, in display order.
 * @param columns Columns of the table, in display order.
 * @param modifier Modifier applied to the table.
 * @param style Colors. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 * @param separated Whether to draw rules under the header and between body rows, in
 *   [TableStyle.divider].
 */
@Composable
public fun <T> Table(
    items: List<T>,
    columns: List<TableColumn<T>>,
    modifier: Modifier = Modifier,
    style: TableStyle = LocalTableStyle.current ?: TableStyle.Default,
    size: TableSize = LocalTableSize.current ?: TableSize.Default,
    separated: Boolean = true,
) {
    WithTableState(columns = columns) {
        Column(modifier = modifier.tableSemantics(rowCount = items.size, columnCount = columns.size)) {
            TableHeader(columns = columns, style = style, size = size, drawRule = separated)
            Box(modifier = Modifier.fillMaxWidth(), propagateMinConstraints = true) {
                Column {
                    items.forEachIndexed { index, item ->
                        TableBodyRow(
                            columns = columns,
                            item = item,
                            itemIndex = index,
                            style = style,
                            size = size,
                            drawRule = separated && index > 0,
                        )
                    }
                }
                HorizontalScrollbarOverlay(LocalTableState.current.horizontalScroll)
            }
        }
    }
}

/**
 * Shared state of one table: measurements, the horizontal scroll every row follows, and the
 * resolved column widths.
 *
 * @property measurement Widest content measured per column, in pixels.
 * @property horizontalScroll Scroll position shared by the header and every body row.
 * @property density Density used to convert the column metrics to pixels.
 * @property columns Current columns, read inside a snapshot so a new column list re-resolves
 *   widths.
 */
@Stable
private class TableState(
    val measurement: TableMeasurementState,
    val horizontalScroll: ScrollState,
    private val density: Density,
    private val columns: () -> List<TableColumn<*>>,
) {
    /** Width the table is shown in, in pixels, as measured by the header row. */
    var viewportWidthPx by mutableIntStateOf(0)

    /** Resolved width of each column in pixels, in column order. */
    val columnWidthsPx: List<Int> by derivedStateOf {
        TableColumnWidthMath.resolve(
            specs = columns().map { column -> widthSpec(column) },
            viewportPx = viewportWidthPx,
        )
    }

    /**
     * Width policy of [column] in pixels, with its widest measured content.
     *
     * @param column Column to describe.
     * @return Spec for [TableColumnWidthMath.resolve].
     */
    private fun widthSpec(column: TableColumn<*>): TableColumnWidthSpec = with(density) {
        TableColumnWidthSpec(
            fixedPx = column.width?.roundToPx(),
            minPx = column.minWidth.roundToPx(),
            maxPx = column.maxWidth?.roundToPx(),
            expandWeight = column.expandWeight,
            naturalPx = measurement.naturalWidthPx(column.id) ?: 0,
        )
    }
}

/** State of the enclosing table; only provided inside [WithTableState]. */
private val LocalTableState = staticCompositionLocalOf<TableState> {
    error("Table state not initialized")
}

/**
 * Creates the state of one table and provides it to [content].
 *
 * @param columns Columns of the table, whose widths the state resolves.
 * @param content Header and body of the table.
 */
@Composable
private fun <T> WithTableState(
    columns: List<TableColumn<T>>,
    content: @Composable () -> Unit,
) {
    val measurement = rememberTableMeasurementState()
    val horizontalScroll = rememberScrollState()
    val density = LocalDensity.current
    val currentColumns by rememberUpdatedState(columns)
    val tableState = remember(measurement, horizontalScroll, density) {
        TableState(
            measurement = measurement,
            horizontalScroll = horizontalScroll,
            density = density,
            columns = { currentColumns },
        )
    }

    CompositionLocalProvider(
        LocalTableState provides tableState,
        content = content
    )
}

/**
 * Header row of a table; it also measures the width the table is shown in.
 *
 * @param columns Columns to draw a header cell for.
 * @param style Colors of the table.
 * @param size Metrics of the table.
 * @param drawRule Whether to draw a rule along the row's bottom edge.
 */
@Composable
private fun <T> TableHeader(
    columns: List<TableColumn<T>>,
    style: TableStyle,
    size: TableSize,
    drawRule: Boolean,
) {
    val state = LocalTableState.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(size.headerHeight)
            .background(style.headerBg)
            .then(
                if (drawRule) {
                    Modifier.tableRule(color = style.divider, thickness = size.dividerThickness, atTop = false)
                } else {
                    Modifier
                }
            )
            .onSizeChanged { measured -> state.viewportWidthPx = measured.width }
            .horizontalScroll(state.horizontalScroll),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEachIndexed { index, col ->
            TableHeaderCell(columnIndex = index, column = col, padding = size.headerPadding)
        }
    }
}

/**
 * Body row of a table.
 *
 * @param columns Columns to draw a cell for.
 * @param item Row item the cells render.
 * @param itemIndex Position of [item] among the body rows.
 * @param style Colors of the table.
 * @param size Metrics of the table.
 * @param drawRule Whether to draw a rule along the row's top edge.
 */
@Composable
private fun <T> TableBodyRow(
    columns: List<TableColumn<T>>,
    item: T,
    itemIndex: Int,
    style: TableStyle,
    size: TableSize,
    drawRule: Boolean,
) {
    val state = LocalTableState.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = size.rowMinHeight)
            .background(style.rowBg)
            .then(
                if (drawRule) {
                    Modifier.tableRule(color = style.divider, thickness = size.dividerThickness, atTop = true)
                } else {
                    Modifier
                }
            )
            .horizontalScroll(state.horizontalScroll),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEachIndexed { index, col ->
            TableBodyCell(
                item = item,
                rowIndex = tableBodyRowIndex(itemIndex),
                columnIndex = index,
                column = col,
                padding = size.cellPadding,
            )
        }
    }
}

/**
 * Header cell of one column, drawing [TableColumn.header] or the column title.
 *
 * Announced as a heading in the table's first row.
 *
 * @param columnIndex Position of the column in the table.
 * @param column Column the cell belongs to.
 * @param padding Padding inside the cell.
 */
@Composable
private fun <T> TableHeaderCell(
    columnIndex: Int,
    column: TableColumn<T>,
    padding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .tableColumnWidth(
                columnIndex = columnIndex,
                column = column,
                channel = TableMeasurementState.Channel.Header,
                tableState = LocalTableState.current,
            )
            .semantics(mergeDescendants = true) {
                heading()
                collectionItemInfo = tableCellInfo(rowIndex = TableHeaderRowIndex, columnIndex = columnIndex)
            }
            .padding(padding),
        contentAlignment = Alignment.CenterStart
    ) {
        val custom = column.header
        if (custom != null) {
            custom()
        } else {
            Text(
                text = column.title,
                variant = TextVariant.LabelMedium,
                color = TextColor.Subtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Body cell of one column, drawing [TableColumn.cell] or the column value.
 *
 * @param item Row item the cell renders.
 * @param rowIndex Position of the cell's row in the table, the header being row `0`.
 * @param columnIndex Position of the column in the table.
 * @param column Column the cell belongs to.
 * @param padding Padding inside the cell.
 */
@Composable
private fun <T> TableBodyCell(
    item: T,
    rowIndex: Int,
    columnIndex: Int,
    column: TableColumn<T>,
    padding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .tableColumnWidth(
                columnIndex = columnIndex,
                column = column,
                channel = TableMeasurementState.Channel.Cell,
                tableState = LocalTableState.current,
            )
            .semantics(mergeDescendants = true) {
                collectionItemInfo = tableCellInfo(rowIndex = rowIndex, columnIndex = columnIndex)
            }
            .padding(padding),
        contentAlignment = Alignment.CenterStart
    ) {
        val custom = column.cell
        if (custom != null) {
            custom(item)
        } else {
            Text(
                text = column.value(item)?.toString().orEmpty(),
                variant = TextVariant.BodyMedium,
                color = TextColor.Base,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Row index the header takes in the table's collection semantics. */
internal const val TableHeaderRowIndex = 0

/**
 * Row index a body row takes in the table's collection semantics, counting the header as row `0`.
 *
 * @param itemIndex Position of the row among the body rows.
 * @return Position of the row in the table.
 */
internal fun tableBodyRowIndex(itemIndex: Int): Int = itemIndex + 1

/**
 * Number of rows a table announces: the body rows and the header.
 *
 * @param itemCount Number of body rows.
 * @return Rows in the table's collection semantics.
 */
internal fun tableRowCount(itemCount: Int): Int = itemCount + 1

/**
 * Position of one cell in the table's collection semantics.
 *
 * @param rowIndex Position of the cell's row, the header being row `0`.
 * @param columnIndex Position of the cell's column.
 * @return Item info spanning one row and one column.
 */
private fun tableCellInfo(rowIndex: Int, columnIndex: Int): CollectionItemInfo =
    CollectionItemInfo(rowIndex = rowIndex, rowSpan = 1, columnIndex = columnIndex, columnSpan = 1)

/**
 * Announces the table as a grid of the header and its body rows.
 *
 * @param rowCount Number of body rows.
 * @param columnCount Number of columns.
 * @return Modifier carrying the collection semantics.
 */
private fun Modifier.tableSemantics(rowCount: Int, columnCount: Int): Modifier = semantics {
    collectionInfo = CollectionInfo(rowCount = tableRowCount(rowCount), columnCount = columnCount)
}

/**
 * Draws a rule along the node's top or bottom edge, over its content.
 *
 * Drawn rather than laid out, so a rule adds no height and the rows of a lazy table never shift
 * when one appears.
 *
 * @param color Color of the rule. [Color.Unspecified] draws none.
 * @param thickness Thickness of the rule; at least one pixel.
 * @param atTop Whether the rule runs along the top edge rather than the bottom one.
 * @return Modifier that draws the rule.
 */
private fun Modifier.tableRule(color: Color, thickness: Dp, atTop: Boolean): Modifier = drawWithContent {
    drawContent()
    if (color == Color.Unspecified) return@drawWithContent

    val ruleHeight = thickness.toPx().coerceAtLeast(1f)
    drawRect(
        color = color,
        topLeft = Offset(0f, if (atTop) 0f else size.height - ruleHeight),
        size = Size(size.width, ruleHeight),
    )
}

/**
 * Sizes a cell to its column's resolved width and reports its natural width.
 *
 * A fixed column measures its content at exactly [TableColumn.width]. An auto column measures its
 * content at its natural width, up to [TableColumn.maxWidth], records that width so the table can
 * resolve the column, and then takes the resolved width, so every row lines up. Until a width is
 * resolved, the cell takes its natural width.
 *
 * @param columnIndex Position of the column in the table.
 * @param column Column the cell belongs to.
 * @param channel Whether the cell is a header or a body cell.
 * @param tableState State of the enclosing table.
 * @return Modifier that measures and sizes the cell.
 */
private fun Modifier.tableColumnWidth(
    columnIndex: Int,
    column: TableColumn<*>,
    channel: TableMeasurementState.Channel,
    tableState: TableState,
): Modifier = layout { measurable, constraints ->
    val fixedPx = column.width?.roundToPx()
    val contentConstraints = if (fixedPx != null) {
        constraints.copy(minWidth = fixedPx, maxWidth = fixedPx)
    } else {
        constraints.copy(minWidth = 0, maxWidth = column.maxWidth?.roundToPx() ?: Constraints.Infinity)
    }
    val placeable = measurable.measure(contentConstraints)
    if (fixedPx == null) {
        tableState.measurement.onMeasured(px = placeable.width, columnId = column.id, channel = channel)
    }
    val width = tableState.columnWidthsPx.getOrNull(columnIndex) ?: placeable.width

    layout(width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}
