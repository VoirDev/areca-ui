package dev.voir.areca.viewmodel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

/** Verifies that a JVM without a Main dispatcher module can still construct and run a ViewModel. */
class MissingMainDispatcherTest {
    @Test
    fun `a ViewModel is constructed and reduces actions without a Main dispatcher`() {
        runBlocking {
            val viewModel = IncrementViewModel()

            viewModel.dispatch(Unit)

            withTimeout(5.seconds) { viewModel.state.first { it == UiState.Display(1) } }
        }
    }
}

/** Counts dispatched actions. */
private class IncrementViewModel : BaseViewModel<Unit, Nothing, Int>(initial = 0) {
    override suspend fun reduce(action: Unit) {
        mutateDisplay { it + 1 }
    }
}
