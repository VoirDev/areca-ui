package dev.voir.areca.flow.attachments.sheet

import kotlin.test.Test
import kotlin.test.assertEquals

class SplitFileNameTest {

    @Test
    fun `the extension is kept apart with its dot`() {
        assertEquals("Invoice INV-2026-0412" to ".pdf", splitFileName("Invoice INV-2026-0412.pdf"))
    }

    @Test
    fun `only the last extension is kept`() {
        assertEquals("export.tar" to ".gz", splitFileName("export.tar.gz"))
    }

    @Test
    fun `a name without an extension is edited whole`() {
        assertEquals("Lease agreement" to "", splitFileName("Lease agreement"))
    }

    @Test
    fun `a name with a dot in it is not cut at the dot`() {
        assertEquals(
            "Invoice 12.03.2026 corrected" to "",
            splitFileName("Invoice 12.03.2026 corrected"),
        )
    }

    @Test
    fun `a leading dot is part of the name`() {
        assertEquals(".env" to "", splitFileName(".env"))
    }

    @Test
    fun `the extension keeps its case`() {
        assertEquals("IMG_2041" to ".JPG", splitFileName("IMG_2041.JPG"))
    }
}
