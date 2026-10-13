package dev.voir.areca.foundation.settings

import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsTextFieldRowLengthTest {

    // U+1F600, one code point written as a surrogate pair.
    private val grin = "😀"

    @Test
    fun `plain text is cut at the limit`() {
        assertEquals("abc", "abcdef".takeCodePoints(3))
    }

    @Test
    fun `text within the limit is kept whole`() {
        assertEquals("ab", "ab".takeCodePoints(5))
    }

    @Test
    fun `a zero limit keeps nothing`() {
        assertEquals("", "abc".takeCodePoints(0))
    }

    @Test
    fun `a surrogate pair counts as one character`() {
        assertEquals("a${grin}", "a${grin}b".takeCodePoints(2))
    }

    @Test
    fun `a cut never splits a surrogate pair`() {
        val cut = "${grin}${grin}".takeCodePoints(1)

        assertEquals(grin, cut)
        assertEquals(2, cut.length)
    }

    @Test
    fun `a lone surrogate counts as one character`() {
        assertEquals("\uD83D", "\uD83Dx".takeCodePoints(1))
    }

    @Test
    fun `length counts code points`() {
        assertEquals(3, "a${grin}b".codePointLength())
        assertEquals(0, "".codePointLength())
    }
}
