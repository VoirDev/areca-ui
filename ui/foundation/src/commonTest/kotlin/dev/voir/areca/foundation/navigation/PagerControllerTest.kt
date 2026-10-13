package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.pager.PagerState
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Tests for [PagerController] and the rule that decides where its moves land. */
class PagerControllerTest {
    private fun TestScope.controller(currentPage: Int, pageCount: Int) = PagerController(
        pagerState = PagerState(currentPage = currentPage) { pageCount },
        scope = this,
    )

    @Test
    fun `the first page reports itself as first and not last`() = runTest {
        val pager = controller(currentPage = 0, pageCount = 3)

        assertTrue(pager.isFirstPage)
        assertFalse(pager.isLastPage)
        assertEquals(3, pager.pageCount)
    }

    @Test
    fun `the last page reports itself as last`() = runTest {
        val pager = controller(currentPage = 2, pageCount = 3)

        assertTrue(pager.isLastPage)
        assertFalse(pager.isFirstPage)
    }

    @Test
    fun `a single page is both first and last`() = runTest {
        val pager = controller(currentPage = 0, pageCount = 1)

        assertTrue(pager.isFirstPage)
        assertTrue(pager.isLastPage)
    }

    @Test
    fun `a move to one of the pages lands on it`() {
        assertEquals(0, pagerMoveTarget(page = 0, pageCount = 3))
        assertEquals(2, pagerMoveTarget(page = 2, pageCount = 3))
    }

    @Test
    fun `a move past either end is ignored rather than clamped`() {
        assertNull(pagerMoveTarget(page = 3, pageCount = 3))
        assertNull(pagerMoveTarget(page = -1, pageCount = 3))
    }

    @Test
    fun `no move lands in a pager without pages`() {
        assertNull(pagerMoveTarget(page = 0, pageCount = 0))
    }
}
