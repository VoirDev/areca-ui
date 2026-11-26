package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** A [FlowModal] stops at the readable width, centred, unless its size says to span the window. */
@OptIn(ExperimentalTestApi::class)
class FlowModalWidthTest {

    /** The window the sheet opens in: the scrim covers it. */
    private fun ComposeUiTest.window(): DpRect = onNodeWithContentDescription("Dismiss").getBoundsInRoot()

    private fun ComposeUiTest.sheet(): DpRect = onNodeWithTag(SheetTag).getBoundsInRoot()

    private fun ComposeUiTest.openSheet(size: (@Composable () -> FlowModalSize)? = null) {
        setContent {
            ArecaThemeProvider {
                val controller = rememberFlowModal()
                LaunchedEffect(Unit) { controller.show() }
                FlowModal(
                    controller = controller,
                    modifier = Modifier.testTag(SheetTag),
                    size = size?.invoke() ?: FlowModalSize.Default,
                ) {
                    Text("Body")
                }
            }
        }
        waitForIdle()
    }

    private fun assertClose(expected: Dp, actual: Dp, message: String) =
        assertTrue(abs(expected.value - actual.value) < 1f, "$message: expected $expected, was $actual")

    @Test
    fun `a sheet on a wide window stops at the readable width, centred on the bottom edge`() = runComposeUiTest {
        openSheet()
        val window = window()
        val sheet = sheet()

        assertTrue(window.right - window.left > AdaptiveDefaults.ReadableWidth, "the test window is wider than the cap")
        assertClose(AdaptiveDefaults.ReadableWidth, sheet.right - sheet.left, "sheet width")
        assertClose(sheet.left - window.left, window.right - sheet.right, "space either side")
        assertClose(window.bottom, sheet.bottom, "sheet bottom")
    }

    @Test
    fun `an unspecified maximum width spans the window`() = runComposeUiTest {
        openSheet { FlowModalSize.default(maxWidth = Dp.Unspecified) }
        val window = window()
        val sheet = sheet()

        assertEquals(window.left, sheet.left)
        assertEquals(window.right, sheet.right)
    }

    @Test
    fun `a narrower maximum width is kept`() = runComposeUiTest {
        openSheet { FlowModalSize.default(maxWidth = 480.dp) }
        val sheet = sheet()

        assertClose(480.dp, sheet.right - sheet.left, "sheet width")
    }

    private companion object {
        const val SheetTag = "sheet"
    }
}
