package dev.voir.areca.crop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** How a finished crop carries over to another copy of the photo and sizes its output. */
class CropResultTest {

    /** A straightened, mirrored crop of a 1365 × 1024 preview of a 4000 × 3000 photo. */
    private val preview = CropResult(
        imageSize = Size(1365f, 1024f),
        center = Offset(682.5f, 512f),
        size = Size(600f, 400f),
        rotation = 12f,
        isMirrored = true,
    )

    @Test
    fun `a crop on a preview scales to the full photo`() {
        val full = preview.scaledTo(Size(4000f, 3000f))

        assertEquals(Size(4000f, 3000f), full.imageSize)
        assertClose(2000f, full.center.x)
        assertClose(1500f, full.center.y)
        assertClose(preview.aspectRatio, full.aspectRatio)
        assertClose(600f * 2.9300f, full.size.width, tolerance = 2f)
        assertEquals(12f, full.rotation)
        assertTrue(full.isMirrored)
    }

    @Test
    fun `a photo turned on its side is not the preview's source`() {
        assertFailsWith<IllegalArgumentException> { preview.scaledTo(Size(3000f, 4000f)) }
    }

    @Test
    fun `an output cap must be positive`() {
        assertFailsWith<IllegalArgumentException> { preview.outputSize(maxDimension = 0) }
    }

    /**
     * Asserts two floats match to within [tolerance].
     *
     * @param expected Value the test expects.
     * @param actual Value the code produced.
     * @param tolerance Largest difference that still counts as a match.
     */
    private fun assertClose(expected: Float, actual: Float, tolerance: Float = 0.01f) {
        assertTrue(abs(expected - actual) <= tolerance, "Expected $expected, was $actual")
    }
}
