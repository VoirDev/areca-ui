package dev.voir.areca.viewmodel

import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Admits at most one user-requested refresh at a time and suppresses rapid repeats after it ends.
 *
 * A reservation is made synchronously before an action is dispatched, closing the interval in
 * which several UI gestures could otherwise enter the action queue before refreshing state is
 * rendered. Rejected requests are discarded rather than delayed or replayed.
 *
 * Every successful [tryReserve] must reach exactly one [runReserved]. [BaseViewModel.dispatch]
 * never drops an action, so dispatching the refresh action from [tryReserve] satisfies this while
 * the ViewModel is alive.
 *
 * Each refresh owner should keep its own instance so unrelated screens do not block one another.
 *
 * @property cooldown Minimum interval between one refresh completing and another being accepted.
 * @property timeSource Monotonic time source used to measure the cooldown.
 */
public class RefreshGate(
    private val cooldown: Duration = DEFAULT_COOLDOWN,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) {
    /** Defaults shared by every gate. */
    public companion object {
        /** Default protection interval after a user-requested refresh completes. */
        public val DEFAULT_COOLDOWN: Duration = 1.seconds
    }

    /** Held from a successful [tryReserve] until the matching [runReserved] finishes. */
    private val reservation = Mutex()

    /** When the last admitted refresh finished, or `null` before the first one finishes. */
    private var completedAt: TimeMark? = null

    /**
     * Reserves the gate and invokes [onAccepted] only when a refresh may be dispatched.
     *
     * The reservation remains held until [runReserved] completes. If dispatching throws, the
     * reservation is released immediately because no refresh will reach [runReserved].
     *
     * @param onAccepted Synchronous callback that starts or dispatches the accepted refresh.
     * @return `true` when the refresh was accepted, or `false` when one is active or cooling down.
     * @throws Throwable when [onAccepted] throws; the reservation is released before rethrowing.
     */
    public fun tryReserve(onAccepted: () -> Unit): Boolean {
        if (!reservation.tryLock()) return false

        val lastCompletion = completedAt
        if (lastCompletion != null && lastCompletion.elapsedNow() < cooldown) {
            reservation.unlock()
            return false
        }

        try {
            onAccepted()
        } catch (throwable: Throwable) {
            reservation.unlock()
            throw throwable
        }
        return true
    }

    /**
     * Runs an accepted refresh and releases its reservation after success, failure, or cancellation.
     *
     * Call this exactly once for the refresh started by a successful [tryReserve]. The cooldown
     * begins when [block] finishes. [block]'s result or failure is passed through unchanged.
     *
     * @param T Result type of [block].
     * @param block Refresh operation associated with the preceding successful [tryReserve].
     * @return The refresh operation's result.
     * @throws IllegalStateException when no reservation is held; [block] is then not run and the
     * cooldown does not restart.
     */
    public suspend fun <T> runReserved(block: suspend () -> T): T {
        check(reservation.isLocked) { "runReserved was called without a successful tryReserve." }

        return try {
            block()
        } finally {
            completedAt = timeSource.markNow()
            reservation.unlock()
        }
    }
}
