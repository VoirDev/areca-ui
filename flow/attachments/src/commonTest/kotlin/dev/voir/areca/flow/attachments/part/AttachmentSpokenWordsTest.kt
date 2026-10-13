package dev.voir.areca.flow.attachments.part

import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AttachmentSpokenWordsTest {

    @Test
    fun `a square is named by its label and its file name`() {
        val item = AttachmentItem(key = 1, fileName = "IMG_2041.jpg", label = "Receipt")

        assertEquals("Receipt, IMG_2041.jpg", defaultDescription(item))
    }

    @Test
    fun `a square whose label is its name says the name once`() {
        assertEquals("IMG_2041.jpg", defaultDescription(AttachmentItem(key = 1, fileName = "IMG_2041.jpg")))
    }

    @Test
    fun `a file that is here says nothing about its bytes`() {
        assertNull(defaultStateDescription(AttachmentState.Ready))
    }

    @Test
    fun `a transfer says how far it has come when that is known`() {
        assertEquals("Uploading, 40%", defaultStateDescription(AttachmentState.Uploading(progress = 0.4f)))
        assertEquals("Downloading", defaultStateDescription(AttachmentState.Downloading(progress = null)))
    }
}
