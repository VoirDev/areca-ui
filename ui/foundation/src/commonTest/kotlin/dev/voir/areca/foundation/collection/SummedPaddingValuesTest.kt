package dev.voir.areca.foundation.collection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [plusPadding], which stacks a paged list's own inset and the caller's. */
class SummedPaddingValuesTest {
    @Test
    fun `every side is the sum of both paddings`() {
        val sum = PaddingValues(start = 1.dp, top = 2.dp, end = 3.dp, bottom = 4.dp)
            .plusPadding(PaddingValues(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp))

        assertEquals(
            listOf(11.dp, 22.dp, 33.dp, 44.dp),
            listOf(
                sum.calculateLeftPadding(LayoutDirection.Ltr),
                sum.calculateTopPadding(),
                sum.calculateRightPadding(LayoutDirection.Ltr),
                sum.calculateBottomPadding(),
            ),
        )
    }

    @Test
    fun `start insets add on the right in a right-to-left layout`() {
        val sum = PaddingValues(start = 1.dp).plusPadding(PaddingValues(start = 10.dp))

        assertEquals(
            listOf(0.dp, 11.dp),
            listOf(
                sum.calculateLeftPadding(LayoutDirection.Rtl),
                sum.calculateRightPadding(LayoutDirection.Rtl),
            ),
        )
    }
}
