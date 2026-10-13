package dev.voir.areca.flow.attachments.viewer

import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AttachmentViewerPageTest {

    @Test
    fun `a stated file's line adds its label and size under its name`() {
        val item = AttachmentItem(key = 1, fileName = "IMG_2041.jpg", label = "Receipt", size = "1.2 MB")

        assertEquals("Receipt · 1.2 MB", fileDetail(item))
    }

    @Test
    fun `a stated file's line does not repeat the name above it`() {
        val item = AttachmentItem(key = 1, fileName = "IMG_2041.jpg", size = "1.2 MB")

        assertEquals("1.2 MB", fileDetail(item))
    }

    @Test
    fun `a stated file with no label of its own and no size has no line`() {
        assertNull(fileDetail(AttachmentItem(key = 1, fileName = "IMG_2041.jpg")))
    }

    @Test
    fun `a download that stops is a stop`() {
        assertTrue(stoppedArriving(AttachmentState.Downloading(0.4f), AttachmentState.Waiting, wasStopped = false))
        assertTrue(stoppedArriving(AttachmentState.Remote, AttachmentState.Unavailable, wasStopped = false))
    }

    @Test
    fun `a file first drawn waiting has not stopped while watched`() {
        assertFalse(stoppedArriving(previous = null, current = AttachmentState.Waiting, wasStopped = false))
        assertFalse(stoppedArriving(AttachmentState.Ready, AttachmentState.Waiting, wasStopped = false))
    }

    @Test
    fun `a stop stays a stop while the file stays stopped`() {
        assertTrue(stoppedArriving(AttachmentState.Waiting, AttachmentState.Waiting, wasStopped = true))
        assertTrue(stoppedArriving(AttachmentState.Waiting, AttachmentState.Unavailable, wasStopped = true))
    }

    @Test
    fun `a file that arrives is no longer stopped`() {
        assertFalse(stoppedArriving(AttachmentState.Waiting, AttachmentState.Downloading(null), wasStopped = true))
    }
}
