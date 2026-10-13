package dev.voir.areca.foundation.table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalDensity

/**
 * Widest content measured in each auto column of one table, in pixels.
 *
 * Cells report their natural width as they are measured, and the table resolves column widths from
 * the widest seen so far. Measurements only grow, so a column never narrows while it scrolls past
 * shorter rows; a lazy table measures only the rows on screen, and a column that shrank and grew
 * with scrolling would make every row jump.
 *
 * Headers and body cells report on separate channels, so the two can be told apart when reading.
 */
@Stable
internal class TableMeasurementState {
    /** Widest width reported per column and channel. */
    private val measuredPx: SnapshotStateMap<Key, Int> = mutableStateMapOf()

    /** Which kind of cell a measurement came from. */
    enum class Channel {
        /** A header cell. */
        Header,

        /** A body cell. */
        Cell,
    }

    /**
     * Identifies the measurements of one channel of one column.
     *
     * @property columnId [TableColumn.id] of the column.
     * @property channel Kind of cell measured.
     */
    private data class Key(val columnId: String, val channel: Channel)

    /**
     * Records a cell's natural width, keeping the widest seen for its column and channel.
     *
     * @param px Natural width of the cell, including its padding, in pixels.
     * @param columnId [TableColumn.id] of the cell's column.
     * @param channel Kind of cell measured.
     */
    fun onMeasured(px: Int, columnId: String, channel: Channel) {
        val key = Key(columnId, channel)
        val previous = measuredPx[key] ?: 0
        if (px > previous) measuredPx[key] = px
    }

    /**
     * Widest content measured in a column across its header and body cells.
     *
     * @param columnId [TableColumn.id] of the column.
     * @return Width in pixels, or `null` before anything in the column has been measured.
     */
    fun naturalWidthPx(columnId: String): Int? =
        widestOf(Channel.entries.map { channel -> measuredPx[Key(columnId, channel)] })
}

/**
 * Widest of the measurements a column has, ignoring channels that have none.
 *
 * @param measurements Width per channel in pixels, `null` where a channel has not been measured.
 * @return The widest positive width, or `null` when none has been measured.
 */
internal fun widestOf(measurements: List<Int?>): Int? =
    measurements.filterNotNull().maxOrNull()?.takeIf { it > 0 }

/**
 * Remembers the measurements of one table.
 *
 * Measurements are pixels, so a change of density or font scale starts them over.
 *
 * @return State remembered for this position in the composition.
 */
@Composable
internal fun rememberTableMeasurementState(): TableMeasurementState {
    val density = LocalDensity.current
    return remember(density) { TableMeasurementState() }
}
