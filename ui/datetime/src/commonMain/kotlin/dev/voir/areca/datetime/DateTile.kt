package dev.voir.areca.datetime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.LocalDate

/**
 * A date as a small tile: the month over the day — "Nov" over "22" — for the leading visual of a
 * row about something on that date, such as an expected payment.
 *
 * Assistive technology reads [contentDescription], or the whole date as
 * [CalendarLabels.dayDescription] words it — "Sunday 22 November 2026" — rather than the clipped
 * "Nov 22" it draws.
 *
 * @param date The date.
 * @param modifier Modifier applied to the tile.
 * @param contentDescription What assistive technology reads, or `null` for the whole date.
 * @param labels Words of the tile: [CalendarLabels.shortMonth] and [CalendarLabels.dayNumber] drawn,
 *   [CalendarLabels.dayDescription] read aloud. Pass the application's own, as for a calendar.
 * @param style Colours. Defaults to [LocalDateTileStyle], or [DateTileStyle.Default].
 * @param size Size, shape and type. Defaults to [LocalDateTileSize], or [DateTileSize.Default].
 */
@Composable
public fun DateTile(
    date: LocalDate,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    labels: CalendarLabels = CalendarLabels.English,
    style: DateTileStyle = LocalDateTileStyle.current ?: DateTileStyle.Default,
    size: DateTileSize = LocalDateTileSize.current ?: DateTileSize.Default,
) {
    val month = labels.shortMonth(date.month)
    val day = labels.dayNumber(date.day)
    Column(
        modifier = modifier
            .size(size.size)
            .background(style.bg, size.shape)
            .clearAndSetSemantics { this.contentDescription = contentDescription ?: labels.dayDescription(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = month, style = size.monthTextStyle, color = style.month, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = day, style = size.dayTextStyle, color = style.day, maxLines = 1)
    }
}

/**
 * Colours of a [DateTile].
 *
 * @property bg Fill of the tile.
 * @property month The month line.
 * @property day The day line.
 */
@Immutable
public data class DateTileStyle(
    public val bg: Color,
    public val month: Color,
    public val day: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a soft tile, the day in the primary ink and the month in the secondary. */
        public val Default: DateTileStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the tile.
         * @param month The month line.
         * @param day The day line.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSoft,
            month: Color = ArecaTheme.colors.fgSubtle,
            day: Color = ArecaTheme.colors.fgBase,
        ): DateTileStyle = DateTileStyle(bg = bg, month = month, day = day)
    }
}

/**
 * Size, shape and type of a [DateTile].
 *
 * @property size Side of the square tile.
 * @property shape Shape of the tile.
 * @property monthTextStyle Type of the month line.
 * @property dayTextStyle Type of the day line.
 */
@Immutable
public data class DateTileSize(
    public val size: Dp,
    public val shape: Shape,
    public val monthTextStyle: TextStyle,
    public val dayTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** A row's leading visual, a little larger than a settings tile. */
        public val Default: DateTileSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param size Side of the tile.
         * @param shape Shape of the tile.
         * @param monthTextStyle Type of the month line.
         * @param dayTextStyle Type of the day line.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            size: Dp = 46.dp,
            shape: Shape = ArecaTheme.shapes.lg,
            monthTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            dayTextStyle: TextStyle = ArecaTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        ): DateTileSize = DateTileSize(size = size, shape = shape, monthTextStyle = monthTextStyle, dayTextStyle = dayTextStyle)
    }
}

/**
 * Default [DateTileStyle] in scope, or `null` to use [DateTileStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalDateTileStyle: ProvidableCompositionLocal<DateTileStyle?> = compositionLocalOf { null }

/**
 * Default [DateTileSize] in scope, or `null` to use [DateTileSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalDateTileSize: ProvidableCompositionLocal<DateTileSize?> = compositionLocalOf { null }
