package dev.voir.areca.foundation.selection

import androidx.compose.animation.core.tween
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Tests for [SelectorThumbState], where a selector draws its thumb and how it gets there. */
class SelectorThumbStateTest {
    /** A frame clock that advances 16ms a frame and counts the frames an animation asked for. */
    private class CountingFrameClock : MonotonicFrameClock {
        var frames = 0
            private set

        override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
            frames++
            return onFrame(frames * 16_000_000L)
        }
    }

    /** A spec long enough that any move it drives takes many frames. */
    private val slow = tween<Float>(durationMillis = 300)

    private fun target(index: Int, start: Float, end: Float, color: Color = Color.Black) =
        SelectorThumbTarget(index = index, bounds = SelectorBounds(start, end), color = color)

    /**
     * Runs [block] with a fresh thumb and a counting frame clock.
     *
     * @param block Test body, given the thumb and the clock.
     */
    private fun withThumb(block: suspend (SelectorThumbState, CountingFrameClock) -> Unit) = runTest {
        val clock = CountingFrameClock()
        withContext(clock) { block(SelectorThumbState(), clock) }
    }

    @Test
    fun `the first position is taken in place so a screen never opens with a slide`() = withThumb { thumb, clock ->
        thumb.moveTo(target(index = 2, start = 120f, end = 200f), slow, reduceMotion = false)

        assertEquals(SelectorBounds(start = 120f, end = 200f), thumb.bounds())
        assertEquals(1f, thumb.alpha)
        assertEquals(1f, thumb.scale)
        assertEquals(0, clock.frames)
    }

    @Test
    fun `opening with nothing selected draws no thumb`() = withThumb { thumb, clock ->
        thumb.moveTo(null, slow, reduceMotion = false)

        assertNull(thumb.bounds())
        assertEquals(0, clock.frames)
    }

    @Test
    fun `a move to another option slides there and takes its colour`() = withThumb { thumb, clock ->
        thumb.moveTo(target(index = 0, start = 0f, end = 80f, color = Color.Black), slow, reduceMotion = false)

        thumb.moveTo(target(index = 1, start = 80f, end = 200f, color = Color.Red), slow, reduceMotion = false)

        assertTrue(clock.frames > 1)
        assertEquals(SelectorBounds(start = 80f, end = 200f), thumb.bounds())
        assertEquals(Color.Red, thumb.color)
    }

    @Test
    fun `a choice after none grows in at the chosen option`() = withThumb { thumb, clock ->
        thumb.moveTo(null, slow, reduceMotion = false)

        thumb.moveTo(target(index = 1, start = 300f, end = 360f), slow, reduceMotion = false)

        assertTrue(clock.frames > 1)
        assertEquals(SelectorBounds(start = 300f, end = 360f), thumb.bounds())
        assertEquals(1f, thumb.alpha)
        assertEquals(1f, thumb.scale)
    }

    @Test
    fun `clearing the selection shrinks the thumb away`() = withThumb { thumb, clock ->
        thumb.moveTo(target(index = 0, start = 0f, end = 80f), slow, reduceMotion = false)

        thumb.moveTo(null, slow, reduceMotion = false)

        assertTrue(clock.frames > 1)
        assertNull(thumb.bounds())
    }

    @Test
    fun `a new measurement of the same option follows without animating`() = withThumb { thumb, clock ->
        thumb.moveTo(target(index = 0, start = 0f, end = 80f), slow, reduceMotion = false)

        thumb.moveTo(target(index = 0, start = 0f, end = 140f), slow, reduceMotion = false)

        assertEquals(SelectorBounds(start = 0f, end = 140f), thumb.bounds())
        assertEquals(0, clock.frames)
    }

    @Test
    fun `with reduced motion a move ends at the new place fully shown`() = withThumb { thumb, _ ->
        thumb.moveTo(target(index = 0, start = 0f, end = 80f), slow, reduceMotion = true)

        thumb.moveTo(target(index = 2, start = 200f, end = 260f), slow, reduceMotion = true)

        assertEquals(SelectorBounds(start = 200f, end = 260f), thumb.bounds())
        assertEquals(1f, thumb.alpha)
    }
}
