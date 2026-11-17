package dev.voir.areca.foundation.list

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

/** Re-inking a row's value and chevron moves every state of that ink, not the rest color alone. */
class ListItemStyleTest {
    private val base = ListItemStyle(
        bg = Color.Transparent,
        bgHovered = Color.Gray,
        bgPressed = Color.DarkGray,
        bgSelected = Color.LightGray,
        bgDisabled = Color.Transparent,
        title = Color.Black,
        titleDisabled = Color.Gray,
        subtitle = Color.Gray,
        subtitleDisabled = Color.Gray,
        overline = Color.Gray,
        overlineDisabled = Color.Gray,
        value = Color.Gray,
        valueActive = Color.Black,
        valueSelected = Color.Black,
        valueDisabled = Color.LightGray,
        extra = Color.Gray,
        extraDisabled = Color.LightGray,
    )

    @Test
    fun `copy moves the value at rest alone - the trap the helper removes`() {
        val copied = base.copy(value = Color.Red)

        assertEquals(Color.Black, copied.valueActive)
        assertEquals(Color.Black, copied.valueSelected)
    }

    @Test
    fun `withValueColor moves the value in every state but disabled`() {
        val style = base.withValueColor(Color.Red)

        assertEquals(Color.Red, style.value)
        assertEquals(Color.Red, style.valueActive)
        assertEquals(Color.Red, style.valueSelected)
        assertEquals(Color.LightGray, style.valueDisabled)
    }

    @Test
    fun `withValueColor takes a state of its own where given`() {
        val style = base.withValueColor(Color.Red, active = Color.Blue)

        assertEquals(Color.Red, style.value)
        assertEquals(Color.Blue, style.valueActive)
        assertEquals(Color.Red, style.valueSelected)
    }

    @Test
    fun `the chevron follows the trailing slot unless given an ink`() {
        assertEquals(Color.Unspecified, base.chevron)
        assertEquals(Color.Unspecified, base.chevronActive)
        assertEquals(Color.Unspecified, base.chevronSelected)
        assertEquals(Color.Unspecified, base.chevronDisabled)
    }

    @Test
    fun `withChevronColor sets the chevron and leaves the slots`() {
        val style = base.withChevronColor(Color.Red, active = Color.Blue)

        assertEquals(Color.Red, style.chevron)
        assertEquals(Color.Blue, style.chevronActive)
        assertEquals(Color.Red, style.chevronSelected)
        // Still fades with its row's slots.
        assertEquals(Color.Unspecified, style.chevronDisabled)
        assertEquals(base.extra, style.extra)
        assertEquals(base.extraActive, style.extraActive)
    }
}
