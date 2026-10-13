package dev.voir.areca.foundation.field.select

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Behavior of [SelectFlowModalDraft], the selection a [SelectFlowModal] edits before the caller
 * applies it.
 */
class SelectFlowModalDraftTest {

    private val apple = option("apple")
    private val banana = option("banana")

    @Test
    fun `a new draft starts at the committed selection`() {
        val draft = SelectFlowModalDraft(setOf(apple.value))

        assertEquals(setOf(apple.value), draft.values)
    }

    @Test
    fun `selecting updates the draft and reports it`() {
        val draft = SelectFlowModalDraft(setOf(apple.value))

        val reported = draft.select(banana, SelectSelectionMode.Multiple)

        assertEquals(setOf(apple.value, banana.value), reported)
        assertEquals(reported, draft.values)
    }

    @Test
    fun `discarding drops unapplied edits so a reopened sheet shows the committed selection`() {
        val draft = SelectFlowModalDraft(setOf(apple.value))
        draft.select(banana, SelectSelectionMode.Multiple)
        draft.select(apple, SelectSelectionMode.Multiple)

        draft.discard()

        assertEquals(setOf(apple.value), draft.values)
    }

    private fun option(value: String): SelectOption<String> =
        SelectOption(key = value, value = value, label = value)
}
