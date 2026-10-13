package dev.voir.areca.foundation.navigation

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for how a [NavList] saves expansion and how a [NavItem] picks its colors. */
class NavExpansionSaverTest {
    @Test
    fun `toggled sections survive a save and restore`() {
        val toggled = mapOf("reports" to true, "settings" to false)

        assertEquals(toggled, navExpansionFromSaveable(navExpansionToSaveable(toggled)))
    }

    @Test
    fun `nothing toggled saves and restores as nothing toggled`() {
        assertEquals(emptyMap(), navExpansionFromSaveable(navExpansionToSaveable(emptyMap())))
    }

    @Test
    fun `malformed saved pairs are skipped`() {
        assertEquals(
            mapOf("reports" to true),
            navExpansionFromSaveable(listOf("reports", true, 42, "settings", "orphan")),
        )
    }

    @Test
    fun `a disabled row takes its disabled color even while selected`() {
        val picked = navItemColor(
            enabled = false,
            selected = true,
            rest = Color.Black,
            disabled = Color.Gray,
            selectedColor = Color.Blue,
        )

        assertEquals(Color.Gray, picked)
    }

    @Test
    fun `an available row takes its selected or resting color`() {
        assertEquals(Color.Blue, navItemColor(true, true, Color.Black, Color.Gray, Color.Blue))
        assertEquals(Color.Black, navItemColor(true, false, Color.Black, Color.Gray, Color.Blue))
    }
}
