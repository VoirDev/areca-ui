package dev.voir.areca.flow.profile.sheet

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfirmationMatchTest {

    @Test
    fun `the exact phrase confirms`() {
        assertTrue(confirmationMatches(typed = "DELETE", phrase = "DELETE"))
    }

    @Test
    fun `case is ignored`() {
        assertTrue(confirmationMatches(typed = "delete", phrase = "DELETE"))
    }

    @Test
    fun `surrounding spaces are ignored`() {
        assertTrue(confirmationMatches(typed = "  DELETE ", phrase = "DELETE"))
    }

    @Test
    fun `a partial phrase does not confirm`() {
        assertFalse(confirmationMatches(typed = "DELE", phrase = "DELETE"))
    }

    @Test
    fun `an empty box does not confirm`() {
        assertFalse(confirmationMatches(typed = "", phrase = "DELETE"))
    }
}
