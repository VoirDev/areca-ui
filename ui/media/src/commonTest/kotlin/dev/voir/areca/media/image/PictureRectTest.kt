package dev.voir.areca.media.image

import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for where a picture lies in its frame, which pins hotspots to it. */
class PictureRectTest {
    private val frame = Size(400f, 400f)

    @Test
    fun `a whole wide picture sits centred with bands above and below`() {
        assertEquals(Triple(0f, Size(400f, 200f), 100f), pictureRect(Size(1200f, 600f), frame, MediaFit.Whole))
    }

    @Test
    fun `a filling wide picture overhangs the frame's sides`() {
        assertEquals(Triple(-200f, Size(800f, 400f), 0f), pictureRect(Size(1200f, 600f), frame, MediaFit.Fill))
    }

    @Test
    fun `a picture with no size fills the frame`() {
        assertEquals(Triple(0f, frame, 0f), pictureRect(Size.Unspecified, frame, MediaFit.Whole))
    }
}
