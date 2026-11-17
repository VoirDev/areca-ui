package dev.voir.areca.flow.attachments.sheet

import androidx.compose.ui.graphics.Color
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Camera
import dev.voir.areca.theme.ArecaHues
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** What a bento card reads off a choice: its description and the hue it falls back to. */
class AttachmentChoiceTest {
    private val hues = ArecaHues()

    @Test
    fun `a choice of the product's own carries no description or hue unless given`() {
        val choice = AttachmentChoice("Scan a document", BundledIcons.Camera, {})

        assertNull(choice.description)
        assertEquals(Color.Unspecified, choice.hue)
        assertNull(choice.defaultHue)
    }

    @Test
    fun `the module's sources fall back to blue then purple then orange`() {
        val camera = AttachmentSourceOptions.camera {}
        val library = AttachmentSourceOptions.library {}
        val files = AttachmentSourceOptions.files {}

        assertEquals(Color.Unspecified, camera.hue)
        assertEquals(hues.blue, assertNotNull(camera.defaultHue).invoke(hues))
        assertEquals(hues.purple, assertNotNull(library.defaultHue).invoke(hues))
        assertEquals(hues.orange, assertNotNull(files.defaultHue).invoke(hues))
    }

    @Test
    fun `the module's sources carry English descriptions that can be replaced or dropped`() {
        assertNotNull(AttachmentSourceOptions.camera {}.description)
        assertEquals("Receipts", AttachmentSourceOptions.library(description = "Receipts") {}.description)
        assertNull(AttachmentSourceOptions.files(description = null) {}.description)
    }

    @Test
    fun `a hue given to a source wins over its default`() {
        assertEquals(Color.Red, AttachmentSourceOptions.camera(hue = Color.Red) {}.hue)
    }
}
