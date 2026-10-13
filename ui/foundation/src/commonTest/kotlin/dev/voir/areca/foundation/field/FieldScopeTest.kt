package dev.voir.areca.foundation.field

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

/**
 * Which label a container draws, and what a label tap does when nothing is listening.
 */
class FieldScopeTest {

    @Test
    fun `no scope leaves the container with nothing to draw`() {
        assertNull(null.internalLabel())
    }

    @Test
    fun `an external placement leaves the label to the group`() {
        val scope = FieldScope(
            label = FieldLabel("Email"),
            labelPlacement = FieldLabelPlacement.Above,
        )

        assertNull(scope.internalLabel())
    }

    @Test
    fun `a beside placement leaves the label to the group`() {
        val scope = FieldScope(
            label = FieldLabel("Email"),
            labelPlacement = FieldLabelPlacement.Beside(width = 120.dp),
        )

        assertNull(scope.internalLabel())
    }

    @Test
    fun `an internal placement hands the label to the container`() {
        val label = FieldLabel("Email", required = true)
        val scope = FieldScope(label = label, labelPlacement = FieldLabelPlacement.Inside)

        val internal = scope.internalLabel()

        assertEquals(label, internal?.first)
        assertEquals(FieldLabelPlacement.Inside, internal?.second)
    }

    @Test
    fun `an internal placement with no label draws nothing`() {
        val scope = FieldScope(
            label = null,
            labelPlacement = FieldLabelPlacement.InsideLeading(valueAlignment = Alignment.End),
        )

        assertNull(scope.internalLabel())
    }

    @Test
    fun `tapping a label no control claimed is ignored rather than fatal`() {
        val focus = FieldFocus()

        assertFalse(focus.request())
    }
}
