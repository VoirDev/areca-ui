package dev.voir.areca.foundation.overlay

import kotlinx.coroutines.test.runTest
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class RunModalActionTest {

    @Test
    fun `a failing action reports its exception instead of throwing`() = runTest {
        val failure = IllegalStateException("offline")
        var reported: Exception? = null

        runModalAction(action = { throw failure }, onFailure = { reported = it })

        assertSame(failure, reported)
    }

    @Test
    fun `a successful action reports nothing`() = runTest {
        var runs = 0
        var reported: Exception? = null

        runModalAction(action = { runs++ }, onFailure = { reported = it })

        assertEquals(1, runs)
        assertNull(reported)
    }

    @Test
    fun `cancellation is rethrown rather than reported`() = runTest {
        var reported: Exception? = null

        assertFailsWith<CancellationException> {
            runModalAction(
                action = { throw CancellationException("left the composition") },
                onFailure = { reported = it },
            )
        }
        assertNull(reported)
    }
}
