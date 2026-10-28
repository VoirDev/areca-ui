package dev.voir.areca.flow.attachments.viewer

import dev.voir.areca.flow.attachments.AttachmentItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AttachmentViewerTest {

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
}
