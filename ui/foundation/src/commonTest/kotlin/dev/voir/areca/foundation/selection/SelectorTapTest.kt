package dev.voir.areca.foundation.selection

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [resolveSelectorTap], what a tap on a [SlidingSelector] option does. */
class SelectorTapTest {
    private fun item(value: String, enabled: Boolean = true) =
        SelectorItem(value = value, enabled = enabled) {}

    @Test
    fun `a tap on another option selects it`() {
        assertEquals(
            SelectorTap.Select("avoid"),
            resolveSelectorTap(item("avoid"), selected = "has", mode = SelectorMode.Required),
        )
    }

    @Test
    fun `a required selection is never cleared by tapping the chosen option`() {
        assertEquals(
            SelectorTap.Ignore,
            resolveSelectorTap(item("has"), selected = "has", mode = SelectorMode.Required),
        )
    }

    @Test
    fun `an optional selection is cleared by tapping the chosen option again`() {
        assertEquals(
            SelectorTap.Select(null),
            resolveSelectorTap(item("has"), selected = "has", mode = SelectorMode.Optional),
        )
    }

    @Test
    fun `a tap on an option with nothing selected selects it`() {
        assertEquals(
            SelectorTap.Select("has"),
            resolveSelectorTap(item("has"), selected = null, mode = SelectorMode.Optional),
        )
    }

    @Test
    fun `a disabled option does nothing in either mode`() {
        for (mode in SelectorMode.entries) {
            assertEquals(
                SelectorTap.Ignore,
                resolveSelectorTap(item("avoid", enabled = false), selected = "has", mode = mode),
            )
            assertEquals(
                SelectorTap.Ignore,
                resolveSelectorTap(item("has", enabled = false), selected = "has", mode = mode),
            )
        }
    }
}
