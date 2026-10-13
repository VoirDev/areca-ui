package dev.voir.areca.foundation.wheel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlin.time.Duration.Companion.milliseconds

/**
 * How hard a wheel answers the hand.
 *
 * Separate from [WheelPickerSize], which says only how big the wheel is. How fast it turns is not a
 * dimension, and it is the setting most likely to differ between two wheels of identical size: a
 * wheel of twelve months hides almost any pacing, because it reaches its ends before the gesture is
 * over, while a wheel of two centuries of years shows every bit of it.
 *
 * The two ceilings here are a *base* pace that [acceleration] multiplies according to how hard the
 * user is scrolling, so a wheel can be exact under a careful gesture and quick under a determined
 * one without either setting being a compromise.
 *
 * A column picks its own by default, from how many rows it holds — see [forItemCount]. Pass one
 * explicitly only when a wheel wants something other than what its length implies.
 *
 * @property rowsPerUnit Rows travelled per unit of scroll. One unit is one notch of a stepped wheel
 *   and a fraction of one on a precision device, so `1f` makes a notch advance exactly one row on
 *   every platform. Expressed in scroll units rather than pixels deliberately: the platform's own
 *   pixels-per-notch differs per operating system and, on Windows, scales with the height of the
 *   element being scrolled, which would make a tall picker turn faster than a short one.
 * @property maxRowsPerEvent Most a single scroll event may ask for. Desktop drivers accelerate — a
 *   quick flick arrives not as more events but as far larger ones, on a curve meant for throwing a
 *   document by the pixel. At the default a notch never reaches this and slow scrolling is
 *   untouched; only the flick is clipped.
 * @property acceleration How much harder the wheel turns when it is scrolled harder. This is what
 *   lets one setting serve both aiming at a row and crossing a long column; the two ceilings below
 *   are the *base* pace, which acceleration multiplies up to
 *   [WheelPickerAcceleration.maxGain] times.
 * @property maxRowsAhead Furthest ahead of the wheel a gesture may queue travel, before
 *   acceleration. This is the setting
 *   that decides how far a flick carries: a trackpad's momentum phase keeps emitting events after
 *   the fingers lift, and without a bound the wheel is left chasing a point hundreds of rows away.
 *   Bounded, the wheel turns while the gesture lasts and stops within this many rows of its end.
 * @property maxRowsPerSecond Top speed while scrolling, in rows per second, before acceleration.
 *   Caps how fast rows go by however large a gap has built up, so they stay readable rather than
 *   becoming a blur.
 * @property maxFlingRows Furthest a single throw of a finger may carry the wheel. Separate from
 *   [maxRowsAhead] because a throw is one decisive gesture, where scrolling is a stream of small
 *   ones.
 */
@Immutable
public data class WheelPickerScroll(
    public val rowsPerUnit: Float = 1f,
    public val maxRowsPerEvent: Float = 1f,
    public val maxRowsAhead: Float = 2f,
    public val maxRowsPerSecond: Float = 12f,
    public val acceleration: WheelPickerAcceleration = WheelPickerAcceleration.Default,
    public val maxFlingRows: Int = 12,
) {
    init {
        require(rowsPerUnit > 0f) { "rowsPerUnit must be positive, was $rowsPerUnit" }
        require(maxRowsPerEvent > 0f) { "maxRowsPerEvent must be positive, was $maxRowsPerEvent" }
        require(maxRowsAhead > 0f) { "maxRowsAhead must be positive, was $maxRowsAhead" }
        require(maxRowsPerSecond > 0f) {
            "maxRowsPerSecond must be positive, was $maxRowsPerSecond"
        }
        require(maxFlingRows >= 1) { "maxFlingRows must be at least 1, was $maxFlingRows" }
    }

    /** The paces a column chooses between by length, and a faster one it never chooses itself. */
    public companion object {
        /** Longest wheel still counted as short by [forItemCount]. */
        private const val SHORT_WHEEL_ROWS: Int = 16

        /** Longest wheel still counted as medium by [forItemCount]. */
        private const val MEDIUM_WHEEL_ROWS: Int = 80

        /**
         * Pace suited to a wheel of [itemCount] rows.
         *
         * What a wheel should do under the hand depends mostly on how much of it there is, and that
         * is something the component already knows — so by default a column asks this rather than
         * making the caller choose. A picker holding a wheel of twelve months beside a wheel of two
         * hundred years gets each paced for what it is, without either being named at the call site.
         *
         * The tiers differ in *shape*, not only in degree. A short wheel is all precision and has
         * nowhere to hurry to; a long one needs a gentler base than a short one, because a careless
         * nudge on two hundred rows is a lost place, and far more wind-up, because its ends are a
         * journey. Speed alone rising with length would give the worst of both.
         *
         * @param itemCount Rows the wheel holds.
         * @return Pace for a wheel that long.
         */
        public fun forItemCount(itemCount: Int): WheelPickerScroll = when {
            itemCount <= SHORT_WHEEL_ROWS -> Short
            itemCount <= MEDIUM_WHEEL_ROWS -> Medium
            else -> Long
        }

        /**
         * A wheel with few enough rows to take in at a glance — days of the week, AM and PM, a
         * handful of sizes.
         *
         * Barely accelerates, because there is nowhere to travel to: every row is a second away, and
         * the only thing that matters is landing on the right one.
         */
        public val Short: WheelPickerScroll = WheelPickerScroll(
            maxRowsAhead = 1.5f,
            maxRowsPerSecond = 10f,
            acceleration = WheelPickerAcceleration(maxGain = 1.5f),
            maxFlingRows = 6,
        )

        /** A wheel of a few dozen rows — hours, minutes, a list of countries. */
        public val Medium: WheelPickerScroll = WheelPickerScroll()

        /**
         * A wheel long enough that its ends are a journey — years of a date of birth.
         *
         * Gentler at rest than [Short], deliberately: on two hundred rows a careless nudge is a lost
         * place. What it has instead is room to wind up, so a gesture that keeps going crosses the
         * wheel while a short one still picks out a single row.
         */
        public val Long: WheelPickerScroll = WheelPickerScroll(
            maxRowsPerEvent = 0.5f,
            maxRowsAhead = 1f,
            maxRowsPerSecond = 5f,
            acceleration = WheelPickerAcceleration(maxGain = 6f, windUp = 700.milliseconds),
            maxFlingRows = 8,
        )

        /** Faster than any tier [forItemCount] chooses, for a wheel meant to be crossed quickly. */
        public val Brisk: WheelPickerScroll = WheelPickerScroll(
            maxRowsPerEvent = 2f,
            maxRowsAhead = 4f,
            maxRowsPerSecond = 28f,
            acceleration = WheelPickerAcceleration(maxGain = 2.5f),
            maxFlingRows = 24,
        )
    }
}

/**
 * Pace every wheel in scope should use, or `null` to let each choose from its own length.
 *
 * Null by default, and deliberately not seeded by [dev.voir.areca.theme.ArecaThemeProvider]: a
 * single pace provided for a whole application would override exactly the judgement
 * [WheelPickerScroll.forItemCount] exists to make, and would do it invisibly. Provide one only
 * around a subtree that genuinely wants every wheel in it to behave the same.
 */
public val LocalWheelPickerScroll: ProvidableCompositionLocal<WheelPickerScroll?> =
    compositionLocalOf { null }
