package dev.voir.areca.crop

import kotlin.test.Test
import kotlin.test.assertEquals

/** How the toolbar writes angles, zooms and rotations by default. */
class CropValueFormatTest {

    @Test
    fun `angles carry a sign and one decimal`() {
        assertEquals("0.0°", formatAngle(0.02f))
        assertEquals("+3.2°", formatAngle(3.21f))
        assertEquals("−3.5°", formatAngle(-3.5f))
    }

    @Test
    fun `zoom carries one decimal and a multiplication sign`() {
        assertEquals("1.0×", formatZoom(1f))
        assertEquals("2.5×", formatZoom(2.46f))
    }
}
