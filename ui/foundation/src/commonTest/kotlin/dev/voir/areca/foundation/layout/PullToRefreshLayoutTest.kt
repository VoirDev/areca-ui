package dev.voir.areca.foundation.layout

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for the pull damping, release and fling arithmetic of a [PullToRefreshLayout]. */
class PullToRefreshLayoutTest {
    @Test
    fun pullDistanceIsReducedBeforeThreshold() {
        assertEquals(
            expected = 20f,
            actual = calculatePullOffset(
                distancePulled = 40f,
                thresholdPx = 80f,
            ),
        )
    }

    @Test
    fun pullDistanceUsesResistanceBeyondThreshold() {
        assertEquals(
            expected = 140f,
            actual = calculatePullOffset(
                distancePulled = 320f,
                thresholdPx = 80f,
            ),
        )
    }

    @Test
    fun pullDistanceCapsOvershoot() {
        assertEquals(
            expected = 160f,
            actual = calculatePullOffset(
                distancePulled = 10_000f,
                thresholdPx = 80f,
            ),
        )
    }

    @Test
    fun refreshTriggersOnlyAtOrBeyondThreshold() {
        assertFalse(shouldTriggerRefresh(distancePulled = 159f, thresholdPx = 80f))
        assertTrue(shouldTriggerRefresh(distancePulled = 160f, thresholdPx = 80f))
        assertTrue(shouldTriggerRefresh(distancePulled = 200f, thresholdPx = 80f))
    }

    @Test
    fun `a release that requests a refresh rests the indicator at the threshold`() {
        assertEquals(1f, pullReleaseFraction(distancePulled = 160f, thresholdPx = 80f))
        assertEquals(1f, pullReleaseFraction(distancePulled = 400f, thresholdPx = 80f))
    }

    @Test
    fun `a release short of the threshold hides the indicator`() {
        assertEquals(0f, pullReleaseFraction(distancePulled = 159f, thresholdPx = 80f))
        assertEquals(0f, pullReleaseFraction(distancePulled = 0f, thresholdPx = 80f))
    }

    @Test
    fun pullConsumesOnlyDownwardReleaseVelocity() {
        assertEquals(0f, consumedPullVelocity(distancePulled = 0f, velocityY = 100f))
        assertEquals(0f, consumedPullVelocity(distancePulled = 20f, velocityY = -100f))
        assertEquals(100f, consumedPullVelocity(distancePulled = 20f, velocityY = 100f))
    }
}
