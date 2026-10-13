package dev.voir.areca.foundation.table

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for the row indices and counts a table announces to assistive technology. */
class TableSemanticsTest {
    @Test
    fun `the header is row zero and body rows follow it`() {
        assertEquals(0, TableHeaderRowIndex)
        assertEquals(1, tableBodyRowIndex(itemIndex = 0))
        assertEquals(5, tableBodyRowIndex(itemIndex = 4))
    }

    @Test
    fun `the row count includes the header`() {
        assertEquals(1, tableRowCount(itemCount = 0))
        assertEquals(4, tableRowCount(itemCount = 3))
    }

    @Test
    fun `a column is as wide as its widest channel`() {
        assertEquals(120, widestOf(listOf(80, 120)))
        assertEquals(80, widestOf(listOf(80, null)))
    }

    @Test
    fun `an unmeasured column has no natural width`() {
        assertNull(widestOf(listOf(null, null)))
        assertNull(widestOf(listOf(0, null)))
    }
}
