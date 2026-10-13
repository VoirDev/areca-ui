package dev.voir.areca.foundation.feedback

import kotlin.test.Test
import kotlin.test.assertEquals

/** What a [RequirementList] reads after each rule. */
class RequirementListLabelsTest {

    private val labels = RequirementListLabels(met = "erfüllt", notMet = "nicht erfüllt")

    @Test
    fun `a met rule reads the met label`() {
        assertEquals("erfüllt", labels.describe(RequirementState.Met))
    }

    @Test
    fun `pending and unmet rules read the same label`() {
        assertEquals("nicht erfüllt", labels.describe(RequirementState.Pending))
        assertEquals("nicht erfüllt", labels.describe(RequirementState.Unmet))
    }
}
