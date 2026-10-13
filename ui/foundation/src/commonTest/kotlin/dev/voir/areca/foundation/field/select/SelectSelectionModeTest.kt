package dev.voir.areca.foundation.field.select

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Behavior of [applyTo], the rule both select controls share for turning a tapped option into
 * the next selection.
 */
class SelectSelectionModeTest {

    private val apple = option("apple")
    private val banana = option("banana")

    @Test
    fun `single select replaces the previous choice`() {
        val result = SelectSelectionMode.Single.applyTo(setOf(apple.value), banana)

        assertEquals(setOf(banana.value), result)
    }

    @Test
    fun `single select clears when the current choice is tapped again`() {
        val result = SelectSelectionMode.Single.applyTo(setOf(apple.value), apple)

        assertEquals(emptySet(), result)
    }

    @Test
    fun `multi select adds an unselected option`() {
        val result = SelectSelectionMode.Multiple.applyTo(setOf(apple.value), banana)

        assertEquals(setOf(apple.value, banana.value), result)
    }

    @Test
    fun `multi select removes an already selected option`() {
        val result = SelectSelectionMode.Multiple.applyTo(setOf(apple.value, banana.value), apple)

        assertEquals(setOf(banana.value), result)
    }

    @Test
    fun `applying leaves the previous selection untouched`() {
        val previous = setOf(apple.value)

        SelectSelectionMode.Multiple.applyTo(previous, banana)

        assertEquals(setOf(apple.value), previous)
    }

    private fun option(value: String) = SelectOption(
        key = value,
        value = value,
        label = value.replaceFirstChar { it.uppercase() },
    )
}
