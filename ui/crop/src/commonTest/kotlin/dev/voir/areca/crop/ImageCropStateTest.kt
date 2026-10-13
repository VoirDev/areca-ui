package dev.voir.areca.crop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.test.TestScope
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

/** The commands of a crop, its undo history, and how it is saved and restored. */
class ImageCropStateTest {

    /** Animations are launched here and never run, so each command's target is read through [ImageCropState.result]. */
    private val scope = TestScope()

    /** Clock the scroll-wheel bursts are timed against. */
    private val clock = TestTimeSource()

    /**
     * A ready crop of a 4000 × 3000 photo on a 400 × 400 stage.
     *
     * @param aspect Proportions the frame starts with.
     * @param isAspectLocked Whether the frame keeps [aspect].
     * @return The state, with the photo and the stage already reported.
     */
    private fun state(
        aspect: CropAspect = CropAspect.Free,
        isAspectLocked: Boolean = false,
    ): ImageCropState = ImageCropState(
        spec = CropSpec(aspect = aspect, isAspectLocked = isAspectLocked),
        scope = scope,
        timeSource = clock,
    ).apply {
        updateImage(Size(4000f, 3000f))
        updateBounds(Rect(0f, 0f, 400f, 400f))
    }

    @Test
    fun `nothing can be read until the photo and the stage are known`() {
        val state = ImageCropState(CropSpec(), scope)
        state.updateImage(Size(4000f, 3000f))

        assertNull(state.result())
    }

    @Test
    fun `a fresh free crop is the whole photo`() {
        val result = assertNotNull(state().result())

        assertClose(4000f, result.size.width)
        assertClose(3000f, result.size.height)
        assertEquals(Offset(2000f, 1500f), result.center)
        assertEquals(0f, result.rotation)
    }

    @Test
    fun `a square crop starts centred and as large as the photo allows`() {
        val result = assertNotNull(state(aspect = CropAspect.Square).result())

        assertClose(3000f, result.size.width)
        assertClose(3000f, result.size.height)
    }

    @Test
    fun `rotating left turns the photo anticlockwise and the frame with it`() {
        val state = state()

        state.rotateLeft()

        val result = assertNotNull(state.result())
        assertEquals(-90f, result.rotation)
        assertClose(3f / 4f, result.aspectRatio)
    }

    @Test
    fun `a locked aspect keeps its frame through a rotation`() {
        val state = state(aspect = CropAspect.Ratio(3, 1), isAspectLocked = true)

        state.rotateLeft()

        assertClose(3f, assertNotNull(state.result()).aspectRatio)
        assertFalse(state.canSwapOrientation)
    }

    @Test
    fun `swapping orientation turns a ratio on its side`() {
        val state = state()
        state.selectAspect(CropAspect.Ratio(4, 3))

        state.swapOrientation()

        assertTrue(state.isOrientationSwapped)
        assertClose(3f / 4f, assertNotNull(state.result()).aspectRatio)
    }

    @Test
    fun `straightening snaps to level close to zero`() {
        val state = state()

        state.setStraighten(0.2f)
        assertEquals(0f, state.straighten)

        state.setStraighten(60f)
        assertEquals(45f, state.straighten)
    }

    @Test
    fun `mirroring is reported and resettable`() {
        val state = state()

        state.flipHorizontally()

        assertTrue(assertNotNull(state.result()).isMirrored)
        assertTrue(state.canReset)
    }

    @Test
    fun `the avatar spec renders a masked square at its output size`() {
        val state = ImageCropState(CropSpec.Avatar, scope).apply {
            updateImage(Size(4000f, 3000f))
            updateBounds(Rect(0f, 0f, 400f, 400f))
        }

        val result = assertNotNull(state.result())

        assertEquals(IntSize(512, 512), result.outputSize(state.spec.maxOutputDimension))
    }

    @Test
    fun `an output cap never scales a small crop up`() {
        val result = CropResult(Size(300f, 200f), Offset(150f, 100f), Size(300f, 200f), rotation = 0f, isMirrored = false)

        assertEquals(IntSize(300, 200), result.outputSize(maxDimension = 1500))
    }

    @Test
    fun `a fresh crop has nothing to reset`() {
        assertFalse(state().canReset)
    }

