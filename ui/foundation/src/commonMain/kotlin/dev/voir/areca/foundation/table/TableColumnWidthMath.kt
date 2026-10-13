package dev.voir.areca.foundation.table

/**
 * Width policy of a [TableColumn], in pixels, as [TableColumnWidthMath.resolve] consumes it.
 *
 * @property fixedPx Exact width of a fixed column, or `null` for an auto column.
 * @property minPx Smallest width of an auto column.
 * @property maxPx Largest width of an auto column, or `null` when it is unbounded.
 * @property expandWeight Share of leftover viewport space an auto column takes; `0` or less takes none.
 * @property naturalPx Widest content measured in the column so far, including cell padding; `0`
 *   before anything has been measured.
 */
internal data class TableColumnWidthSpec(
    val fixedPx: Int?,
    val minPx: Int,
    val maxPx: Int?,
    val expandWeight: Float,
    val naturalPx: Int,
)

/**
 * The column-width arithmetic behind [Table] and [LazyTable], kept apart from the Compose plumbing.
 *
 * Separated so the width policy can be stated and tested as a plain function, as
 * [dev.voir.areca.foundation.topbar.TopBarScrollMath] is for the top bar.
 */
internal object TableColumnWidthMath {
    /**
     * Resolves the width of every column.
     *
     * A fixed column takes its [TableColumnWidthSpec.fixedPx]. An auto column starts at its natural
     * width clamped to its min and max, the max winning when the two conflict. When the columns are
     * narrower than [viewportPx] together, the leftover is shared among auto columns by
     * [TableColumnWidthSpec.expandWeight], never past a column's max; a capped column's share goes to
     * the others. When they are wider, nothing shrinks and the table scrolls horizontally.
     *
     * @param specs Width policy of each column, in display order.
     * @param viewportPx Width the table is shown in; `0` or less shares no leftover.
     * @return Width of each column in pixels, in the order of [specs].
     */
    fun resolve(specs: List<TableColumnWidthSpec>, viewportPx: Int): List<Int> {
        val widths = specs.map { spec -> spec.fixedPx ?: spec.clampedNaturalPx() }.toMutableList()

        var leftover = viewportPx - widths.sum()
        val growable = specs.indices
            .filter { i -> specs[i].fixedPx == null && specs[i].expandWeight > 0f && !specs[i].isAtMax(widths[i]) }
            .toMutableList()

        while (leftover > 0 && growable.isNotEmpty()) {
            val totalWeight = growable.sumOf { i -> specs[i].expandWeight.toDouble() }
            var assigned = 0
            var added = 0
            val capped = mutableListOf<Int>()

            growable.forEachIndexed { position, i ->
                // The last column takes the rounding remainder so the shares add up to the leftover.
                val share = if (position == growable.lastIndex) {
                    leftover - assigned
                } else {
                    (leftover * specs[i].expandWeight / totalWeight).toInt()
                }
                assigned += share

                val room = specs[i].maxPx?.let { max -> max - widths[i] } ?: Int.MAX_VALUE
                val add = minOf(share, room)
                widths[i] += add
                added += add
                if (add == room) capped += i
            }

            leftover -= added
            if (capped.isEmpty()) break
            growable.removeAll(capped)
        }

        return widths
    }

    /**
     * Natural width clamped to the column's bounds, the max winning over the min.
     *
     * @return Starting width of an auto column.
     */
    private fun TableColumnWidthSpec.clampedNaturalPx(): Int {
        val atLeastMin = naturalPx.coerceAtLeast(minPx)
        return maxPx?.let { max -> atLeastMin.coerceAtMost(max) } ?: atLeastMin
    }

    /**
     * Whether a column at [widthPx] has no room left to grow.
     *
     * @param widthPx Current width of the column.
     * @return `true` when the column has a max and has reached it.
     */
    private fun TableColumnWidthSpec.isAtMax(widthPx: Int): Boolean =
        maxPx != null && widthPx >= maxPx
}
