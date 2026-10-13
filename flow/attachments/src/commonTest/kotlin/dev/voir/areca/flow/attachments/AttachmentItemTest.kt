package dev.voir.areca.flow.attachments

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AttachmentItemTest {

    @Test
    fun `the extension is printed in capitals`() {
        assertEquals("PDF", item("invoice.pdf").extension)
    }

    @Test
    fun `only the last dot counts`() {
        assertEquals("GZ", item("export.tar.gz").extension)
    }

    @Test
    fun `a name without a dot has no extension`() {
        assertNull(item("contract").extension)
    }

    @Test
    fun `a leading dot is not an extension`() {
        assertNull(item(".env").extension)
    }

    @Test
    fun `a trailing dot is not an extension`() {
        assertNull(item("draft.").extension)
    }

    @Test
    fun `a long trailing part is a name with a dot in it`() {
        assertNull(item("Invoice 12.03.2026 corrected").extension)
    }

    @Test
    fun `the label defaults to the file name`() {
        assertEquals("receipt.jpg", item("receipt.jpg").label)
    }

    private fun item(fileName: String) = AttachmentItem(key = fileName, fileName = fileName)
}
