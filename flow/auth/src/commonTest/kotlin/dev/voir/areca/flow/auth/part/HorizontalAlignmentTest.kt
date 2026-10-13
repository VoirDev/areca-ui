package dev.voir.areca.flow.auth.part

import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for where a header's mark and a resend line sit for a given text alignment.
 *
 * Start and end follow the layout direction; left and right do not, so a right-to-left layout must
 * not mirror them.
 */
class HorizontalAlignmentTest {
    @Test
    fun `start and end follow the layout direction`() {
        assertEquals(Alignment.Start, TextAlign.Start.toHorizontalAlignment())
        assertEquals(Alignment.End, TextAlign.End.toHorizontalAlignment())
    }

    @Test
    fun `left and right stay on their side in any layout direction`() {
        assertEquals(AbsoluteAlignment.Left, TextAlign.Left.toHorizontalAlignment())
        assertEquals(AbsoluteAlignment.Right, TextAlign.Right.toHorizontalAlignment())
    }

    @Test
    fun `centre and justify are centred`() {
        assertEquals(Alignment.CenterHorizontally, TextAlign.Center.toHorizontalAlignment())
        assertEquals(Alignment.CenterHorizontally, TextAlign.Justify.toHorizontalAlignment())
    }
}
