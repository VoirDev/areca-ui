package dev.voir.areca.foundation.slot

/**
 * How a [SlotRow] turns its three [SlotSize] rules into positions.
 *
 * The two arrangements answer different questions. [Sequential] asks "how wide is each slot?" and
 * lays the answers out one after another — which is what a list row, a button and a badge want.
 * [Centered] asks "where is the middle?" and works backwards from the row's centre line, which is
 * what a heading bar wants and what no combination of width rules can express: a slot cannot be
 * both as wide as its content and positioned by the width of the slot opposite it.
 */
public enum class SlotRowArrangement {
    /**
     * Each slot takes the width its [SlotSize] asks for, laid out leading to trailing.
     *
     * The middle slot's position therefore depends on how wide the leading slot is, so a row whose
     * leading content varies has a middle that moves with it.
     */
    Sequential,

    /**
     * The side slots take their content's width and the middle slot is placed on the row's centre
     * line — as long as it fits there.
     *
     * "As long as it fits" is the whole point. Centring is done by reserving the *wider* of the two
     * sides on both sides of the middle, never by shrinking a side to a share of the row, so a
     * trailing control keeps its full width whatever the middle holds. When the middle needs more
     * room than the symmetric band leaves, the row gives up centring rather than the controls: the
     * middle takes the space between the two sides and truncates there.
     *
     * The side slots' [SlotSize.Weight] rules are meaningless here and are treated as
     * [SlotSize.Wrap]; [SlotSize.Fixed] is still honoured. The middle slot always spans the band
     * that centring computes, so its content is positioned by
     * [SlotRowLayout.centerAlignment] — use [androidx.compose.ui.Alignment.Center], as
     * [SlotRowLayout.Centered] does.
     *
     * The middle slot is asked for its intrinsic width to decide whether it fits, so it has to be a
     * subtree that can answer: text, icons, rows and columns can, a lazy list cannot.
     */
    Centered,
}
