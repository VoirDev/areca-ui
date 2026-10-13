package dev.voir.areca.foundation.selection

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Tests for [swatchCheckColor], the rule that keeps a [SwatchPicker] check readable. */
class SwatchCheckColorTest {
    private val onDark = Color.White
    private val onLight = Color(0xFF131214)

    @Test
    fun `a white swatch gets the dark check`() {
        assertEquals(onLight, swatchCheckColor(Color.White, onDark = onDark, onLight = onLight))
    }

    @Test
    fun `a pale yellow swatch gets the dark check`() {
        val paleYellow = Color(0xFFFFF3B0)

        assertEquals(onLight, swatchCheckColor(paleYellow, onDark = onDark, onLight = onLight))
    }

    @Test
    fun `a deep blue swatch gets the light check`() {
        val deepBlue = Color(0xFF1A237E)

        assertEquals(onDark, swatchCheckColor(deepBlue, onDark = onDark, onLight = onLight))
    }

    @Test
    fun `contrast between black and white is the maximum ratio`() {
        assertTrue(contrastRatio(Color.Black, Color.White) in 20.9f..21.1f)
    }

    @Test
    fun `contrast of a color with itself is one`() {
        assertEquals(1f, contrastRatio(Color.Red, Color.Red))
    }
}
