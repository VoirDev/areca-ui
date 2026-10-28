package dev.voir.areca.media.viewer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.AccessibilityManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/** Tests for the geometry of the viewer's transition and how long its notice stays. */
class MediaViewerTest {
    /** Accessibility settings that answer with [answer] and record whether controls were reported. */
    private class FakeAccessibilityManager(private val answer: (Long) -> Long) : AccessibilityManager {
        var askedControls: Boolean? = null

        override fun calculateRecommendedTimeoutMillis(
            originalTimeoutMillis: Long,
            containsIcons: Boolean,
            containsText: Boolean,
            containsControls: Boolean,
        ): Long {
            askedControls = containsControls
            return answer(originalTimeoutMillis)
        }
    }

    private val page = Rect(Offset.Zero, Size(400f, 800f))

    @Test
    fun `a picture with no shape of its own fills its bounds`() {
        assertEquals(page, fitted(page, aspectRatio = null))
    }

    @Test
    fun `a square picture fits the width of a tall page and is centred`() {
        assertEquals(Rect(Offset(0f, 200f), Size(400f, 400f)), fitted(page, aspectRatio = 1f))
    }

    @Test
    fun `a tall picture fits the height of a wide frame and is centred`() {
        val frame = Rect(Offset(100f, 50f), Size(300f, 100f))
        assertEquals(Rect(Offset(225f, 50f), Size(50f, 100f)), fitted(frame, aspectRatio = 0.5f))
    }

    @Test
    fun `without accessibility settings a notice keeps its own duration`() {
        assertEquals(3.seconds, noticeTimeout(3.seconds, accessibilityManager = null))
    }

    @Test
    fun `the platform recommendation lengthens the notice`() {
        val manager = FakeAccessibilityManager { original -> original * 4 }

        assertEquals(12_000.milliseconds, noticeTimeout(3.seconds, manager))
        assertEquals(false, manager.askedControls)
    }

    @Test
    fun `an unbounded recommendation keeps the notice until it is cleared`() {
        val manager = FakeAccessibilityManager { Long.MAX_VALUE }

        assertNull(noticeTimeout(3.seconds, manager))
    }
}
