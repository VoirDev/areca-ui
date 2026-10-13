package dev.voir.areca.flow.attachments.part

import kotlin.test.Test
import kotlin.test.assertEquals

class AttachmentTileTest {

    @Test
    fun `a tile is read with its second line after its name`() {
        assertEquals("Receipt, IMG_2041.jpg, 1.2 MB", spokenTile("Receipt, IMG_2041.jpg", detail = "1.2 MB"))
    }

    @Test
    fun `a tile with no second line is read by its name alone`() {
        assertEquals("Receipt", spokenTile("Receipt", detail = null))
        assertEquals("Receipt", spokenTile("Receipt", detail = ""))
    }
}
