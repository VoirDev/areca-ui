package dev.voir.areca.flow.attachments.viewer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals

class ZoomMathTest {

    private val frame = Size(width = 400f, height = 800f)

    /** A landscape photograph on a portrait frame: fitted, it is 400 by 300 with bands above and below. */
    private val landscape = fittedSize(picture = Size(width = 4000f, height = 3000f), frame = frame)

    @Test
    fun `a picture is fitted whole into the frame`() {
        assertEquals(Size(width = 400f, height = 300f), landscape)
    }

    @Test
    fun `a picture with no size of its own fills the frame`() {
        assertEquals(frame, fittedSize(picture = Size.Unspecified, frame = frame))
    }

    @Test
    fun `at the fitted size nothing can be panned`() {
        val clamped = clampOffset(Offset(120f, -60f), frame, landscape, scale = 1f)

        // Compared as numbers: clamping a negative pan to zero gives -0.0, which Offset's equality
        // tells apart from 0.0 although the picture does not move.
        assertEquals(0f, clamped.x, absoluteTolerance = 0f)
        assertEquals(0f, clamped.y, absoluteTolerance = 0f)
    }

    @Test
    fun `a pan past an edge of the picture stops at that edge`() {
        // At twice the size the picture is 800 wide, overhanging the frame by 200 on each side.
        val clamped = clampOffset(Offset(900f, 0f), frame, landscape, scale = 2f)

        assertEquals(200f, clamped.x, absoluteTolerance = 0f)
    }

    @Test
    fun `a letterboxed picture cannot be dragged into its bands`() {
        // At 2.5 times the size the picture is 750 tall, still inside the 800 frame, so it stays
        // centred; a limit taken from the frame would have let it travel 600 either way.
        val clamped = clampOffset(Offset(0f, -500f), frame, landscape, scale = 2.5f)

        assertEquals(0f, clamped.y, absoluteTolerance = 0f)
    }

    @Test
    fun `zooming around the centre only scales the offset`() {
        val zoomed = zoomOffset(Offset(10f, -20f), focus = Offset(200f, 400f), frame = frame, zoomChange = 2f)

        assertEquals(Offset(20f, -40f), zoomed)
    }

    @Test
    fun `zooming keeps the point under the fingers where it is`() {
        val centre = Offset(200f, 400f)
        val focus = Offset(300f, 100f)
        val before = Offset(10f, -20f)
        val scale = 2f
        val zoomChange = 1.5f

        val after = zoomOffset(before, focus, frame, zoomChange)

        // The point of the picture drawn under the focus, measured from the picture's centre.
        val pointBefore = (focus - centre - before) / scale
        val pointAfter = (focus - centre - after) / (scale * zoomChange)
        assertEquals(pointBefore.x, pointAfter.x, absoluteTolerance = 0.001f)
        assertEquals(pointBefore.y, pointAfter.y, absoluteTolerance = 0.001f)
    }

    @Test
    fun `a picture is asked for at the longer side of its page`() {
        assertEquals(2340, displayEdge(maxWidth = 1080, maxHeight = 2340, fallback = 2048))
    }

    @Test
    fun `an unbounded side is not a size to ask for`() {
        assertEquals(1080, displayEdge(maxWidth = 1080, maxHeight = Int.MAX_VALUE, fallback = 2048))
        assertEquals(2048, displayEdge(maxWidth = Int.MAX_VALUE, maxHeight = Int.MAX_VALUE, fallback = 2048))
    }

    @Test
    fun `a pan inside the edges is used whole`() {
        val result = panWithin(Offset(0f, 100f), pan = Offset(-50f, 100f), frame, content = frame, scale = 2f)

        assertEquals(Offset(-50f, 200f), result.offset)
        assertEquals(0f, result.remainder.x, absoluteTolerance = 0.001f)
        assertEquals(0f, result.remainder.y, absoluteTolerance = 0.001f)
    }

    @Test
    fun `a pan past an edge hands back what the edge stopped`() {
        // Zoomed twice, a frame-filling column overhangs the frame by 400 above and below.
        val result = panWithin(Offset(0f, 350f), pan = Offset(0f, 100f), frame, content = frame, scale = 2f)

        assertEquals(400f, result.offset.y, absoluteTolerance = 0.001f)
        assertEquals(50f, result.remainder.y, absoluteTolerance = 0.001f)
    }
}