    /** Asserts two floats match to within a hundredth. */
    @Test
    fun `undo walks back every change one step at a time`() {
        val state = state()
        val fresh = assertNotNull(state.result())

        state.selectAspect(CropAspect.Ratio(4, 3))
        val fourByThree = assertNotNull(state.result())
        state.flipHorizontally()
        state.rotateLeft()

        state.undo()
        state.undo()
        assertEquals(fourByThree, state.result())
        assertTrue(state.canUndo)

        state.undo()
        assertEquals(fresh, state.result())
        assertEquals(CropAspect.Free, state.aspect)
        assertFalse(state.canUndo)
    }

    @Test
    fun `changes from an earlier tool session can still be undone`() {
        val state = state()
        val before = assertNotNull(state.result())

        // Open Angle, straighten, OK: the toolbar records a step on opening, the dial on its drag.
        state.checkpoint()
        state.startAdjusting(straightening = true)
        state.setStraighten(5f)
        state.stopAdjusting()

        // Open Angle again: nothing new has changed, yet Undo reaches the first session.
        state.checkpoint()
        assertTrue(state.canUndo)

        state.undo()
        assertEquals(before, state.result())
    }

    @Test
    fun `reset is a step undo brings back`() {
        val state = state()
        state.rotateLeft()
        val rotated = assertNotNull(state.result())

        state.reset()
        state.undo()

        assertEquals(rotated, state.result())
    }

    @Test
    fun `a fresh crop has nothing to undo`() {
        val state = state()

        state.checkpoint()

        assertFalse(state.canUndo)
    }

    @Test
    fun `a new photo clears the history`() {
        val state = state()
        state.rotateLeft()

        state.updateImage(Size(3000f, 3000f))

        assertFalse(state.canUndo)
    }

    @Test
    fun `undoing into new bounds keeps the earlier proportions`() {
        val state = state(aspect = CropAspect.Ratio(4, 3))
        state.swapOrientation()

        state.updateBounds(Rect(0f, 0f, 300f, 600f))
        state.undo()

        assertClose(4f / 3f, assertNotNull(state.result()).aspectRatio)
        assertFalse(state.isOrientationSwapped)
    }

    @Test
    fun `a key press right after a rotation is a step of its own`() {
        val state = state()
        state.rotateLeft()
        val rotated = assertNotNull(state.result())

        state.zoomBy(1.1f)
        state.undo()

        assertEquals(rotated, state.result())
    }

    @Test
    fun `each key press is a step of its own`() {
        val state = state()
        state.zoomBy(1.1f)

        state.zoomBy(1.1f)
        state.undo()

        assertClose(1.1f, state.zoom)
    }

    @Test
    fun `a key press right after an undo can be undone`() {
        val state = state()
        state.flipHorizontally()
        state.undo()

        state.zoomBy(1.1f)

        assertTrue(state.canUndo)
    }

    @Test
    fun `one turn of the wheel is one step`() {
        val state = state()

        state.scrollZoom(1.1f, Offset(200f, 200f))
        clock += 100.milliseconds
        state.scrollZoom(1.1f, Offset(200f, 200f))
        state.undo()

        assertClose(1f, state.zoom)
    }

    @Test
    fun `wheel turns apart in time are separate steps`() {
        val state = state()
        state.scrollZoom(1.1f, Offset(200f, 200f))
        val first = state.zoom

        clock += 1.seconds
        state.scrollZoom(1.1f, Offset(200f, 200f))
        state.undo()

        assertClose(first, state.zoom)
    }

    @Test
    fun `another step ends a wheel burst`() {
        val state = state()
        state.scrollZoom(1.1f, Offset(200f, 200f))
        state.zoomBy(1.1f)
        val afterKey = state.zoom

        state.scrollZoom(1.1f, Offset(200f, 200f))
        state.undo()

        assertClose(afterKey, state.zoom)
    }

    @Test
    fun `stage bounds with no room are ignored`() {
        val state = state()
        val before = assertNotNull(state.result())

        state.updateBounds(Rect(24f, 24f, 16f, 16f))

        assertEquals(before, state.result())
    }

    @Test
    fun `a free frame can be made wider and taller without a pointer`() {
        val state = state()

        assertTrue(state.stretchFrame(1.15f, minSize = 72f))
        assertClose(4f / 3f * 1.15f, assertNotNull(state.result()).aspectRatio)

        state.undo()
        assertClose(4f / 3f, assertNotNull(state.result()).aspectRatio)
    }

    @Test
    fun `a frame with a ratio cannot be stretched`() {
        val state = state(aspect = CropAspect.Ratio(4, 3))

        assertFalse(state.stretchFrame(1.15f, minSize = 72f))
    }

