package dev.voir.areca.foundation.layout

import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [startEndInsets], which maps physical window insets onto the reading direction. */
class BaseLayoutInsetsTest {
    @Test
    fun `left to right keeps the left inset at the start`() {
        assertEquals(
            expected = StartEndInsets(start = 24, end = 8),
            actual = startEndInsets(left = 24, right = 8, layoutDirection = LayoutDirection.Ltr),
        )
    }

    @Test
    fun `right to left moves the left inset to the end`() {
        assertEquals(
            expected = StartEndInsets(start = 8, end = 24),
            actual = startEndInsets(left = 24, right = 8, layoutDirection = LayoutDirection.Rtl),
        )
    }
}
