package dev.voir.areca.foundation.slot

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Vertical arithmetic of an aligned slot row, one scenario at a time.
 *
 * Heights are in pixels and read as a settings row at 1x: a 20px title line, a 39px title and
 * subtitle, a 55px title, subtitle and caption, a 28px icon tile, a 24px switch, a 40px avatar and
 * an 8px legend dot.
 */
class AlignedPlacementTest {

    private val titleLine = FirstLine(center = 10, height = 20)

    @Test
    fun `a single line centres every slot on it`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Auto,
            startHeight = 28,
            centerHeight = 20,
            firstLine = titleLine,
            endHeight = 24,
            minHeight = 0,
        )

        assertEquals(28, placement.height)
        assertEquals(0, placement.startY)
        assertEquals(4, placement.centerY)
        assertEquals(2, placement.endY)
    }

    @Test
    fun `auto centres a title and a subtitle as one block`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Auto,
            startHeight = 28,
            centerHeight = 39,
            firstLine = titleLine,
            endHeight = 24,
            minHeight = 0,
        )

        assertEquals(39, placement.height)
        assertEquals(0, placement.centerY)
        assertEquals(5, placement.startY)
        assertEquals(placement.startY + 28 / 2, placement.endY + 24 / 2)
    }

    @Test
    fun `auto starts a three-line row at the top`() {
        assertEquals(
            SlotRowAlignment.Top,
            resolveAlignment(SlotRowAlignment.Auto, centerHeight = 55, firstLine = titleLine),
        )
        assertEquals(
            SlotRowAlignment.Center,
            resolveAlignment(SlotRowAlignment.Auto, centerHeight = 39, firstLine = titleLine),
        )
    }

    @Test
    fun `auto centres a middle slot that reports no first line`() {
        assertEquals(
            SlotRowAlignment.Center,
            resolveAlignment(SlotRowAlignment.Auto, centerHeight = 120, firstLine = null),
        )
    }

    @Test
    fun `top keeps the trailing control level with the visual`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Top,
            startHeight = 28,
            centerHeight = 55,
            firstLine = titleLine,
            endHeight = 24,
            minHeight = 0,
        )

        assertEquals(0, placement.startY)
        assertEquals(0, placement.centerY)
        assertEquals(placement.startY + 28 / 2, placement.endY + 24 / 2)
        assertEquals(55, placement.height)
    }

    @Test
    fun `top centres a visual shorter than the first line on it`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Top,
            startHeight = 16,
            centerHeight = 55,
            firstLine = titleLine,
            endHeight = 0,
            minHeight = 0,
        )

        assertEquals(placement.centerY + 10, placement.startY + 16 / 2)
    }

    @Test
    fun `top without a visual keeps the trailing control on the first line`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Top,
            startHeight = 0,
            centerHeight = 55,
            firstLine = titleLine,
            endHeight = 24,
            minHeight = 0,
        )

        assertEquals(placement.centerY + 10, placement.endY + 24 / 2)
    }

    @Test
    fun `first line centres a marker on the title`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.FirstLine,
            startHeight = 8,
            centerHeight = 39,
            firstLine = titleLine,
            endHeight = 20,
            minHeight = 0,
        )

        assertEquals(placement.centerY + 10, placement.startY + 8 / 2)
        assertEquals(placement.centerY + 10, placement.endY + 20 / 2)
    }

    @Test
    fun `a short row is centred within its minimum height`() {
        val placement = alignedPlacement(
            alignment = SlotRowAlignment.Auto,
            startHeight = 0,
            centerHeight = 20,
            firstLine = titleLine,
            endHeight = 0,
            minHeight = 24,
        )

        assertEquals(24, placement.height)
        assertEquals(2, placement.centerY)
    }

    @Test
    fun `nothing is placed above the top of the row`() {
        for (alignment in SlotRowAlignment.entries) {
            val placement = alignedPlacement(
                alignment = alignment,
                startHeight = 32,
                centerHeight = 60,
                firstLine = titleLine,
                endHeight = 44,
                minHeight = 0,
            )

            assertEquals(0, minOf(placement.startY, placement.centerY, placement.endY), "$alignment")
        }
    }
}
