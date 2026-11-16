package dev.voir.areca.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** The stock categorical hues carry a white glyph, and are told apart. */
class ArecaHuesTest {

    @Test
    fun `a white glyph reads on every hue`() {
        for (hue in ArecaHues().all) {
            val ratio = (Color.White.luminance() + 0.05f) / (hue.luminance() + 0.05f)
            assertTrue(ratio >= 3f, "white on $hue: $ratio is under 3")
        }
    }

    @Test
    fun `the hues are all different`() {
        val hues = ArecaHues().all
        assertEquals(hues.size, hues.toSet().size)
    }
}
