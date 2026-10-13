package dev.voir.areca.datetime.calendar

import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** How a day cell's color travels when it appears or disappears. */
class DayColorFadeTest {

    /** A pale brand tint, the color a range band and a chosen day are actually drawn in. */
    private val tint = Color(0xFFF0EDFF)

    /** Fractions through a fade, avoiding the two ends where every method agrees. */
    private val midway = listOf(0.25f, 0.5f, 0.75f)

    /**
     * Interpolates the way the animation does: to vectors, along each channel, and back.
     *
     * @param start Color at the beginning of the fade.
     * @param stop Color at the end of it.
     * @param fraction How far along to sample.
     * @return Color the animation would be showing at [fraction].
     */
    private fun fade(start: Color, stop: Color, fraction: Float): Color {
        val from = DayColorConverter.convertToVector(start)
        val to = DayColorConverter.convertToVector(stop)
        fun channel(a: Float, b: Float) = a + (b - a) * fraction
        return DayColorConverter.convertFromVector(
            AnimationVector4D(
                channel(from.v1, to.v1),
                channel(from.v2, to.v2),
                channel(from.v3, to.v3),
                channel(from.v4, to.v4),
            ),
        )
    }

    /**
     * Whether two channel values are equal to within the color format's own precision.
     *
     * A [Color] keeps eight bits a channel, so a half-way alpha comes back as `128/255` rather than
     * as `0.5`. The tolerance is one step of that ladder, which is the finest distinction the type
     * can hold and well under anything an eye would catch.
     *
     * @param expected Value wanted.
     * @param actual Value produced.
     * @return `true` when they agree to within one 255th.
     */
    private fun near(expected: Float, actual: Float) = abs(expected - actual) <= 1f / 255f

    @Test
    fun `a fade in from nothing is the colour itself at rising alpha`() {
        midway.forEach { fraction ->
            val color = fade(Color.Transparent, tint, fraction)

            assertTrue(near(tint.red, color.red), "red at $fraction was ${color.red}")
            assertTrue(near(tint.green, color.green), "green at $fraction was ${color.green}")
            assertTrue(near(tint.blue, color.blue), "blue at $fraction was ${color.blue}")
            assertTrue(near(fraction, color.alpha), "alpha at $fraction was ${color.alpha}")
        }
    }

    @Test
    fun `a fade out to nothing keeps its colour the whole way`() {
        midway.forEach { fraction ->
            val color = fade(tint, Color.Transparent, fraction)

            assertTrue(near(tint.red, color.red), "red at $fraction was ${color.red}")
            assertTrue(near(1f - fraction, color.alpha), "alpha at $fraction was ${color.alpha}")
        }
    }

    @Test
    fun `an opaque fade still crosses between the two colours`() {
        val other = Color(0xFF402080)
        val half = fade(tint, other, 0.5f)

        assertEquals(1f, half.alpha)
        assertTrue(half.red < tint.red, "should have moved toward the darker colour")
        assertTrue(half.red > other.red, "but not past it")
    }

    @Test
    fun `nothing stays nothing`() {
        assertEquals(Color.Transparent, fade(Color.Transparent, Color.Transparent, 0.5f))
    }

    @Test
    fun `the ordinary colour animation is why this converter exists`() {
        // Color.Transparent is transparent BLACK, and lerp moves every channel at once: halfway
        // from nothing to a near-white tint is a dark colour at half alpha, which composites to
        // grey. If this ever stops being true, animateDayColorAsState can go.
        val half = lerp(Color.Transparent, tint, 0.5f)

        assertTrue(
            half.red < tint.red - 0.1f,
            "expected the plain lerp to slide through a darker colour, got ${half.red}",
        )
    }
}
