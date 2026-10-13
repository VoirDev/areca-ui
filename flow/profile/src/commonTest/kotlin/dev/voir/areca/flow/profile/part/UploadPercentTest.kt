package dev.voir.areca.flow.profile.part

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** The percentage the upload band draws for a fraction. */
class UploadPercentTest {

    @Test
    fun `a fraction rounds to the nearest whole percent`() {
        assertEquals(43, uploadPercent(0.425f))
    }

    @Test
    fun `a fraction outside 0 to 1 is clamped`() {
        assertEquals(0, uploadPercent(-0.2f))
        assertEquals(100, uploadPercent(1.7f))
    }

    @Test
    fun `an unknown fraction draws no percentage`() {
        assertNull(uploadPercent(null))
    }

    @Test
    fun `NaN from an upload of unknown size draws no percentage`() {
        assertNull(uploadPercent(0f / 0f))
    }

    @Test
    fun `an infinite fraction draws no percentage`() {
        assertNull(uploadPercent(Float.POSITIVE_INFINITY))
    }
}
