package dev.voir.areca.flow.auth.part

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests for how a legal-sentence template is split into words and the two links.
 *
 * The template is what lets a translation put the documents in its own order, so the split has to
 * follow the placeholders wherever they are, and refuse a sentence that would drop a document.
 */
class LegalSentenceTest {
    @Test
    fun `the default sentence puts the terms before the privacy policy`() {
        assertEquals(
            listOf(
                LegalSentencePart.Words("By continuing, you agree to our "),
                LegalSentencePart.Terms,
                LegalSentencePart.Words(" and "),
                LegalSentencePart.Privacy,
                LegalSentencePart.Words("."),
            ),
            legalSentenceParts("By continuing, you agree to our %1\$s and %2\$s."),
        )
    }

    @Test
    fun `a translation can name the privacy policy first`() {
        assertEquals(
            listOf(
                LegalSentencePart.Words("Lies die "),
                LegalSentencePart.Privacy,
                LegalSentencePart.Words(" und die "),
                LegalSentencePart.Terms,
                LegalSentencePart.Words("."),
            ),
            legalSentenceParts("Lies die %2\$s und die %1\$s."),
        )
    }

    @Test
    fun `placeholders at the edges leave no empty words`() {
        assertEquals(
            listOf(LegalSentencePart.Terms, LegalSentencePart.Words(" · "), LegalSentencePart.Privacy),
            legalSentenceParts("%1\$s · %2\$s"),
        )
    }

    @Test
    fun `other percent signs are kept as written`() {
        assertEquals(
            LegalSentencePart.Words("100% yours: "),
            legalSentenceParts("100% yours: %1\$s, %2\$s").first(),
        )
    }

    @Test
    fun `a sentence missing a document is refused`() {
        assertFailsWith<IllegalArgumentException> { legalSentenceParts("You agree to our %1\$s.") }
    }

    @Test
    fun `a sentence naming a document twice is refused`() {
        assertFailsWith<IllegalArgumentException> { legalSentenceParts("%1\$s, %2\$s and %1\$s") }
    }
}
