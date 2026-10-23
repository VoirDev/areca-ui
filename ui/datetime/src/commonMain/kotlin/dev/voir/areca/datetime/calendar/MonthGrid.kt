package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

/**
 * Months as tiles, a year to a block — for picking a whole month at a glance, with a figure under
 * each, rather than scrolling a calendar to it.
 *
 * Each year in [years] gets a heading and its twelve months in rows of [columns]. A month tile
 * shows its name and, when [content] is given, a second line beneath it — a month's total, "this"
 * for the current one. The grid holds no selection: it draws [selected] and reports taps through
 * [onSelect]. A month [enabled] refuses is drawn flat and takes no tap — the current month, the
 * months still to come.
 *
 * Assistive technology reads each tile as a radio button in one selectable group: the month as
 * [CalendarLabels.monthHeader] words it, followed by the tile's [content], so a month's figure is
 * read with it. Year headings are headings.
 *
 * @param years Years to show, in order — usually the latest first.
 * @param selected The chosen month, or `null` for none.
 * @param onSelect Called with a month when it is tapped. Never called for a refused month or the
 *   selected one.
 * @param modifier Modifier applied to the grid, which fills the width it is given.
 * @param enabled Whether a month can be chosen.
 * @param columns How many months sit side by side. At least `1`.
 * @param labels Words of the grid: [CalendarLabels.year] for the headings,
 *   [CalendarLabels.shortMonth] on the tiles and [CalendarLabels.monthHeader] read aloud. Pass the
 *   application's own, as for a [Calendar].
 * @param style Colours. Defaults to [LocalMonthGridStyle], or [MonthGridStyle.Default].
 * @param size Tile size, gaps and type. Defaults to [LocalMonthGridSize], or [MonthGridSize.Default].
 * @param content The second line of a month's tile, given its month. It inherits the tile's ink and
 *   [MonthGridSize.contentTextStyle]. `null` for tiles with the name alone.
 * @throws IllegalArgumentException When [columns] is below `1`.
 */
