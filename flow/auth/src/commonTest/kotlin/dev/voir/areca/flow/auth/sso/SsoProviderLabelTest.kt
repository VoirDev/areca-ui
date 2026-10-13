package dev.voir.areca.flow.auth.sso

import dev.voir.areca.flow.auth.AuthProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the English label a provider button carries when the application has not worded one.
 *
 * The same label is the text of a labelled button and the accessibility label of a mark, so a
 * provider without one would be an unnamed circle to a screen reader.
 */
class SsoProviderLabelTest {
    @Test
    fun `each provider is named after continue with`() {
        assertEquals("Continue with Apple", AuthProvider.Apple.defaultLabel)
        assertEquals("Continue with Google", AuthProvider.Google.defaultLabel)
        assertEquals("Continue with Facebook", AuthProvider.Facebook.defaultLabel)
        assertEquals("Continue with Email", AuthProvider.Email.defaultLabel)
    }

    @Test
    fun `every provider has a label of its own`() {
        val labels = AuthProvider.entries.map { it.defaultLabel }

        assertEquals(AuthProvider.entries.size, labels.toSet().size)
    }

    @Test
    fun `the providers are Apple Google Facebook and the way out to email`() {
        assertEquals(
            listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Facebook, AuthProvider.Email),
            AuthProvider.entries,
        )
    }
}
