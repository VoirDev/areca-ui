package dev.voir.areca.foundation.field

import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.indication.FadeIndication
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The order a field resolves everything that might apply into the one state it is drawn in.
 *
 * Every role reads this — background, border, value, placeholder, label, message — so the order
 * here is what stops a red border from appearing over a resting label.
 */
class FieldVisualStateTest {

    @Test
    fun `a disabled field says nothing else about itself`() {
        val state = fieldVisualState(
            status = FieldStatus.Error,
            enabled = false,
            readOnly = true,
            focused = true,
        )

        assertEquals(FieldVisualState.Disabled, state)
    }

    @Test
    fun `read-only outranks the status`() {
        val state = fieldVisualState(
            status = FieldStatus.Error,
            readOnly = true,
            focused = true,
        )

        assertEquals(FieldVisualState.ReadOnly, state)
    }

    @Test
    fun `a problem with the value outranks focus`() {
        assertEquals(
            FieldVisualState.Error,
            fieldVisualState(status = FieldStatus.Error, focused = true),
        )
        assertEquals(
            FieldVisualState.Warning,
            fieldVisualState(status = FieldStatus.Warning, focused = true),
        )
        assertEquals(
            FieldVisualState.Success,
            fieldVisualState(status = FieldStatus.Success, focused = true),
        )
    }

    @Test
    fun `focus shows when there is nothing to report`() {
        assertEquals(
            FieldVisualState.Focused,
            fieldVisualState(status = FieldStatus.Default, focused = true),
        )
    }

    @Test
    fun `an untouched field is at rest`() {
        assertEquals(FieldVisualState.Rest, fieldVisualState(status = FieldStatus.Default))
    }

    @Test
    fun `focus outranks hover`() {
        assertEquals(
            FieldVisualState.Focused,
            fieldVisualState(status = FieldStatus.Default, focused = true, hovered = true),
        )
    }

    @Test
    fun `hover shows only when nothing louder applies`() {
        assertEquals(
            FieldVisualState.Hovered,
            fieldVisualState(status = FieldStatus.Default, hovered = true),
        )
        assertEquals(
            FieldVisualState.Error,
            fieldVisualState(status = FieldStatus.Error, hovered = true),
        )
        assertEquals(
            FieldVisualState.Disabled,
            fieldVisualState(status = FieldStatus.Default, enabled = false, hovered = true),
        )
    }

    @Test
    fun `the focus ring answers focus while the status only colors it`() {
        val style = testStyle()

        assertEquals(
            Color.Transparent,
            resolveFocusRingColor(style, FieldVisualState.Rest, focused = false),
        )
        assertEquals(
            style.focusRing,
            resolveFocusRingColor(style, FieldVisualState.Focused, focused = true),
        )
        assertEquals(
            style.focusRingError,
            resolveFocusRingColor(style, FieldVisualState.Error, focused = true),
        )
        // A status does not summon a ring on a field nobody is in.
        assertEquals(
            Color.Transparent,
            resolveFocusRingColor(style, FieldVisualState.Error, focused = false),
        )
    }

    @Test
    fun `a field that takes no input never rings`() {
        val style = testStyle()

        assertEquals(
            Color.Transparent,
            resolveFocusRingColor(style, FieldVisualState.Disabled, focused = true),
        )
        assertEquals(
            Color.Transparent,
            resolveFocusRingColor(style, FieldVisualState.ReadOnly, focused = true),
        )
    }

    @Test
    fun `hover falls back to the resting color where a style does not distinguish it`() {
        val style = testStyle()

        assertEquals(style.bg, resolveBackgroundColor(style, FieldVisualState.Hovered))
        assertEquals(style.content, resolveContentColor(style, FieldVisualState.Hovered))
        assertEquals(style.placeholder, resolvePlaceholderColor(style, FieldVisualState.Hovered))
        assertEquals(
            style.label.content,
            style.label.colorFor(FieldVisualState.Hovered),
        )
    }

    @Test
    fun `a message reports the value and ignores the interaction`() {
        val style = FieldMessageStyle(
            content = Color.Black,
            contentDisabled = Color.Gray,
            contentError = Color.Red,
        )

        assertEquals(style.content, style.colorFor(FieldVisualState.Focused))
        assertEquals(style.content, style.colorFor(FieldVisualState.ReadOnly))
        assertEquals(style.contentError, style.colorFor(FieldVisualState.Error))
        assertEquals(style.contentDisabled, style.colorFor(FieldVisualState.Disabled))
    }
}

/**
 * A palette whose roles are all distinct colors, so a resolver that reads the wrong one is caught
 * by the value rather than by a coincidence of two tokens agreeing.
 *
 * @return Style with a hover that is not distinguished and a ring that is.
 */
private fun testStyle() = FieldStyle(
    bg = Color.White,
    border = Color.Gray,
    content = Color.Black,
    placeholder = Color.LightGray,
    extra = Color.DarkGray,
    spinner = Color.Blue,
    focusRing = Color.Blue.copy(alpha = 0.2f),
    focusRingError = Color.Red.copy(alpha = 0.2f),
    shape = RectangleShape,
    label = FieldLabelStyle(content = Color.Gray, required = Color.Red),
    message = FieldMessageStyle(content = Color.Gray),
    iconButton = IconButtonStyle(
        bg = Color.Transparent,
        border = Color.Transparent,
        content = Color.Black,
        shape = RectangleShape,
        indication = FadeIndication(),
    ),
)
