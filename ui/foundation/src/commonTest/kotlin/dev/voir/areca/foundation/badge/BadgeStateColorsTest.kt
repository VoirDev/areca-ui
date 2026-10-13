package dev.voir.areca.foundation.badge

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

/** Which of a [BadgeStyle]'s filled, soft and disabled colors a badge draws. */
class BadgeStateColorsTest {

    private val style = BadgeStyle(
        bg = Color.Red,
        bgDisabled = Color.Gray,
        bgSoft = Color.Yellow,
        content = Color.White,
        contentDisabled = Color.DarkGray,
        contentSoft = Color.Black,
        border = Color.Blue,
        borderDisabled = Color.LightGray,
        borderSoft = Color.Cyan,
    )

    @Test
    fun `an enabled badge draws its filled colors`() {
        assertEquals(Color.Red, style.bgFor(soft = false, enabled = true))
        assertEquals(Color.White, style.contentFor(soft = false, enabled = true))
        assertEquals(Color.Blue, style.borderFor(soft = false, enabled = true))
    }

    @Test
    fun `a soft badge draws its soft colors`() {
        assertEquals(Color.Yellow, style.bgFor(soft = true, enabled = true))
        assertEquals(Color.Black, style.contentFor(soft = true, enabled = true))
        assertEquals(Color.Cyan, style.borderFor(soft = true, enabled = true))
    }

    @Test
    fun `disabled wins over soft`() {
        assertEquals(Color.Gray, style.bgFor(soft = true, enabled = false))
        assertEquals(Color.DarkGray, style.contentFor(soft = true, enabled = false))
        assertEquals(Color.LightGray, style.borderFor(soft = true, enabled = false))
    }
}
