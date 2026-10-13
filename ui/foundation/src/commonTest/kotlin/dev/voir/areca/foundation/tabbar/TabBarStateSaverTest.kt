package dev.voir.areca.foundation.tabbar

import androidx.compose.runtime.saveable.SaverScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [tabBarStateSaver]. */
class TabBarStateSaverTest {
    /**
     * Selection value the string-only registry cannot store.
     *
     * @property route Route the destination stands for.
     */
    private data class Destination(val route: String)

    /** Stands in for a registry that, like Android's, stores strings but not arbitrary objects. */
    private val stringsOnly = SaverScope { it is String }

    @Test
    fun `a storable selection is saved and restored`() {
        val enabled = listOf("home", "inbox")
        val saver = tabBarStateSaver(enabled)
        val state = TabBarState(initialSelectedValue = "inbox", initialEnabledValues = enabled)

        val saved = with(saver) { stringsOnly.save(state) }
        val restored = saved?.let(saver::restore)

        assertEquals("inbox", restored?.selectedValue)
    }

    @Test
    fun `a selection the registry cannot store is not handed to it`() {
        val enabled = listOf(Destination("home"))
        val saver = tabBarStateSaver(enabled)
        val state = TabBarState(initialSelectedValue = Destination("home"), initialEnabledValues = enabled)

        val saved = with(saver) { stringsOnly.save(state) }

        assertNull(saved)
    }

    @Test
    fun `a restored selection that is no longer enabled falls back to the first enabled value`() {
        val saver = tabBarStateSaver(listOf("home", "settings"))

        val restored = saver.restore("inbox")

        assertEquals("home", restored?.selectedValue)
    }
}
