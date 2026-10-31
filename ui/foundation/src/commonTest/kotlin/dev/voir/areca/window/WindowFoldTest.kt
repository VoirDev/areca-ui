package dev.voir.areca.window

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** How a fold reported for the window is seen from a container inside it. */
class WindowFoldTest {
    private val fold = WindowFold.vertical(at = 380.dp, height = 720.dp)

    @Test
    fun `a container across the fold sees it in its own coordinates`() {
        val seen = fold.within(origin = DpOffset(100.dp, 40.dp), size = DpSize(600.dp, 680.dp))
        assertEquals(DpRect(280.dp, (-40).dp, 280.dp, 680.dp), seen?.bounds)
    }

    @Test
    fun `a container beside the fold sees none`() {
        assertNull(fold.within(origin = DpOffset(380.dp, 0.dp), size = DpSize(380.dp, 720.dp)))
        assertNull(fold.within(origin = DpOffset(0.dp, 0.dp), size = DpSize(380.dp, 720.dp)))
    }

    @Test
    fun `a horizontal fold crosses a container only between its top and bottom`() {
        val tabletop = WindowFold.horizontal(at = 380.dp, width = 720.dp)
        assertEquals(380.dp, tabletop.within(DpOffset.Zero, DpSize(720.dp, 760.dp))?.bounds?.top)
        assertNull(tabletop.within(DpOffset(0.dp, 400.dp), DpSize(720.dp, 360.dp)))
    }

    @Test
    fun `a half-opened fold separates the halves by default`() {
        assertEquals(true, WindowFold.horizontal(at = 380.dp, width = 720.dp).isSeparating)
        assertEquals(false, fold.isSeparating)
    }
}
