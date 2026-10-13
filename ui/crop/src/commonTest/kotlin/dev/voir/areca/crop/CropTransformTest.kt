package dev.voir.areca.crop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Where the photo sits under the frame, and how moves, zooms, turns and animations change it. */
class CropTransformTest {

    /** A landscape photo twice as wide as it is tall, in pixels. */
    private val image = Size(2000f, 1000f)

    /** A square frame, in viewport pixels. */
    private val square = Rect(0f, 0f, 400f, 400f)

    @Test
    fun `at zoom one the photo just covers the frame`() {
        val projection = CropTransform(frame = square).project(image)

        assertClose(0.4f, projection.coverScale)
        assertClose(0.4f, projection.scale)
        assertClose(500f, projection.limit.x)
        assertClose(0f, projection.limit.y)
    }

    @Test
    fun `straightening keeps every frame corner on the photo`() {
        val transform = CropTransform(frame = square, focus = Offset(5000f, -5000f), straighten = 12f, rotation = 90f)
            .clamped(image)

        assertFrameOnImage(transform)
    }

    @Test
    fun `a pan past the edge clamps to the edge`() {
        val transform = CropTransform(frame = square).panned(Offset(-10_000f, 0f), image).clamped(image)

        assertClose(transform.project(image).limit.x, transform.focus.x)
        assertFrameOnImage(transform)
    }

    @Test
    fun `zooming holds the anchor point still`() {
        val start = CropTransform(frame = square, straighten = -8f)
        val anchor = Offset(300f, 120f)
        val before = start.toImage(anchor, start.project(image))

        val zoomed = start.zoomed(3f, anchor, image, maxZoom = 10f)

        val after = zoomed.toImage(anchor, zoomed.project(image))
        assertClose(before.x, after.x)
        assertClose(before.y, after.y)
    }

    @Test
    fun `zoom stays within one and the maximum`() {
        val transform = CropTransform(frame = square)

        assertClose(1f, transform.zoomed(0.2f, null, image, maxZoom = 4f).zoom)
        assertClose(4f, transform.zoomed(40f, null, image, maxZoom = 4f).zoom)
    }

    @Test
    fun `mirroring flips the crop left to right on screen`() {
        val start = CropTransform(frame = square, focus = Offset(120f, -40f), rotation = 90f, straighten = 7f)
        val mirrored = start.mirrored()
        val point = Offset(310f, 90f)
        val reflected = Offset(2f * square.center.x - point.x, point.y)

        val original = start.toImage(point, start.project(image))
        val flipped = mirrored.toImage(reflected, mirrored.project(image))

        assertClose(original.x, flipped.x)
        assertClose(original.y, flipped.y)
    }

    @Test
    fun `resizing the frame leaves the photo where it is`() {
        val start = CropTransform(frame = square, zoom = 2f)
        val probe = Offset(250f, 260f)
        val before = start.toImage(probe, start.project(image))

        val resized = start.withFrame(Rect(100f, 120f, 380f, 400f), image, maxZoom = 10f)

        val after = resized.toImage(probe, resized.project(image))
        assertClose(before.x, after.x)
        assertClose(before.y, after.y)
    }

    @Test
    fun `growing the frame past the photo zooms the photo in`() {
        val start = CropTransform(frame = Rect(0f, 100f, 400f, 300f))

        val grown = start.withFrame(Rect(0f, 0f, 400f, 400f), image, maxZoom = 10f)

        assertFrameOnImage(grown)
    }

    @Test
    fun `fitted frame is centred and as large as the bounds allow`() {
        val frame = fitFrame(Rect(0f, 0f, 400f, 600f), aspectRatio = 4f / 3f)

        assertClose(400f, frame.width)
        assertClose(300f, frame.height)
        assertClose(300f, frame.center.y)
    }

    @Test
    fun `quarter turns normalise into one turn`() {
        assertEquals(270f, normalizeQuarterTurns(-90f))
        assertEquals(0f, normalizeQuarterTurns(360f))
        assertEquals(90f, normalizeQuarterTurns(88f))
    }

    @Test
    fun `an angle shifts by whole turns to the one nearest the reference`() {
        assertEquals(360f, nearestTurn(0f, reference = 270f))
        assertEquals(-90f, nearestTurn(270f, reference = 0f))
        assertEquals(90f, nearestTurn(90f, reference = 0f))
        assertEquals(-90f, nearestTurn(-90f, reference = 0f))
    }

    @Test
    fun `undoing a left turn turns back a quarter rather than three quarters`() {
        val turned = CropTransform(frame = square, rotation = 270f)

        val path = animationPath(start = turned, target = turned.copy(rotation = 0f))

        assertEquals(360f, path.rotation)
    }

    @Test
    fun `undoing a mirror squeezes back instead of flipping and spinning`() {
        val start = CropTransform(frame = square, rotation = 90f, straighten = 10f).mirrored()

        val path = animationPath(start = start, target = start.mirrored())

        assertEquals(start.isMirrored, path.isMirrored)
        assertClose(start.straighten, path.straighten)
        assertEquals(start.rotation, path.rotation)
    }

    /** Asserts that the four corners of [transform]'s frame land inside [image]. */
    private fun assertFrameOnImage(transform: CropTransform) {
        val projection = transform.project(image)
        val frame = transform.frame
        listOf(frame.topLeft, frame.topRight, frame.bottomRight, frame.bottomLeft).forEach { corner ->
            val point = transform.toImage(corner, projection)
            assertTrue(abs(point.x) <= image.width / 2f + Tolerance, "Corner $corner maps off the photo at $point")
            assertTrue(abs(point.y) <= image.height / 2f + Tolerance, "Corner $corner maps off the photo at $point")
        }
    }

    /**
     * Asserts two floats match to within [Tolerance].
     *
     * @param expected Value the test expects.
     * @param actual Value the code produced.
     */
    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(abs(expected - actual) <= Tolerance, "Expected $expected, was $actual")
    }

    /** Shared limits of the assertions. */
    private companion object {
        /** Largest difference two floats may have and still match. */
        const val Tolerance = 0.01f
    }
}
