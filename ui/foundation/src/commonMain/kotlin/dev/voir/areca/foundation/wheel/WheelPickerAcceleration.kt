package dev.voir.areca.foundation.wheel

import androidx.compose.runtime.Immutable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * How much harder a wheel turns when it is scrolled harder.
 *
 * Without this a wheel has one speed, and that speed cannot be right for both things a wheel is
 * asked to do: nudging one row along needs a wheel that answers gently, and crossing two centuries
 * of years needs one that moves. Pick a single number and a long column is either a blur under the
 * smallest flick or a wade from end to end.
 *
 * So the gain is taken from how long the scrolling has been *going on*, not from how fast the events
 * are arriving. Duration is the one measure of intent that means the same thing on every device: a
 * notch of a stepped mouse is an instant however hard it is flicked, while a gesture that has been
 * running for half a second is somebody travelling. A rate would not do — a trackpad reports
 * fractional scroll units at over a hundred a second, so even a careful drag looks frantic beside a
 * mouse that reports one unit per notch, and a wheel tuned on one device would be unusable on the
 * other.
 *
 * The gain rises with the square of the elapsed fraction, so the first part of a gesture barely
 * accelerates at all — which is what keeps aiming precise right up to the point where the gesture is
 * obviously a long one.
 *
 * Each gesture starts from rest, so a short scroll is never sped up by a long one that came before
 * it, and a single notch is never sped up at all.
 *
 * This deliberately replaces rather than compounds the acceleration the operating system applies:
 * [WheelPickerScroll.maxRowsPerEvent] flattens the driver's own curve, which is measured in document
 * pixels and knows nothing of rows, and this puts back a curve that is measured in rows.
 *
 * @property maxGain Most the wheel may be sped up by. `1f` turns acceleration off, which is what
 *   [None] is.
 * @property windUp How long a gesture must be sustained to reach [maxGain]. Shorter makes a wheel
 *   eager to run; longer keeps it aimable for longer before it starts to travel.
 */
@Immutable
public data class WheelPickerAcceleration(
    public val maxGain: Float = 3f,
    public val windUp: Duration = 600.milliseconds,
) {
    init {
        require(maxGain >= 1f) { "maxGain must be at least 1, was $maxGain" }
        require(windUp.isPositive()) { "windUp must be positive, was $windUp" }
    }

    /** Named acceleration curves. */
    public companion object {
        /** Theme default: up to three times the base pace, over a gesture of about half a second. */
        public val Default: WheelPickerAcceleration = WheelPickerAcceleration()

        /** One speed, whatever the gesture. For a short wheel, where there is nowhere to hurry to. */
        public val None: WheelPickerAcceleration = WheelPickerAcceleration(maxGain = 1f)

        /**
         * Winds up further and sooner, for a column long enough that its ends are a journey — the
         * years of a date of birth.
         */
        public val Long: WheelPickerAcceleration = WheelPickerAcceleration(
            maxGain = 4f,
            windUp = 700.milliseconds,
        )
    }
}
