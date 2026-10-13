package dev.voir.areca.foundation.tabs

import androidx.compose.animation.core.snap
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [TabsIndicator], the position a tab strip draws its indicator at. */
class TabsIndicatorTest {
    @Test
    fun `the first position is taken in place rather than swept in from the edge`() = runTest {
        val indicator = TabsIndicator()

        indicator.moveTo(TabBounds(start = 120f, end = 200f), animationSpec = snap())

        assertEquals(TabBounds(start = 120f, end = 200f), indicator.bounds())
    }

    @Test
    fun `nothing selected draws no indicator`() = runTest {
        val indicator = TabsIndicator()
        indicator.moveTo(TabBounds(start = 120f, end = 200f), animationSpec = snap())

        indicator.moveTo(null, animationSpec = snap())

        assertNull(indicator.bounds())
    }

    @Test
    fun `a selection after none appears in place too`() = runTest {
        val indicator = TabsIndicator()
        indicator.moveTo(TabBounds(start = 0f, end = 80f), animationSpec = snap())
        indicator.moveTo(null, animationSpec = snap())

        indicator.moveTo(TabBounds(start = 300f, end = 360f), animationSpec = snap())

        assertEquals(TabBounds(start = 300f, end = 360f), indicator.bounds())
    }
}
