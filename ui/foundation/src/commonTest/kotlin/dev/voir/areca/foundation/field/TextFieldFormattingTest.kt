package dev.voir.areca.foundation.field

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Caret behavior of the display-only formatter behind [TextField]'s `format` parameter.
 */
class TextFieldFormattingTest {

    /** Groups digits the way a phone number is usually written. */
    private val phoneFormat: (String) -> String = { raw ->
        buildString {
            raw.forEachIndexed { index, char ->
                when (index) {
                    3 -> append(") ")
                    6 -> append('-')
                }
                if (index == 0) append('(')
                append(char)
            }
        }
    }

    @Test
    fun `formatter decorates the displayed text without touching the value`() {
        val result = FormatVisualTransformation(phoneFormat).filter(AnnotatedString("1234567890"))

        assertEquals("(123) 456-7890", result.text.text)
    }

    @Test
    fun `an unchanged value maps offsets one to one`() {
        val result = FormatVisualTransformation { it }.filter(AnnotatedString("1234"))

        assertSame(OffsetMapping.Identity, result.offsetMapping)
    }

    @Test
    fun `the caret lands past a separator that was inserted before the next character`() {
        val mapping = FormatVisualTransformation(phoneFormat)
            .filter(AnnotatedString("1234567890"))
            .offsetMapping

        // After three digits the display reads "(123", and the next character typed belongs
        // after the ") " the formatter inserted — not between the digits and the bracket.
        assertEquals("(123) ".length, mapping.originalToTransformed(3))
    }

    @Test
    fun `offsets round trip back to the raw value`() {
        val raw = "1234567890"
        val mapping = FormatVisualTransformation(phoneFormat)
            .filter(AnnotatedString(raw))
            .offsetMapping

        for (offset in 0..raw.length) {
            assertEquals(
                offset,
                mapping.transformedToOriginal(mapping.originalToTransformed(offset)),
                "offset $offset did not round trip",
            )
        }
    }

    @Test
    fun `the start of an empty value sits after a decoration the formatter opened with`() {
        val mapping = FormatVisualTransformation(phoneFormat)
            .filter(AnnotatedString("123"))
            .offsetMapping

        // The opening bracket is decoration, so there is nothing useful about a caret in front
        // of it: offset zero in the raw value is the first position the user can type at.
        assertEquals(1, mapping.originalToTransformed(0))
    }

    @Test
    fun `out of range offsets are clamped rather than thrown`() {
        val mapping = FormatVisualTransformation(phoneFormat)
            .filter(AnnotatedString("123"))
            .offsetMapping

        assertEquals(mapping.originalToTransformed(0), mapping.originalToTransformed(-5))
        assertTrue(mapping.originalToTransformed(99) <= "(123".length)
        assertEquals(3, mapping.transformedToOriginal(99))
    }

    @Test
    fun `chaining with None returns the other transformation unchanged`() {
        val formatting = FormatVisualTransformation(phoneFormat)

        assertSame(formatting, formatting.then(VisualTransformation.None))
        assertSame(formatting, VisualTransformation.None.then(formatting))
    }

    @Test
    fun `chained transformations apply in order`() {
        val upper = VisualTransformation { text ->
            androidx.compose.ui.text.input.TransformedText(
                AnnotatedString(text.text.uppercase()),
                OffsetMapping.Identity,
            )
        }

        val chained = FormatVisualTransformation { "<${it}>" }.then(upper)

        assertEquals("<AB>", chained.filter(AnnotatedString("ab")).text.text)
    }
}
