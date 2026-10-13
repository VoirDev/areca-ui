package dev.voir.areca.foundation.selection

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertSame

/** Tests for [SelectionStyle.resolve] and [ToggleStyle.resolve]. */
class SelectionStyleResolveTest {
    private fun colors(seed: Int) = SelectionColors(
        bg = Color(seed),
        border = Color(seed + 1),
        mark = Color(seed + 2),
    )

    private val checkbox = CheckboxStyle(
        unchecked = colors(10),
        checked = colors(20),
        disabledUnchecked = colors(30),
        disabledChecked = colors(40),
        readOnlyUnchecked = colors(50),
        readOnlyChecked = colors(60),
    )

    private val toggle = ToggleStyle(
        off = colors(10),
        indeterminate = colors(20),
        on = colors(30),
        disabled = colors(40),
        readOnly = colors(50),
    )

    @Test
    fun `an editable control takes its checked or unchecked colors`() {
        assertSame(
            checkbox.checked,
            checkbox.resolve(selected = true, enabled = true, readOnly = false),
        )
        assertSame(
            checkbox.unchecked,
            checkbox.resolve(selected = false, enabled = true, readOnly = false),
        )
    }

    @Test
    fun `an unavailable control keeps its state in the disabled colors`() {
        assertSame(
            checkbox.disabledChecked,
            checkbox.resolve(selected = true, enabled = false, readOnly = false),
        )
        assertSame(
            checkbox.disabledUnchecked,
            checkbox.resolve(selected = false, enabled = false, readOnly = false),
        )
    }

    @Test
    fun `a read-only control keeps its state in the read-only colors`() {
        assertSame(
            checkbox.readOnlyChecked,
            checkbox.resolve(selected = true, enabled = true, readOnly = true),
        )
        assertSame(
            checkbox.readOnlyUnchecked,
            checkbox.resolve(selected = false, enabled = true, readOnly = true),
        )
    }

    @Test
    fun `disabled wins over read-only for a selection control`() {
        assertSame(
            checkbox.disabledChecked,
            checkbox.resolve(selected = true, enabled = false, readOnly = true),
        )
    }

    @Test
    fun `an editable toggle takes the colors of its state`() {
        assertSame(toggle.off, toggle.resolve(ToggleState.Off, enabled = true, readOnly = false))
        assertSame(
            toggle.indeterminate,
            toggle.resolve(ToggleState.Indeterminate, enabled = true, readOnly = false),
        )
        assertSame(toggle.on, toggle.resolve(ToggleState.On, enabled = true, readOnly = false))
    }

    @Test
    fun `an unavailable or read-only toggle takes one slot whatever its state`() {
        val on = ToggleState.On

        assertSame(toggle.disabled, toggle.resolve(on, enabled = false, readOnly = false))
        assertSame(toggle.readOnly, toggle.resolve(on, enabled = true, readOnly = true))
    }

    @Test
    fun `disabled wins over read-only for a toggle`() {
        val off = ToggleState.Off

        assertSame(toggle.disabled, toggle.resolve(off, enabled = false, readOnly = true))
    }
}
