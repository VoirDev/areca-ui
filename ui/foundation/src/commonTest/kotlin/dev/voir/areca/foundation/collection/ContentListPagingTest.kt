package dev.voir.areca.foundation.collection

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for [shouldLoadNextPage], which decides when a paged list asks for its next page. */
class ContentListPagingTest {
    @Test
    fun `a load finishing while still near the end requests the next page again`() {
        val whileLoading = shouldLoadNextPage(
            totalItemsCount = 20,
            lastVisibleItemIndex = 19,
            hasNextPage = true,
            isLoading = true,
            hasLoadMoreError = false,
        )
        val afterLoad = shouldLoadNextPage(
            totalItemsCount = 30,
            lastVisibleItemIndex = 29,
            hasNextPage = true,
            isLoading = false,
            hasLoadMoreError = false,
        )

        // Distinct values are what let the distinct-until-changed trigger fire after the load.
        assertEquals(listOf(false, true), listOf(whileLoading, afterLoad))
    }

    @Test
    fun `a first page that fits on screen requests the next page`() {
        assertTrue(
            shouldLoadNextPage(
                totalItemsCount = 5,
                lastVisibleItemIndex = 4,
                hasNextPage = true,
                isLoading = false,
                hasLoadMoreError = false,
            ),
        )
    }

    @Test
    fun `a list far from its end does not request a page`() {
        assertFalse(
            shouldLoadNextPage(
                totalItemsCount = 50,
                lastVisibleItemIndex = 10,
                hasNextPage = true,
                isLoading = false,
                hasLoadMoreError = false,
            ),
        )
    }

    @Test
    fun `a load-more error waits for an explicit retry`() {
        assertFalse(
            shouldLoadNextPage(
                totalItemsCount = 20,
                lastVisibleItemIndex = 19,
                hasNextPage = true,
                isLoading = false,
                hasLoadMoreError = true,
            ),
        )
    }

    @Test
    fun `the last page never requests another`() {
        assertFalse(
            shouldLoadNextPage(
                totalItemsCount = 20,
                lastVisibleItemIndex = 19,
                hasNextPage = false,
                isLoading = false,
                hasLoadMoreError = false,
            ),
        )
    }

    @Test
    fun `an empty list does not request a page`() {
        assertFalse(
            shouldLoadNextPage(
                totalItemsCount = 0,
                lastVisibleItemIndex = -1,
                hasNextPage = true,
                isLoading = false,
                hasLoadMoreError = false,
            ),
        )
    }
}
