package dev.voir.areca.foundation.slot

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Width arithmetic of a centred slot row: when the middle keeps the centre line, when it gives it
 * up, and what the sides are never asked to give away.
 *
 * Widths are in pixels and read as a phone-sized bar: 720px of usable row, a leading control plus
 * its gap on one side and a trailing one plus its gap on the other.
 */
class CenterSlotPlacementTest {

    @Test
    fun `a middle that fits sits on the row's centre line`() {
        val placement = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 120,
            trailWidth = 200,
            desiredWidth = 240,
        )

        assertEquals(320, placement.width)
        assertEquals(200, placement.x)
        assertEquals(placement.x + placement.width / 2, 720 / 2)
    }

    @Test
    fun `even sides give the middle everything between them`() {
        val placement = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 160,
            trailWidth = 160,
            desiredWidth = 200,
        )

        assertEquals(400, placement.width)
        assertEquals(160, placement.x)
    }

    @Test
    fun `a title with no controls beside it takes the whole row`() {
        val placement = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 0,
            trailWidth = 0,
            desiredWidth = 300,
        )

        assertEquals(720, placement.width)
        assertEquals(0, placement.x)
    }

    @Test
    fun `a middle too wide to centre spreads between the sides instead of squeezing them`() {
        val placement = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 120,
            trailWidth = 260,
            desiredWidth = 340,
        )

        // The symmetric band would be 200px, too little; the middle takes the 340px between the
        // sides and starts where the leading slot ends. Neither side lost a pixel either way.
        assertEquals(340, placement.width)
        assertEquals(120, placement.x)
    }

    @Test
    fun `sides wider than the row leave the middle with nothing rather than a negative width`() {
        val placement = centerSlotPlacement(
            rowWidth = 320,
            leadWidth = 200,
            trailWidth = 200,
            desiredWidth = 0,
        )

        assertEquals(0, placement.width)
        assertEquals(200, placement.x)
    }

    @Test
    fun `a wide trailing action keeps its width and pushes the band inwards`() {
        val narrow = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 120,
            trailWidth = 120,
            desiredWidth = 200,
        )
        val wide = centerSlotPlacement(
            rowWidth = 720,
            leadWidth = 120,
            trailWidth = 260,
            desiredWidth = 200,
        )

        // Widening the trailing control narrows the title's band; it never narrows the control.
        assertEquals(480, narrow.width)
        assertEquals(200, wide.width)
        assertEquals(wide.x + wide.width / 2, 720 / 2)
    }
}
