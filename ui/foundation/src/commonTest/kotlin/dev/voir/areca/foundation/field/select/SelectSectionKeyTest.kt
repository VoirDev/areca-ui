package dev.voir.areca.foundation.field.select

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/** Lazy keys of a sectioned select list: strings, so Android can save them, and never alike by accident. */
class SelectSectionKeyTest {

    @Test
    fun `one option listed in two sections gets two keys`() {
        assertNotEquals(optionKey("popular", "Apple"), optionKey("all", "Apple"))
    }

    @Test
    fun `keys whose parts contain the separator do not collide`() {
        assertNotEquals(optionKey("a:b", "c"), optionKey("a", "b:c"))
        assertNotEquals(letterHeaderKey("a:b", "C", 0), letterHeaderKey("a", "b:C", 0))
    }

    @Test
    fun `header and letter and option keys never share a string`() {
        val keys = listOf(sectionHeaderKey("all"), letterHeaderKey("all", "A", 0), optionKey("all", "A"))
        assertEquals(keys.size, keys.toSet().size)
    }

    @Test
    fun `an unsorted section heading two runs with one letter keys them apart`() {
        assertNotEquals(letterHeaderKey("all", "B", 0), letterHeaderKey("all", "B", 2))
    }
}
