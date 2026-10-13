package dev.voir.areca.foundation.tabs

import androidx.compose.runtime.saveable.SaverScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [tabsStateSaver]. */
class TabsStateSaverTest {
    /** Selection value the string-only registry cannot store. */
    private sealed interface Destination {
        /** The only destination these tests need. */
        data object Inbox : Destination
    }

    /** Stands in for a registry that, like Android's, stores strings but not arbitrary objects. */
    private val stringsOnly = SaverScope { it is String }

    @Test
    fun `a selection the registry can store is saved and restored`() {
        val saver = tabsStateSaver<String>()

        val saved = with(saver) { stringsOnly.save(TabsState("archive")) }
        val restored = saved?.let(saver::restore)

        assertEquals("archive", restored?.selectedValue)
    }

    @Test
    fun `a selection the registry cannot store is not handed to it`() {
        val saver = tabsStateSaver<Destination>()

        val saved = with(saver) { stringsOnly.save(TabsState(Destination.Inbox)) }

        assertNull(saved)
    }
}
