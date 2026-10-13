package dev.voir.areca.foundation.navigation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for [isShownExpanded], the rule that decides whether a [NavList] section shows its children. */
class NavSectionExpansionTest {
    private fun section(expandable: Boolean, initiallyExpanded: Boolean) = NavNode.Section(
        trigger = NavData(id = "settings", title = "Settings"),
        children = listOf(NavData(id = "profile", title = "Profile")),
        expandable = expandable,
        initiallyExpanded = initiallyExpanded,
    )

    @Test
    fun `a non-expandable section shows its children without being expanded first`() {
        val fixed = section(expandable = false, initiallyExpanded = false)

        assertTrue(fixed.isShownExpanded(toggledState = null))
    }

    @Test
    fun `an untouched expandable section follows its initial expansion`() {
        assertFalse(section(expandable = true, initiallyExpanded = false).isShownExpanded(toggledState = null))
        assertTrue(section(expandable = true, initiallyExpanded = true).isShownExpanded(toggledState = null))
    }

    @Test
    fun `a toggled expandable section follows the toggle over its initial expansion`() {
        val collapsedByDefault = section(expandable = true, initiallyExpanded = false)

        assertTrue(collapsedByDefault.isShownExpanded(toggledState = true))
    }
}
