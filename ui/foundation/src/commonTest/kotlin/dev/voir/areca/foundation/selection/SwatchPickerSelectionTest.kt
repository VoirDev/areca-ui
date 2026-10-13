package dev.voir.areca.foundation.selection

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for [isSwatchSelected], the rule that decides which [SwatchPicker] swatch is selected. */
class SwatchPickerSelectionTest {
    private fun color(id: String, name: String) =
        ColorItem(id = id, name = name, border = Color.Black, bg = Color.White)

    @Test
    fun `the color with the selected id is selected`() {
        val brand = color(id = "brand40", name = "Brand 40")

        assertTrue(isSwatchSelected(brand, selected = brand.copy()))
    }

    @Test
    fun `a color sharing only the selected name is not selected`() {
        val light = color(id = "gray-light", name = "Gray")
        val dark = color(id = "gray-dark", name = "Gray")

        assertFalse(isSwatchSelected(dark, selected = light))
    }

    @Test
    fun `no color is selected when the selection is empty`() {
        assertFalse(isSwatchSelected(color(id = "red40", name = "Red 40"), selected = null))
    }
}