    @Test
    fun `the result maps frame points to the photo points the stage draws there`() {
        val state = state()
        state.rotateLeft()
        state.flipHorizontally()
        state.setStraighten(12f)
        val result = assertNotNull(state.result())
        val transform = assertNotNull(state.snapshot()).transform
        val frame = transform.frame
        val sourcePerViewport = result.size.width / frame.width
        val radians = -result.rotation * PI.toFloat() / 180f
        val mirror = if (result.isMirrored) -1f else 1f

        listOf(Offset(30f, -20f), Offset(-70f, 45f), Offset(90f, 60f)).forEach { offset ->
            val drawn = transform.toImage(frame.center + offset, transform.project(state.imageSize)) +
                Offset(state.imageSize.width / 2f, state.imageSize.height / 2f)
            val q = offset * sourcePerViewport
            val rotated = Offset(q.x * cos(radians) - q.y * sin(radians), q.x * sin(radians) + q.y * cos(radians))
            val documented = result.center + Offset(mirror * rotated.x, rotated.y)

            assertClose(drawn.x, documented.x)
            assertClose(drawn.y, documented.y)
        }
    }

    @Test
    fun `holding a key down is one step`() {
        val state = state()
        val start = assertNotNull(state.result())

        repeat(3) { state.pressKey(Key.DirectionRight) { zoomAround(1.1f, anchor = null) } }
        state.releaseKey(Key.DirectionRight)
        state.undo()

        assertEquals(start, state.result())
    }

    @Test
    fun `pressing a key again after releasing it is a step of its own`() {
        val state = state()
        state.pressKey(Key.DirectionRight) { zoomAround(1.1f, anchor = null) }
        state.releaseKey(Key.DirectionRight)
        val first = state.zoom

        state.pressKey(Key.DirectionRight) { zoomAround(1.1f, anchor = null) }
        state.undo()

        assertClose(first, state.zoom)
    }

    @Test
    fun `another step ends a key hold`() {
        val state = state()
        state.pressKey(Key.DirectionRight) { zoomAround(1.1f, anchor = null) }
        state.flipHorizontally()
        val flipped = state.zoom

        state.pressKey(Key.DirectionRight) { zoomAround(1.1f, anchor = null) }
        state.undo()

        assertClose(flipped, state.zoom)
        assertTrue(state.isMirrored)
    }

    @Test
    fun `zoom reports the target of a running animation`() {
        val state = state()
        state.setZoom(2f)

        state.resetZoom()

        assertClose(1f, state.zoom)
        assertClose(2f, state.liveZoom)
    }

    @Test
    fun `straighten reports the target of a running animation`() {
        val state = state()
        state.setStraighten(10f)

        state.resetStraighten()

        assertEquals(0f, state.straighten)
        assertClose(10f, state.liveStraighten)
    }

    @Test
    fun `resizing the stage leaves an unchanged crop with nothing to undo`() {
        val state = state()
        state.checkpoint()

        state.updateBounds(Rect(0f, 0f, 300f, 600f))

        assertFalse(state.canUndo)
    }

    @Test
    fun `a saved crop is restored with its history`() {
        val state = state(aspect = CropAspect.Ratio(4, 3))
        state.swapOrientation()
        state.flipHorizontally()
        state.setStraighten(7f)

        val restored = ImageCropState(state.spec, scope, clock).apply { restoreSaved(state.toSaved()) }

        assertEquals(state.result(), restored.result())
        assertTrue(restored.isOrientationSwapped)
        restored.undo()
        restored.undo()
        assertFalse(restored.isMirrored)
        assertFalse(restored.isOrientationSwapped)
    }

    @Test
    fun `a restored crop survives the stage reporting the same photo in new bounds`() {
        val state = state()
        state.rotateLeft()
        val saved = state.toSaved()

        val restored = ImageCropState(state.spec, scope, clock).apply {
            restoreSaved(saved)
            updateImage(Size(4000f, 3000f))
            updateBounds(Rect(0f, 0f, 800f, 300f))
        }

        assertEquals(-90f, assertNotNull(restored.result()).rotation)
        assertTrue(restored.canUndo)
    }

    @Test
    fun `a state saved before it was ready restores fresh`() {
        val unready = ImageCropState(CropSpec(), scope)

        val restored = ImageCropState(CropSpec(), scope).apply { restoreSaved(unready.toSaved()) }

        assertNull(restored.result())
    }

    /**
     * Asserts two floats match to within a hundredth.
     *
     * @param expected Value the test expects.
     * @param actual Value the code produced.
     */
    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(abs(expected - actual) <= 0.01f, "Expected $expected, was $actual")
    }
}
