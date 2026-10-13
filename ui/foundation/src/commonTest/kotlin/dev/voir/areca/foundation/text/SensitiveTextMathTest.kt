package dev.voir.areca.foundation.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Tests for [SensitiveTextMath], the rules a [SensitiveText]'s veil clears by. */
class SensitiveTextMathTest {
    @Test
    fun `a seed always gives the same fraction`() {
        assertEquals(SensitiveTextMath.noise(42), SensitiveTextMath.noise(42))
    }

    @Test
    fun `every seed lands inside the unit interval`() {
        val outside = (-500..500).map(SensitiveTextMath::noise).filter { it < 0f || it >= 1f }

        assertTrue(outside.isEmpty(), "fractions outside [0, 1): $outside")
    }

    @Test
    fun `sequential seeds scatter instead of climbing`() {
        val fractions = (0 until 64).map(SensitiveTextMath::noise)
        val lowHalf = fractions.count { it < 0.5f }

        // A hash that leaked its input's order would put the run's first half on one side.
        assertTrue(lowHalf in 20..44, "$lowHalf of 64 fractions below the midpoint")
    }

    @Test
    fun `a spark is brightest as the reveal passes its own position`() {
        val alpha = SensitiveTextMath.sparkAlpha(progress = 0.4f, phase = 0.4f)

        assertEquals(1f, alpha)
    }

    @Test
    fun `a spark the reveal has not reached is dark`() {
        val alpha = SensitiveTextMath.sparkAlpha(progress = 0.1f, phase = 0.9f)

        assertEquals(0f, alpha)
    }

    @Test
    fun `a spark dims as the reveal moves away from it`() {
        val near = SensitiveTextMath.sparkAlpha(progress = 0.5f, phase = 0.45f)
        val far = SensitiveTextMath.sparkAlpha(progress = 0.5f, phase = 0.3f)

        assertTrue(near > far, "$near should be brighter than $far")
    }

    @Test
    fun `a settled text draws no sparks`() {
        assertEquals(0f, SensitiveTextMath.sparkAlpha(progress = 0f, phase = 0f))
        assertEquals(0f, SensitiveTextMath.sparkAlpha(progress = 1f, phase = 1f))
    }

    @Test
    fun `a concealed text is drawn at no opacity where the platform cannot blur`() {
        assertEquals(0f, SensitiveTextMath.contentAlpha(0f))
    }

    @Test
    fun `the text stays invisible until the veil is mostly gone`() {
        assertEquals(0f, SensitiveTextMath.contentAlpha(SensitiveTextMath.CONTENT_FADE_START))
        assertTrue(SensitiveTextMath.contentAlpha(0.8f) > 0f)
    }

    @Test
    fun `a revealed text is drawn in full`() {
        assertEquals(1f, SensitiveTextMath.contentAlpha(1f))
    }

    @Test
    fun `the shimmer starts and ends clear of the text`() {
        val start = SensitiveTextMath.shimmerCenter(progress = 0f, width = 200f, bandWidth = 90f)
        val end = SensitiveTextMath.shimmerCenter(progress = 1f, width = 200f, bandWidth = 90f)

        assertEquals(-90f, start)
        assertEquals(290f, end)
    }

    @Test
    fun `the shimmer crosses the text at a steady pace`() {
        val quarter = SensitiveTextMath.shimmerCenter(progress = 0.25f, width = 200f, bandWidth = 90f)
        val half = SensitiveTextMath.shimmerCenter(progress = 0.5f, width = 200f, bandWidth = 90f)
        val threeQuarters = SensitiveTextMath.shimmerCenter(0.75f, width = 200f, bandWidth = 90f)

        assertEquals(half - quarter, threeQuarters - half)
    }
}
