package dev.voir.areca.foundation.collection

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Tests for [flattenGroupedGrid], which turns grouped data into the rows of a grouped grid. */
class GroupedGridFlatteningTest {
    @Test
    fun `each group is its header followed by its items in order`() {
        val rows = flattenGroupedGrid(
            linkedMapOf(
                "Fruit" to listOf("Apple", "Pear"),
                "Veg" to listOf("Leek"),
            )
        )

        assertEquals(
            expected = listOf(
                GroupedGridItem.Header("Fruit"),
                GroupedGridItem.Entry("Apple"),
                GroupedGridItem.Entry("Pear"),
                GroupedGridItem.Header("Veg"),
                GroupedGridItem.Entry("Leek"),
            ),
            actual = rows,
        )
    }

    @Test
    fun `an empty group still contributes its header`() {
        val rows = flattenGroupedGrid(linkedMapOf("Empty" to emptyList<String>(), "Full" to listOf("One")))

        assertEquals(
            expected = listOf(
                GroupedGridItem.Header("Empty"),
                GroupedGridItem.Header("Full"),
                GroupedGridItem.Entry("One"),
            ),
            actual = rows,
        )
    }

    @Test
    fun `groups follow the map iteration order`() {
        val rows = flattenGroupedGrid(linkedMapOf(3 to listOf("c"), 1 to listOf("a"), 2 to listOf("b")))

        assertEquals(
            expected = listOf(3, 1, 2),
            actual = rows.filterIsInstance<GroupedGridItem.Header<Int>>().map { it.group },
        )
    }

    @Test
    fun `no groups flatten to no rows`() {
        assertTrue(flattenGroupedGrid(emptyMap<String, List<String>>()).isEmpty())
    }
}
