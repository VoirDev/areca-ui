package dev.voir.areca.crop

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.test.TestScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Rendering of finished crops into real bitmaps: transform order, preview scaling and masks. */
class CropRenderTest {

    /** Animations are launched here and never run; rendering reads each command's target. */
    private val scope = TestScope()

    @Test
    fun `a mirrored crop is flipped before it is rotated`() {
        val result = CropResult(
            imageSize = Size(8f, 8f),
            center = Offset(4f, 4f),
            size = Size(8f, 8f),
            rotation = 90f,
            isMirrored = true,
        )

        val pixels = result.render(Quadrants, IntSize(8, 8)).toPixelMap()

        // Flipping the source left to right and then turning it a quarter clockwise moves each
        // quadrant to the one diagonally opposite its mirror image: top-left to bottom-right, and so on.
        assertEquals(Quadrants.bottomRight, pixels[1, 1])
        assertEquals(Quadrants.topRight, pixels[6, 1])
        assertEquals(Quadrants.bottomLeft, pixels[1, 6])
        assertEquals(Quadrants.topLeft, pixels[6, 6])
    }

    @Test
    fun `a rotation and a mirror on the stage render as the stage drew them`() {
        val state = ImageCropState(CropSpec(), scope).apply {
            updateImage(Size(8f, 8f))
            updateBounds(Rect(0f, 0f, 400f, 400f))
        }

        state.rotateLeft()
        state.flipHorizontally()

        val result = assertNotNull(state.result())
        assertEquals(90f, result.rotation)
        assertTrue(result.isMirrored)
        val pixels = assertNotNull(state.render(Quadrants)).toPixelMap()
        assertEquals(Quadrants.bottomRight, pixels[1, 1])
        assertEquals(Quadrants.topLeft, pixels[pixels.width - 2, pixels.height - 2])
    }

    @Test
    fun `a crop made on a preview renders at the full photo's size`() {
        val state = ImageCropState(CropSpec(), scope).apply {
            updateImage(Size(8f, 8f))
            updateBounds(Rect(0f, 0f, 400f, 400f))
        }
        state.rotateLeft()

        val pixels = assertNotNull(state.render(LargeQuadrants)).toPixelMap()

        assertEquals(IntSize(32, 32), IntSize(pixels.width, pixels.height))
        // A quarter turn anticlockwise brings the top-right quadrant to the top left.
        assertEquals(Quadrants.topRight, pixels[2, 2])
        assertEquals(Quadrants.bottomLeft, pixels[29, 29])
    }

    @Test
    fun `a mask sized in dp keeps the proportions the stage showed`() {
        val state = ImageCropState(CropSpec(mask = RoundedCornerShape(12.dp), maxOutputDimension = 100), scope).apply {
            updateImage(Size(1000f, 1000f))
            updateBounds(Rect(0f, 0f, 1000f, 1000f))
            updateStage(Density(3f), LayoutDirection.Ltr)
        }

        val pixels = assertNotNull(state.render(Solid)).toPixelMap()

        // On the 1000 px stage at density 3 the corners are 36 px, 3.6 % of the side: 3.6 px here.
        // Resolved at the output's own size they would be 12 px and leave this pixel outside.
        assertEquals(IntSize(100, 100), IntSize(pixels.width, pixels.height))
        assertTrue(pixels[2, 2].alpha > 0.9f, "Expected an opaque pixel inside the corner, was ${pixels[2, 2]}")
        assertTrue(pixels[0, 0].alpha < 0.5f, "Expected a transparent corner, was ${pixels[0, 0]}")
    }

    @Test
    fun `a mask drawn right to left renders right to left`() {
        val state = ImageCropState(CropSpec(mask = RoundedCornerShape(topStart = 30.dp), maxOutputDimension = 100), scope).apply {
            updateImage(Size(1000f, 1000f))
            updateBounds(Rect(0f, 0f, 1000f, 1000f))
            updateStage(Density(1f), LayoutDirection.Rtl)
        }

        val pixels = assertNotNull(state.render(Solid)).toPixelMap()

        assertTrue(pixels[99, 0].alpha < 0.5f, "Expected the rounded corner on the right, was ${pixels[99, 0]}")
        assertTrue(pixels[0, 0].alpha > 0.9f, "Expected a square corner on the left, was ${pixels[0, 0]}")
    }

    /** An 8 × 8 photo of four differently coloured quadrants. */
    private object Quadrants : Painter() {
        /** Colour of the top-left quadrant. */
        val topLeft = Color.Red

        /** Colour of the top-right quadrant. */
        val topRight = Color.Green

        /** Colour of the bottom-left quadrant. */
        val bottomLeft = Color.Blue

        /** Colour of the bottom-right quadrant. */
        val bottomRight = Color.Yellow

        override val intrinsicSize: Size = Size(8f, 8f)

        override fun DrawScope.onDraw() {
            val half = Size(size.width / 2f, size.height / 2f)
            drawRect(topLeft, Offset.Zero, half)
            drawRect(topRight, Offset(half.width, 0f), half)
            drawRect(bottomLeft, Offset(0f, half.height), half)
            drawRect(bottomRight, Offset(half.width, half.height), half)
        }
    }

    /** [Quadrants] decoded at four times the size: the full photo an 8 × 8 preview stands in for. */
    private object LargeQuadrants : Painter() {
        override val intrinsicSize: Size = Size(32f, 32f)

        override fun DrawScope.onDraw() {
            with(Quadrants) { draw(size) }
        }
    }

    /** A 1000 × 1000 photo of one opaque colour. */
    private object Solid : Painter() {
        override val intrinsicSize: Size = Size(1000f, 1000f)

        override fun DrawScope.onDraw() {
            drawRect(Color.White)
        }
    }
}
