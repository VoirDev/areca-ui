package dev.voir.areca.media.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertTrue

/** The viewer pages, reports where it is, and closes on a swipe down. */
@OptIn(ExperimentalTestApi::class)
class MediaViewerBehaviourTest {
    @Test
    fun `a swipe turns to the next picture and a swipe down closes`() = runComposeUiTest {
        var dismissed = false
        setContent {
            ArecaThemeProvider {
                MediaViewer(
                    itemCount = 3,
                    onDismiss = { dismissed = true },
                    topBar = { state -> Text("${state.currentIndex + 1} of ${state.itemCount}") },
                ) { index -> Box(Modifier.fillMaxSize().testTag("page-$index")) }
            }
        }
        waitForIdle()
        onNodeWithText("1 of 3").assertIsDisplayed()

        onNodeWithTag("page-0").performTouchInput { swipeLeft() }
        waitForIdle()
        onNodeWithText("2 of 3").assertIsDisplayed()

        onNodeWithTag("page-1").performTouchInput { swipeDown(startY = centerY - 200f, endY = centerY + 300f) }
        waitForIdle()
        assertTrue(dismissed)
    }
}
