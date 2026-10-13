package dev.voir.areca.foundation.feedback

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals

class SkeletonLineBoxTest {
    @Test
    fun `a line height in sp is the line box`() {
        assertEquals(20.sp, lineBoxHeight(fontSize = 14.sp, lineHeight = 20.sp))
    }

    @Test
    fun `a line height in em scales the font size`() {
        assertEquals(21.sp, lineBoxHeight(fontSize = 14.sp, lineHeight = 1.5.em))
    }

    @Test
    fun `an unspecified line height leaves the box at the font size`() {
        assertEquals(14.sp, lineBoxHeight(fontSize = 14.sp, lineHeight = TextUnit.Unspecified))
    }

    @Test
    fun `a line height tighter than the font keeps the box at the font size`() {
        assertEquals(16.sp, lineBoxHeight(fontSize = 16.sp, lineHeight = 12.sp))
    }
}
