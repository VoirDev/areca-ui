package dev.voir.areca.foundation.field

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for how an [OtpField] turns whatever arrives in its editor into a code, and which cell it
 * rings.
 *
 * Input reaches a code field by typing, by pasting a whole message's worth of text, and by the
 * keyboard offering a code it read from a notification. All three land in the same filter, so the
 * filter is what has to be right.
 */
class OtpFieldInputTest {
    @Test
    fun `a pasted code keeps its digits and drops the separator`() {
        assertEquals("123456", otpCode("123-456", length = 6, filter = Char::isDigit))
    }

    @Test
    fun `input longer than the field is cut at its length`() {
        assertEquals("1234", otpCode("123456", length = 4, filter = Char::isDigit))
    }

    @Test
    fun `a sentence around the code yields only the code`() {
        assertEquals(
            "482913",
            otpCode("Your code is 482913. It expires in 10 minutes.", length = 6, filter = Char::isDigit),
        )
    }

    @Test
    fun `a letter typed into a numeric field is refused`() {
        assertEquals("12", otpCode("12a", length = 6, filter = Char::isDigit))
    }

    @Test
    fun `an alphanumeric filter keeps letters and digits in order`() {
        assertEquals("AB12CD", otpCode("AB-12-CD", length = 6, filter = Char::isLetterOrDigit))
    }

    @Test
    fun `the next empty cell takes the next character`() {
        assertEquals(0, otpActiveIndex(filled = 0, length = 6))
        assertEquals(3, otpActiveIndex(filled = 3, length = 6))
    }

    @Test
    fun `a full code rings the last cell rather than one past it`() {
        assertEquals(5, otpActiveIndex(filled = 6, length = 6))
    }
}
