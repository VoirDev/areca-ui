package dev.voir.areca.foundation.table

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [TableColumnWidthMath], which resolves column widths from their specs. */
class TableColumnWidthMathTest {
    /**
     * Spec of a column sized by its content.
     *
     * @param natural Widest measured content, in pixels.
     * @param min Smallest width, in pixels.
     * @param max Largest width in pixels, or `null` for no limit.
     * @param weight Share of any leftover width.
     * @return The spec.
     */
    private fun auto(natural: Int, min: Int = 80, max: Int? = null, weight: Float = 1f) = TableColumnWidthSpec(
        fixedPx = null,
        minPx = min,
        maxPx = max,
        expandWeight = weight,
        naturalPx = natural,
    )

    /**
     * Spec of a column with a fixed width.
     *
     * @param width Width of the column, in pixels.
     * @return The spec.
     */
    private fun fixed(width: Int) = TableColumnWidthSpec(
        fixedPx = width,
        minPx = 80,
        maxPx = null,
        expandWeight = 1f,
        naturalPx = 0,
    )

    @Test
    fun `auto columns wider than the viewport keep their natural widths so the table scrolls`() {
        assertEquals(
            expected = listOf(300, 250),
            actual = TableColumnWidthMath.resolve(listOf(auto(300), auto(250)), viewportPx = 400),
        )
    }

    @Test
    fun `an auto column never shrinks below its min width`() {
        assertEquals(
            expected = listOf(120, 300),
            actual = TableColumnWidthMath.resolve(listOf(auto(40, min = 120), auto(300)), viewportPx = 200),
        )
    }

    @Test
    fun `an auto column never grows past its max width`() {
        assertEquals(
            expected = listOf(150, 100),
            actual = TableColumnWidthMath.resolve(listOf(auto(400, max = 150), auto(100, weight = 0f)), viewportPx = 200),
        )
    }

    @Test
    fun `fixed columns wider than the viewport leave auto columns at their natural width`() {
        assertEquals(
            expected = listOf(500, 100),
            actual = TableColumnWidthMath.resolve(listOf(fixed(500), auto(100)), viewportPx = 400),
        )
    }

    @Test
    fun `leftover space goes to auto columns and never to fixed ones`() {
        assertEquals(
            expected = listOf(100, 200, 200),
            actual = TableColumnWidthMath.resolve(listOf(fixed(100), auto(100), auto(100)), viewportPx = 500),
        )
    }

    @Test
    fun `uneven weights split the leftover proportionally and exactly`() {
        val widths = TableColumnWidthMath.resolve(listOf(auto(100, weight = 1f), auto(100, weight = 2f)), viewportPx = 501)

        assertEquals(listOf(200, 301), widths)
        assertEquals(501, widths.sum())
    }

    @Test
    fun `a capped column hands its share to the others`() {
        assertEquals(
            expected = listOf(120, 380),
            actual = TableColumnWidthMath.resolve(listOf(auto(100, max = 120), auto(100)), viewportPx = 500),
        )
    }

    @Test
    fun `an unmeasured auto column starts at its min width`() {
        assertEquals(
            expected = listOf(80),
            actual = TableColumnWidthMath.resolve(listOf(auto(0, weight = 0f)), viewportPx = 400),
        )
    }
}
