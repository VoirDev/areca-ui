package dev.voir.areca.foundation.slot

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * How a [SlotRow] divides its width and aligns what sits in it.
 *
 * Separating the arrangement from the content is what lets a button, a list row, a badge and a top
 * bar share one row implementation while looking nothing alike: each supplies the same three slots
 * and a different layout.
 *
 * @property start Width rule for the leading slot.
 * @property startAlignment Where the leading slot's content sits within its own box.
 * @property center Width rule for the middle slot. Weighted by default, so it absorbs the space the
 *   other two do not take. Ignored under [SlotRowArrangement.Centered], which computes the middle
 *   slot's width itself.
 * @property centerAlignment Where the middle slot's content sits within its own box.
 * @property end Width rule for the trailing slot.
 * @property endAlignment Where the trailing slot's content sits within its own box.
 * @property gap Space on both sides of the middle slot, unless overridden per side.
 * @property startGap Space between the leading and middle slots. `null` uses [gap]. Drawn only when
 *   there is a leading slot, or when [start] is [SlotSize.Fixed].
 * @property endGap Space between the middle and trailing slots. `null` uses [gap]. Drawn only when
 *   there is a trailing slot, or when [end] is [SlotSize.Fixed].
 * @property minHeight Smallest height the row will take, which is what gives a row of short content
 *   a consistent tap target. [Dp.Unspecified] leaves the height to the content.
 * @property verticalAlignment How the three slots line up against each other vertically. Content
 *   *within* a slot is placed by that slot's own alignment instead.
 * @property arrangement Whether the slots are laid out one after another or around the row's centre
 *   line.
 * @property alignment How the side slots line up with a middle slot of text that may run to several
 *   lines — see [SlotRowAlignment] for which scenario takes which. `null` leaves the slots to
 *   [verticalAlignment], which is what a button or a bar wants. Applies under
 *   [SlotRowArrangement.Sequential] only.
 */
@Immutable
public data class SlotRowLayout(
    public val start: SlotSize = SlotSize.Wrap,
    public val startAlignment: Alignment = Alignment.CenterStart,
    public val center: SlotSize = SlotSize.Weight(1f),
    public val centerAlignment: Alignment = Alignment.CenterStart,
    public val end: SlotSize = SlotSize.Wrap,
    public val endAlignment: Alignment = Alignment.CenterEnd,
    public val gap: Dp = 0.dp,
    public val startGap: Dp? = null,
    public val endGap: Dp? = null,
    public val minHeight: Dp = Dp.Unspecified,
    public val verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    public val arrangement: SlotRowArrangement = SlotRowArrangement.Sequential,
    public val alignment: SlotRowAlignment? = null,
) {
    /**
     * Space to draw between the leading and middle slots.
     *
     * Resolved on read rather than at construction, so `copy(gap = …)` moves both gaps as a caller
     * would expect — a default evaluated in the constructor would not be re-run by `copy`.
     */
    public val resolvedStartGap: Dp get() = startGap ?: gap

    /** Space to draw between the middle and trailing slots. See [resolvedStartGap]. */
    public val resolvedEndGap: Dp get() = endGap ?: gap

    /** The common slot arrangements, and a builder for rows whose slots all wrap. */
    public companion object {
        /**
         * Leading and trailing slots wrap their content; the middle slot takes the rest.
         *
         * The list-row arrangement, and what most rows want.
         */
        public val Default: SlotRowLayout = SlotRowLayout()

        /**
         * [Default], with every slot centred on the row: [SlotRowAlignment.Center].
         *
         * A row of one or two lines — a switch, a person with a handle, an account with its
         * balance. Unlike [Default], it centres the group within [minHeight] the same way the
         * other aligned layouts do, so rows built on any of them line up.
         */
        public val Center: SlotRowLayout = SlotRowLayout(alignment = SlotRowAlignment.Center)

        /**
         * [Default], with the visual and the text starting at the top: [SlotRowAlignment.Top].
         *
         * A row of three lines or more, whose trailing control still keeps level with its visual.
         */
        public val Top: SlotRowLayout = SlotRowLayout(alignment = SlotRowAlignment.Top)

        /**
         * [Default], with the side slots centred on the middle slot's first line:
         * [SlotRowAlignment.FirstLine]. For a marker no taller than a line — a legend dot, a bullet.
         */
        public val FirstLine: SlotRowLayout = SlotRowLayout(alignment = SlotRowAlignment.FirstLine)

        /**
         * [Center] for up to about two lines of text, [Top] beyond: [SlotRowAlignment.Auto].
         *
         * The list-row arrangement. Every [dev.voir.areca.foundation.list.ListItem] uses it unless
         * given another layout; a row of your own gets the same by reporting its title with
         * [firstLineCenter].
         */
        public val Auto: SlotRowLayout = SlotRowLayout(alignment = SlotRowAlignment.Auto)

        /**
         * The middle slot holds the row's centre line, and the side slots keep their own width.
         *
         * The heading-bar arrangement: a title between a back control and an action. Centring is
         * paid for out of the middle slot, never out of the controls — when the title cannot fit on
         * the centre line it spreads into the space between them and truncates, instead of forcing
         * the controls to shrink.
         *
         * Prefer this over [Fill] for anything with a title. [Fill] centres by making all three
         * slots the same width, which on a narrow screen leaves a text action about a third of the
         * bar and clips it.
         */
        public val Centered: SlotRowLayout = SlotRowLayout(
            centerAlignment = Alignment.Center,
            arrangement = SlotRowArrangement.Centered,
        )

        /**
         * All three slots take an equal third, with the middle one centred.
         *
         * A layout of three columns, and only useful when the equal columns are the point — three
         * balanced statistics, say. It centres the middle slot only while both side slots fit in a
         * third of the row; past that it cuts them off. Use [Centered] for a title bar.
         */
        public val Fill: SlotRowLayout = SlotRowLayout(
            start = SlotSize.Weight(1f),
            center = SlotSize.Weight(1f),
            end = SlotSize.Weight(1f),
            centerAlignment = Alignment.Center,
        )

        /**
         * Every slot wraps its content, so the row is only as wide as what it holds.
         *
         * The arrangement for a control rather than a row: a button's icon, label and spinner sit
         * together in the middle instead of being pushed to the edges.
         *
         * @param gap Space on both sides of the middle slot.
         * @param startGap Space before the middle slot. `null` uses [gap].
         * @param endGap Space after the middle slot. `null` uses [gap].
         * @param minHeight Smallest height the row will take.
         * @return Layout whose three slots all wrap, centred together.
         */
        public fun wrap(
            gap: Dp = 0.dp,
            startGap: Dp? = null,
            endGap: Dp? = null,
            minHeight: Dp = Dp.Unspecified,
        ): SlotRowLayout = SlotRowLayout(
            start = SlotSize.Wrap,
            center = SlotSize.Wrap,
            end = SlotSize.Wrap,
            gap = gap,
            startGap = startGap,
            endGap = endGap,
            minHeight = minHeight,
            centerAlignment = Alignment.Center,
        )
    }
}
