package dev.voir.areca.foundation.overlay

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Configuration checks and visibility of a [FlowModalController]. */
class FlowModalControllerTest {

    @Test
    fun `a height fraction outside zero to one is rejected at construction`() {
        assertFailsWith<IllegalArgumentException> { FlowModalController(maxHeightFraction = -0.1f) }
        assertFailsWith<IllegalArgumentException> { FlowModalController(maxHeightFraction = 1.5f) }
    }

    @Test
    fun `a height fraction outside zero to one is rejected when set`() {
        val controller = FlowModalController(maxHeightFraction = 0.9f)

        assertFailsWith<IllegalArgumentException> { controller.maxHeightFraction = 2f }
        assertEquals(0.9f, controller.maxHeightFraction)
    }

    @Test
    fun `the bounds of the height fraction are accepted`() {
        val controller = FlowModalController(maxHeightFraction = 0f)

        controller.maxHeightFraction = 1f

        assertEquals(1f, controller.maxHeightFraction)
    }

    @Test
    fun `a modal is visible from the moment it starts opening`() {
        val controller = FlowModalController()
        assertFalse(controller.isVisible)

        controller.show()

        assertTrue(controller.isVisible)
    }
}
