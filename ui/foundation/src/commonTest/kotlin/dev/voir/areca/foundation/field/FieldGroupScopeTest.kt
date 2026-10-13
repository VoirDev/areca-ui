package dev.voir.areca.foundation.field

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

/**
 * What a [FieldGroup] tells its control to announce, and what it reports as invalid.
 */
class FieldGroupScopeTest {

    @Test
    fun `an external label names the control`() {
        val scope = scope(label = "Email")

        assertEquals("Email", scope.contentDescription)
    }

    @Test
    fun `a required external label is read with the required word`() {
        val scope = scope(label = "Email", required = true, requiredDescription = "obligatoire")

        assertEquals("Email, obligatoire", scope.contentDescription)
    }

    @Test
    fun `the required glyph is never read aloud`() {
        val scope = scope(label = "Email", required = true, requiredText = "*")

        assertEquals("Email, required", scope.contentDescription)
        assertEquals("*", scope.label?.requiredText)
    }

    @Test
    fun `an internal label does not name the control again`() {
        val inside = scope(label = "Email", labelPlacement = FieldLabelPlacement.Inside)
        val leading = scope(label = "Email", labelPlacement = FieldLabelPlacement.InsideLeading())

        assertNull(inside.contentDescription)
        assertNull(leading.contentDescription)
        assertEquals("Email", inside.label?.text)
    }

    @Test
    fun `a group without a label names nothing`() {
        val scope = scope(label = null)

        assertNull(scope.contentDescription)
        assertNull(scope.label)
    }

    @Test
    fun `an error under the error status is reported as invalid input`() {
        val scope = scope(error = "Enter an email address", status = FieldStatus.Error)

        assertEquals("Enter an email address", scope.errorDescription)
    }

    @Test
    fun `a message under another status is not reported as invalid input`() {
        val warning = scope(error = "Check the domain", status = FieldStatus.Warning)
        val success = scope(error = "Looks good", status = FieldStatus.Success)

        assertNull(warning.errorDescription)
        assertNull(success.errorDescription)
    }

    @Test
    fun `the scope carries the status the enabled flag and the focus handle`() {
        val focus = FieldFocus()
        val scope = scope(status = FieldStatus.Warning, enabled = false, focus = focus)

        assertEquals(FieldStatus.Warning, scope.status)
        assertFalse(scope.enabled)
        assertSame(focus, scope.focus)
    }

    /**
     * Resolves a scope with the group's defaults for everything a test does not name.
     */
    private fun scope(
        label: CharSequence? = "Email",
        required: Boolean = false,
        requiredText: String = "*",
        requiredDescription: String = "required",
        error: CharSequence? = null,
        status: FieldStatus = if (error != null) FieldStatus.Error else FieldStatus.Default,
        labelPlacement: FieldLabelPlacement = FieldLabelPlacement.Above,
        enabled: Boolean = true,
        focus: FieldFocus? = null,
    ): FieldScope = fieldGroupScope(
        label = label,
        required = required,
        requiredText = requiredText,
        requiredDescription = requiredDescription,
        error = error,
        status = status,
        labelPlacement = labelPlacement,
        enabled = enabled,
        focus = focus,
    )
}
