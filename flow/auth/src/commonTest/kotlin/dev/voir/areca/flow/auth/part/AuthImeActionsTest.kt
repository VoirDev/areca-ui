package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Tests for where a credential box sends the keyboard's action.
 *
 * A box nobody chained must keep the platform's behaviour — Next moves focus on, Done closes the
 * keyboard — and a box given a callback must run that callback instead, whatever action the
 * keyboard offers.
 */
class AuthImeActionsTest {
    /** Records the platform actions a handler falls back to. */
    private class RecordingScope : KeyboardActionScope {
        /** Platform actions requested so far, in order. */
        val defaults = mutableListOf<ImeAction>()

        override fun defaultKeyboardAction(imeAction: ImeAction) {
            defaults += imeAction
        }
    }

    @Test
    fun `without a callback the platform handles every action`() {
        assertSame(KeyboardActions.Default, imeActions(onImeAction = null))
    }

    @Test
    fun `a callback runs for each action the keyboard can offer`() {
        var calls = 0
        val actions = imeActions(onImeAction = { calls++ })
        val handlers = listOf(
            actions.onDone,
            actions.onGo,
            actions.onNext,
            actions.onPrevious,
            actions.onSearch,
            actions.onSend,
        )
        val scope = RecordingScope()

        handlers.forEach { handler -> handler?.invoke(scope) }

        assertEquals(handlers.size, calls)
    }

    @Test
    fun `a callback replaces the platform action`() {
        val actions = imeActions(onImeAction = {})
        val scope = RecordingScope()

        actions.onNext?.invoke(scope)
        actions.onDone?.invoke(scope)

        assertTrue(scope.defaults.isEmpty())
    }
}
