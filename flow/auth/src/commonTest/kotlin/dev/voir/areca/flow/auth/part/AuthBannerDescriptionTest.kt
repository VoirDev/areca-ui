package dev.voir.areca.flow.auth.part

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for what a screen reader says for a failure banner the application has not worded.
 */
class AuthBannerDescriptionTest {
    @Test
    fun `a banner without a title reads its message alone`() {
        assertEquals("Wrong password.", defaultBannerDescription(title = null, message = "Wrong password."))
    }

    @Test
    fun `a titled banner reads its title as a sentence before the message`() {
        assertEquals(
            "Too many attempts. Wait 2 minutes.",
            defaultBannerDescription(title = "Too many attempts", message = "Wait 2 minutes."),
        )
    }
}
