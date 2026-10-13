package dev.voir.areca.flow.attachments

import kotlin.test.Test
import kotlin.test.assertEquals

class AttachmentFormatTest {

    @Test
    fun `an image type is a picture whatever the name`() {
        assertEquals(AttachmentFormat.Image, AttachmentFormat.of("image/jpeg", "scan"))
    }

    @Test
    fun `the PDF type is a PDF`() {
        assertEquals(AttachmentFormat.Pdf, AttachmentFormat.of("application/pdf", "invoice"))
    }

    @Test
    fun `type parameters and case are ignored`() {
        assertEquals(AttachmentFormat.Pdf, AttachmentFormat.of("Application/PDF; charset=binary", "invoice"))
    }

    @Test
    fun `a specific type other than a picture or a PDF is a document`() {
        assertEquals(AttachmentFormat.Document, AttachmentFormat.of("text/plain", "notes.pdf"))
    }

    @Test
    fun `the generic type falls back to the extension`() {
        assertEquals(AttachmentFormat.Pdf, AttachmentFormat.of("application/octet-stream", "statement.PDF"))
    }

    @Test
    fun `a missing type falls back to the extension`() {
        assertEquals(AttachmentFormat.Image, AttachmentFormat.of(null, "IMG_2041.HEIC"))
    }

    @Test
    fun `nothing to go on is a document`() {
        assertEquals(AttachmentFormat.Document, AttachmentFormat.of(null, "contract"))
    }
}
