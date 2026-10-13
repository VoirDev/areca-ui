package dev.voir.areca.crop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Which grip a press takes, and the frame a grip drags to. */
class CropHandleTest {

    /** Frame the presses and drags act on, in viewport pixels. */
    private val frame = Rect(100f, 100f, 300f, 250f)

    /** Area the frame may occupy, in viewport pixels. */
    private val bounds = Rect(0f, 0f, 400f, 400f)

    @Test
    fun `a press near a corner takes the corner`() {
        assertEquals(CropHandle.BottomRight, handleAt(frame, Offset(310f, 240f), touchRadius = 28f, includeEdges = true))
    }

    @Test
    fun `edges are only live when asked for`() {
        val onTopEdge = Offset(200f, 104f)

        assertEquals(CropHandle.Top, handleAt(frame, onTopEdge, touchRadius = 28f, includeEdges = true))
        assertNull(handleAt(frame, onTopEdge, touchRadius = 28f, includeEdges = false))
    }

    @Test
    fun `a press inside the frame moves the photo`() {
        assertNull(handleAt(frame, frame.center, touchRadius = 28f, includeEdges = true))
    }

    @Test
    fun `a free frame stops at its minimum size`() {
        val resized = resizeFrame(frame, CropHandle.Right, Offset(-500f, 0f), bounds, minSize = 72f, ratio = null)

        assertEquals(172f, resized.right)
        assertEquals(frame.top, resized.top)
    }

    @Test
    fun `a free frame shorter than the minimum keeps its size instead of failing`() {
        val shortBounds = Rect(0f, 0f, 300f, 50f)
        val short = Rect(117f, 0f, 183f, 50f)

        val resized = resizeFrame(short, CropHandle.TopLeft, Offset(10f, 10f), shortBounds, minSize = 72f, ratio = null)

        assertEquals(short, resized)
    }

    @Test
    fun `a free frame shorter than the minimum can still grow`() {
        val shortBounds = Rect(0f, 0f, 300f, 50f)
        val short = Rect(117f, 0f, 183f, 50f)

        val resized = resizeFrame(short, CropHandle.Right, Offset(40f, 0f), shortBounds, minSize = 72f, ratio = null)

        assertEquals(223f, resized.right)
        assertEquals(short.height, resized.height)
    }

    @Test
    fun `a locked frame keeps its ratio and its opposite corner`() {
        val ratio = frame.width / frame.height

        val resized = resizeFrame(frame, CropHandle.TopLeft, Offset(-40f, -10f), bounds, minSize = 72f, ratio = ratio)

        assertTrue(abs(resized.width / resized.height - ratio) < 0.001f)
        assertEquals(frame.bottomRight, resized.bottomRight)
    }

    @Test
    fun `a locked frame stops at the bounds`() {
        val ratio = frame.width / frame.height

        val resized = resizeFrame(frame, CropHandle.BottomRight, Offset(900f, 900f), bounds, minSize = 72f, ratio = ratio)

        assertTrue(resized.right <= bounds.right + 0.001f && resized.bottom <= bounds.bottom + 0.001f)
        assertTrue(abs(resized.width / resized.height - ratio) < 0.001f)
    }
}
