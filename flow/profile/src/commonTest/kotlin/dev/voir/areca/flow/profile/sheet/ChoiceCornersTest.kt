package dev.voir.areca.flow.profile.sheet

import kotlin.test.Test
import kotlin.test.assertEquals

/** Which corners of the picture sheet's block each choice rounds. */
class ChoiceCornersTest {

    @Test
    fun `a lone choice rounds every corner`() {
        assertEquals(ChoiceCorners.All, choiceCorners(index = 0, count = 1))
    }

    @Test
    fun `the first and last of several round their own edge`() {
        assertEquals(ChoiceCorners.Top, choiceCorners(index = 0, count = 3))
        assertEquals(ChoiceCorners.Bottom, choiceCorners(index = 2, count = 3))
    }

    @Test
    fun `a choice between two others is square`() {
        assertEquals(ChoiceCorners.None, choiceCorners(index = 1, count = 3))
    }

    @Test
    fun `two choices split the corners between them`() {
        assertEquals(ChoiceCorners.Top, choiceCorners(index = 0, count = 2))
        assertEquals(ChoiceCorners.Bottom, choiceCorners(index = 1, count = 2))
    }
}
