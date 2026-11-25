package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.yearMonth

/**
 * The [CalendarLevel.Months] or [CalendarLevel.Years] body of a [Calendar]: twelve tiles in three
 * columns of four rows, filling the height the day grid would take.
 *
 * A tap is the choice. A month tile shows its days, a year tile shows its months, and the tile
 * already shown returns to the days unchanged. The tiles are one radio group to assistive
 * technology, and the arrow keys move focus between them.
 *
 * Month tiles draw the calendar's `monthMarkers` under their names, in the same row and colours as a
 * day's marker.
 *
 * @param scope Calendar scope, for the month markers.
 * @param state Calendar whose level, browsed year and bounds the tiles show.
 * @param level [CalendarLevel.Months] or [CalendarLevel.Years].
 * @param labels Month and year text.
 * @param style Tile colors.
 * @param size Tile spacing, shape and type.
 * @param modifier Modifier giving the tiles their height and inset.
 */
@Composable
internal fun PeriodTiles(
    scope: CalendarScope,
    state: CalendarState,
    level: CalendarLevel,
    labels: CalendarLabels,
    style: CalendarStyle,
    size: CalendarSize,
    modifier: Modifier = Modifier,
) {
    val tiles: List<PeriodTile> = when (level) {
        CalendarLevel.Months -> (1..MONTHS_IN_YEAR).map { number ->
            val month = YearMonth(state.browsedYear, number)
            val marker = scope.monthMarkers?.invoke(month)
            PeriodTile(
                label = labels.shortMonth(Month(number)),
                description = labels.monthHeader(month).withMarker(marker),
                marker = marker,
                hasMarkerRow = scope.monthMarkers != null,
                selected = month == state.visibleMonth,
                current = month == state.today.yearMonth,
                enabled = month in state.bounds,
                onClick = {
                    if (month == state.visibleMonth) state.showDays() else state.selectMonth(month)
                },
            )
        }
        else -> state.browsedYears.map { year ->
            PeriodTile(
                label = labels.year(year),
                description = labels.year(year),
                marker = null,
                hasMarkerRow = false,
                selected = year == state.browsedYear,
                current = year == state.today.year,
                enabled = year in state.bounds.yearRange,
                onClick = { state.selectYear(year) },
            )
        }
    }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                val direction = when (event.key) {
                    Key.DirectionLeft -> FocusDirection.Left
                    Key.DirectionRight -> FocusDirection.Right
                    Key.DirectionUp -> FocusDirection.Up
                    Key.DirectionDown -> FocusDirection.Down
                    else -> return@onKeyEvent false
                }
                focusManager.moveFocus(direction)
            },
        verticalArrangement = Arrangement.spacedBy(size.tileSpacing),
    ) {
        tiles.chunked(TILE_COLUMNS).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(size.tileSpacing),
            ) {
                row.forEach { tile ->
                    PeriodTileCell(
                        tile = tile,
                        labels = labels,
                        style = style,
                        size = size,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            }
        }
    }
}

/**
 * One month or year tile.
 *
 * @param tile What the tile shows and does.
 * @param labels Digits for a count marker.
 * @param style Tile colors.
 * @param size Tile shape and type.
 * @param modifier Modifier giving the tile its share of the row.
 */
@Composable
private fun PeriodTileCell(
    tile: PeriodTile,
    labels: CalendarLabels,
    style: CalendarStyle,
    size: CalendarSize,
    modifier: Modifier,
) {
    val colors = style.resolveTile(selected = tile.selected, current = tile.current, enabled = tile.enabled)
    Box(
        modifier = modifier
            .background(colors.bg, size.tileShape)
            .border(width = size.dayBorderWidth, color = colors.border, shape = size.tileShape)
            .selectable(
                selected = tile.selected,
                enabled = tile.enabled,
                interactionSource = null,
                indication = TouchIndication(shape = size.tileShape),
                role = Role.RadioButton,
                onClick = tile.onClick,
            )
            .clickCursor(tile.enabled)
            .semantics { contentDescription = tile.description },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = tile.label,
                style = size.tileTextStyle,
                color = colors.content,
                maxLines = 1,
                modifier = Modifier.clearAndSetSemantics { },
            )
            if (tile.hasMarkerRow) {
                MarkerLane(
                    marker = tile.marker,
                    onFill = tile.selected && tile.enabled,
                    fillContent = colors.content,
                    enabled = tile.enabled,
                    labels = labels,
                    style = style,
                    size = size,
                )
            }
        }
    }
}

/**
 * Everything one tile needs.
 *
 * @property label Text drawn on the tile.
 * @property description What assistive technology reads.
 * @property selected Whether the tile is the month or year being shown.
 * @property current Whether the tile holds today.
 * @property enabled Whether the tile lies inside the bounds.
 * @property marker What the tile shows under its label, or `null`.
 * @property hasMarkerRow Whether the tile keeps a row under its label for a marker.
 * @property onClick What a tap does.
 */
private class PeriodTile(
    val label: String,
    val description: String,
    val selected: Boolean,
    val current: Boolean,
    val enabled: Boolean,
    val marker: CalendarMarker?,
    val hasMarkerRow: Boolean,
    val onClick: () -> Unit,
)

/** Columns of month and year tiles; with [YEARS_PER_PAGE] and twelve months that is four rows. */
private const val TILE_COLUMNS: Int = 3

/** Months in a year. */
private const val MONTHS_IN_YEAR: Int = 12
