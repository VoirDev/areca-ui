package dev.voir.areca.viewmodel

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

/** Verifies refresh admission, release, cooldown, and per-owner isolation. */
class RefreshGateTest {
    @Test
    fun `rapid requests reserve one refresh`() = runTest {
        val gate = RefreshGate(cooldown = Duration.ZERO)
        var acceptedRequests = 0

        repeat(20) {
            gate.tryReserve { acceptedRequests += 1 }
        }

        assertEquals(1, acceptedRequests)
        gate.runReserved { }
    }

    @Test
    fun `requests are dropped while accepted refresh is suspended`() = runTest {
        val gate = RefreshGate(cooldown = Duration.ZERO)
        assertTrue(gate.tryReserve { })

        val refresh = launch {
            gate.runReserved { awaitCancellation() }
        }
        yield()

        assertFalse(gate.tryReserve { })
        refresh.cancelAndJoin()
        assertTrue(gate.tryReserve { })
        gate.runReserved { }
    }

    @Test
    fun `failed refresh releases reservation`() = runTest {
        val gate = RefreshGate(cooldown = Duration.ZERO)
        assertTrue(gate.tryReserve { })

        assertFailsWith<IllegalStateException> {
            gate.runReserved { error("Refresh failed") }
        }

        assertTrue(gate.tryReserve { })
        gate.runReserved { }
    }

    @Test
    fun `dispatch failure releases reservation`() = runTest {
        val gate = RefreshGate(cooldown = Duration.ZERO)

        assertFailsWith<IllegalStateException> {
            gate.tryReserve { error("Dispatch failed") }
        }

        assertTrue(gate.tryReserve { })
        gate.runReserved { }
    }

    @Test
    fun `refresh is accepted when cooldown elapses`() = runTest {
        val timeSource = TestTimeSource()
        val gate = RefreshGate(cooldown = 1.seconds, timeSource = timeSource)
        assertTrue(gate.tryReserve { })
        gate.runReserved { }

        assertFalse(gate.tryReserve { })
        timeSource += 999.milliseconds
        assertFalse(gate.tryReserve { })
        timeSource += 1.milliseconds
        assertTrue(gate.tryReserve { })
        gate.runReserved { }
    }

    @Test
    fun `runReserved without a reservation fails before running the block`() = runTest {
        val gate = RefreshGate(cooldown = 1.seconds)
        var ran = false

        assertFailsWith<IllegalStateException> {
            gate.runReserved { ran = true }
        }

        assertFalse(ran)
        assertTrue(gate.tryReserve { })
        gate.runReserved { }
    }

    @Test
    fun `runReserved passes the block failure through`() = runTest {
        val gate = RefreshGate(cooldown = Duration.ZERO)
        assertTrue(gate.tryReserve { })

        assertFailsWith<ArithmeticException> {
            gate.runReserved { throw ArithmeticException("Refresh failed") }
        }
    }

    @Test
    fun `different gates admit refreshes independently`() = runTest {
        val first = RefreshGate(cooldown = Duration.ZERO)
        val second = RefreshGate(cooldown = Duration.ZERO)

        assertTrue(first.tryReserve { })
        assertTrue(second.tryReserve { })

        first.runReserved { }
        second.runReserved { }
    }
}
