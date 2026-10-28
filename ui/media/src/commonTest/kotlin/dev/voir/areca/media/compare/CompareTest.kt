package dev.voir.areca.media.compare

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for where a touch puts a compare divider. */
class CompareTest {
    @Test
    fun `a touch across sets the divider by its distance across`() {
        assertEquals(0.25f, fraction(x = 100f, y = 10f, width = 400, height = 200, across = true))
    }

    @Test
    fun `a touch down sets a vertical divider by its distance down`() {
        assertEquals(0.75f, fraction(x = 10f, y = 150f, width = 400, height = 200, across = false))
    }

    @Test
    fun `a touch outside the frame keeps the divider at its edge`() {
        assertEquals(1f, fraction(x = 900f, y = 0f, width = 400, height = 200, across = true))
        assertEquals(0f, fraction(x = -20f, y = 0f, width = 400, height = 200, across = true))
    }
}
