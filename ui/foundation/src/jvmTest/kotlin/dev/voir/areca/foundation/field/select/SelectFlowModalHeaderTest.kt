package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertFalse

/** What [SelectFlowModal]'s `headerBottom` adds under the stock title, and that the stock close stays. */
@OptIn(ExperimentalTestApi::class)
class SelectFlowModalHeaderTest {
    private val options = listOf("Monzo", "Revolut", "Starling").map { name ->
        SelectOption(key = name, value = name, label = name)
    }
    private val sections = listOf(SelectOptionSection(key = "all", title = null, options = options))

    /** Opens [sheet] on [controller], checks the line under the title, then closes from the cross. */
    private fun assertHeaderBottom(controller: FlowModalController, sheet: @Composable () -> Unit) =
        runComposeUiTest {
            setContent { ArecaThemeProvider { sheet() } }
            waitForIdle()

            onNodeWithText("Banks").assertIsDisplayed()
            onNodeWithText("United Kingdom").assertIsDisplayed()

            onNodeWithContentDescription("Close").performClick()
            mainClock.advanceTimeBy(2_000)
            waitForIdle()

            assertFalse(controller.isVisible)
        }

    @Test
    fun `the flat sheet draws its header bottom and keeps the stock close`() {
        val controller = FlowModalController(initialState = FlowModalState.Expanded)
        assertHeaderBottom(controller) {
            SelectFlowModal(
                controller = controller,
                values = emptySet(),
                options = options,
                title = "Banks",
                headerBottom = { Text("United Kingdom") },
            )
        }
    }

    @Test
    fun `the sectioned sheet draws its header bottom and keeps the stock close`() {
        val controller = FlowModalController(initialState = FlowModalState.Expanded)
        assertHeaderBottom(controller) {
            SelectFlowModal(
                controller = controller,
                values = emptySet(),
                sections = sections,
                title = "Banks",
                headerBottom = { Text("United Kingdom") },
            )
        }
    }
}