@Composable
public fun MonthGrid(
    years: List<Int>,
    selected: YearMonth?,
    onSelect: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    enabled: (YearMonth) -> Boolean = { true },
    columns: Int = 4,
    labels: CalendarLabels = CalendarLabels.English,
    style: MonthGridStyle = LocalMonthGridStyle.current ?: MonthGridStyle.Default,
    size: MonthGridSize = LocalMonthGridSize.current ?: MonthGridSize.Default,
    content: (@Composable (YearMonth) -> Unit)? = null,
) {
    require(columns >= 1) { "A month grid needs at least one column, was $columns." }
    Column(
        modifier = modifier.fillMaxWidth().selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(size.yearGap),
    ) {
        years.forEach { year ->
            Column(verticalArrangement = Arrangement.spacedBy(size.headingGap)) {
                Text(
                    text = labels.year(year),
                    style = size.yearTextStyle,
                    color = style.year,
                    modifier = Modifier.semantics { heading() },
                )
                Column(verticalArrangement = Arrangement.spacedBy(size.tileGap)) {
                    Month.entries.chunked(columns).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(size.tileGap)) {
                            row.forEach { month ->
                                val ym = YearMonth(year, month)
                                MonthTile(
                                    month = ym,
                                    label = labels.shortMonth(month),
                                    description = labels.monthHeader(ym),
                                    selected = ym == selected,
                                    enabled = enabled(ym),
                                    onSelect = { onSelect(ym) },
                                    style = style,
                                    size = size,
                                    content = content,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            // Keeps a short last row's tiles the width of the full rows above it.
                            repeat(columns - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
    }
}

/** One month's tile. */
@Composable
private fun MonthTile(
    month: YearMonth,
    label: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
    style: MonthGridStyle,
    size: MonthGridSize,
    content: (@Composable (YearMonth) -> Unit)?,
    modifier: Modifier,
) {
    val (bg, ink) = when {
        selected -> style.bgSelected to style.contentSelected
        !enabled -> style.bgDisabled to style.contentDisabled
        else -> style.bg to style.content
    }
    Column(
        modifier = modifier
            .height(size.tileHeight)
            .background(bg, size.tileShape)
            .selectable(
                selected = selected,
                enabled = enabled,
                interactionSource = null,
                indication = TouchIndication(shape = size.tileShape),
                role = Role.RadioButton,
                onClick = { if (!selected) onSelect() },
            )
            .clickCursor(enabled)
            .semantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            style = if (selected) size.labelSelectedTextStyle else size.labelTextStyle,
            color = ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clearAndSetSemantics {},
        )
        if (content != null) {
            // Left in the tree, so the tile reads its figure after the month.
            CompositionLocalProvider(LocalContentColor provides ink, LocalTextStyle provides size.contentTextStyle) {
                Column { content(month) }
            }
        }
    }
}

/**
 * Colours of a [MonthGrid].
 *
 * @property year A year's heading.
 * @property bg Fill of a month that can be chosen.
 * @property content Ink of a month that can be chosen.
 * @property bgSelected Fill of the chosen month.
 * @property contentSelected Ink of the chosen month.
 * @property bgDisabled Fill of a refused month.
 * @property contentDisabled Ink of a refused month.
 */
@Immutable
public data class MonthGridStyle(
    public val year: Color,
    public val bg: Color,
    public val content: Color,
    public val bgSelected: Color,
    public val contentSelected: Color,
    public val bgDisabled: Color,
    public val contentDisabled: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: subtle tiles, the chosen one inverted, refused ones barely set off. */
        public val Default: MonthGridStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param year A year's heading.
         * @param bg Fill of a month that can be chosen.
         * @param content Ink of a month that can be chosen.
         * @param bgSelected Fill of the chosen month.
         * @param contentSelected Ink of the chosen month.
         * @param bgDisabled Fill of a refused month.
         * @param contentDisabled Ink of a refused month.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            year: Color = ArecaTheme.colors.fgBase,
            bg: Color = ArecaTheme.colors.bgSubtle,
            content: Color = ArecaTheme.colors.fgBase,
            bgSelected: Color = ArecaTheme.colors.bgContrast,
            contentSelected: Color = ArecaTheme.colors.fgOnContrast,
            bgDisabled: Color = ArecaTheme.colors.bgSoft,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
        ): MonthGridStyle = MonthGridStyle(
            year = year,
            bg = bg,
            content = content,
            bgSelected = bgSelected,
            contentSelected = contentSelected,
            bgDisabled = bgDisabled,
            contentDisabled = contentDisabled,
        )
    }
}

/**
 * Tile size, gaps and type of a [MonthGrid].
 *
 * @property tileHeight Height of a month tile.
 * @property tileShape Shape of a month tile.
 * @property tileGap Space between tiles, across and down.
 * @property headingGap Space between a year's heading and its tiles.
 * @property yearGap Space between one year's block and the next.
 * @property yearTextStyle Type of a year's heading.
 * @property labelTextStyle Type of a month's name.
 * @property labelSelectedTextStyle Type of the chosen month's name.
 * @property contentTextStyle Type the second line inherits.
 */
@Immutable
public data class MonthGridSize(
    public val tileHeight: Dp,
    public val tileShape: Shape,
    public val tileGap: Dp,
    public val headingGap: Dp,
    public val yearGap: Dp,
    public val yearTextStyle: TextStyle,
    public val labelTextStyle: TextStyle,
    public val labelSelectedTextStyle: TextStyle,
    public val contentTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default. */
        public val Default: MonthGridSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param tileHeight Height of a month tile.
         * @param tileShape Shape of a month tile.
         * @param tileGap Space between tiles.
         * @param headingGap Space under a year's heading.
         * @param yearGap Space between years.
         * @param yearTextStyle Type of a year's heading.
         * @param labelTextStyle Type of a month's name.
         * @param labelSelectedTextStyle Type of the chosen month's name.
         * @param contentTextStyle Type of the second line.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            tileHeight: Dp = 52.dp,
            tileShape: Shape = ArecaTheme.shapes.md,
            tileGap: Dp = ArecaTheme.spacing.sm,
            headingGap: Dp = ArecaTheme.spacing.md,
            yearGap: Dp = ArecaTheme.spacing.xl,
            yearTextStyle: TextStyle = ArecaTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            labelTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            labelSelectedTextStyle: TextStyle = ArecaTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            contentTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
        ): MonthGridSize = MonthGridSize(
            tileHeight = tileHeight,
            tileShape = tileShape,
            tileGap = tileGap,
            headingGap = headingGap,
            yearGap = yearGap,
            yearTextStyle = yearTextStyle,
            labelTextStyle = labelTextStyle,
            labelSelectedTextStyle = labelSelectedTextStyle,
            contentTextStyle = contentTextStyle,
        )
    }
}

/**
 * Default [MonthGridStyle] in scope, or `null` to use [MonthGridStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalMonthGridStyle: ProvidableCompositionLocal<MonthGridStyle?> = compositionLocalOf { null }

/**
 * Default [MonthGridSize] in scope, or `null` to use [MonthGridSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalMonthGridSize: ProvidableCompositionLocal<MonthGridSize?> = compositionLocalOf { null }
