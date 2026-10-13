package dev.voir.areca.flow.attachments.viewer

import androidx.compose.ui.platform.AccessibilityManager
import dev.voir.areca.flow.attachments.AttachmentItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class AttachmentViewerTest {

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

    @Test
    fun `the caption under a labelled file states its name and size`() {
        val item = AttachmentItem(key = 1, fileName = "IMG_2041.jpg", label = "Receipt", size = "1.2 MB")

        assertEquals("IMG_2041.jpg · 1.2 MB", viewerCaption(item))
    }

    @Test
    fun `the caption does not repeat a name the heading already shows`() {
        val item = AttachmentItem(key = 1, fileName = "IMG_2041.jpg", size = "1.2 MB")

        assertEquals("1.2 MB", viewerCaption(item))
    }

    @Test
    fun `a file with nothing to add to its heading has no caption`() {
        assertNull(viewerCaption(AttachmentItem(key = 1, fileName = "IMG_2041.jpg")))
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
