package dev.voir.areca.media.image

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Where a peeked picture is drawn as it grows, relative to its frame and the window. */
class PeekCentringTest {
    private val window = Size(400f, 800f)

    // A full-width picture at the top of the page, as on a product page.
    private val topFrame = Rect(left = 0f, top = 0f, right = 400f, bottom = 300f)

    @Test
    fun `at the fitted size the picture sits on its frame`() {
        assertEquals(Offset.Zero, peekCentring(topFrame, window, scale = 1f, maxScale = 5f))
    }

    @Test
    fun `a growing picture at the top moves its centre toward the window's centre`() {
        val start = distanceToWindowCentre(scale = 1f)
        val halfway = distanceToWindowCentre(scale = 1.5f)
        val grown = distanceToWindowCentre(scale = 2f)

        assertTrue(halfway < start)
        assertTrue(grown < halfway)
        assertEquals(0f, grown, absoluteTolerance = 0.01f)
    }

    @Test
    fun `growing further keeps the picture at the window's centre`() {
        assertEquals(
            peekCentring(topFrame, window, scale = 2f, maxScale = 5f),
            peekCentring(topFrame, window, scale = 4f, maxScale = 5f),
        )
    }

    @Test
    fun `a picture that cannot grow is not moved`() {
        assertEquals(Offset.Zero, peekCentring(topFrame, window, scale = 1f, maxScale = 1f))
    }

    /** @return How far the drawn picture's centre is from the window's at [scale]. */
    private fun distanceToWindowCentre(scale: Float): Float {
        val drawnCentre = topFrame.center + peekCentring(topFrame, window, scale, maxScale = 5f)
        return (window.center - drawnCentre).getDistance()
    }
}
