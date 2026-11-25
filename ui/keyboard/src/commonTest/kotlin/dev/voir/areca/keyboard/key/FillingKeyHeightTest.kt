package dev.voir.areca.keyboard.key

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

/** How tall the keys of a grid that fills its space grow, between the key height and the maximum. */
class FillingKeyHeightTest {
    private val fiveRows = KeyboardGrid(List(5) { row -> KeyRow(Key.text("$row") {}) })

    @Test
    fun `the keys share the height the gaps leave`() {
        // 5 rows and 4 gaps of 10dp in 400dp: (400 - 40) / 5.
        assertEquals(72.dp, fillingKeyHeight(fiveRows, 400.dp, rowGap = 10.dp, minKeyHeight = 40.dp, maxKeyHeight = 96.dp))
    }

    @Test
    fun `a tall space keeps the keys at the maximum`() {
        assertEquals(96.dp, fillingKeyHeight(fiveRows, 1200.dp, rowGap = 10.dp, minKeyHeight = 40.dp, maxKeyHeight = 96.dp))
    }

    @Test
    fun `a short space keeps the keys at the key height`() {
        assertEquals(40.dp, fillingKeyHeight(fiveRows, 150.dp, rowGap = 10.dp, minKeyHeight = 40.dp, maxKeyHeight = 96.dp))
    }

    @Test
    fun `a row with its own height is left out of the share`() {
        val grid = KeyboardGrid(fiveRows.rows.dropLast(1) + KeyRow(listOf(Key.text("=") {}), height = 80.dp))
        // 400dp less 4 gaps and the 80dp row, over the 4 other rows.
        assertEquals(70.dp, fillingKeyHeight(grid, 400.dp, rowGap = 10.dp, minKeyHeight = 40.dp, maxKeyHeight = 96.dp))
    }
}
