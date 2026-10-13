package dev.voir.areca.foundation.tabbar

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [TabBarState], which keeps the selection on an enabled value. */
class TabBarStateTest {
    @Test
    fun `enabled value becomes selected`() {
        val state = TabBarState(
            initialSelectedValue = "home",
            initialEnabledValues = listOf("home", "profile"),
        )

        state.select("profile")

        assertEquals("profile", state.selectedValue)
    }

    @Test
    fun `disabled value is ignored`() {
        val state = TabBarState(
            initialSelectedValue = "home",
            initialEnabledValues = listOf("home"),
        )

        state.select("profile")

        assertEquals("home", state.selectedValue)
    }

    @Test
    fun `first enabled value is selected when current value disappears`() {
        val state = TabBarState(
            initialSelectedValue = "home",
            initialEnabledValues = listOf("home", "profile"),
        )

        state.updateEnabledValues(listOf("profile"))

        assertEquals("profile", state.selectedValue)
    }

    @Test
    fun `selection is cleared when no values are enabled`() {
        val state = TabBarState(
            initialSelectedValue = "home",
            initialEnabledValues = listOf("home"),
        )

        state.updateEnabledValues(emptyList())

        assertEquals(null, state.selectedValue)
    }
}
