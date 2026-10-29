package dev.voir.areca.media.image

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.MediaImageLoader
import dev.voir.areca.media.MediaImageState
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertTrue

/** What a frame draws for each state of its picture, and how it retries. */
@OptIn(ExperimentalTestApi::class)
class MediaFrameBehaviourTest {
    /** Fails the first call and draws a colour on every call after, counting them. */
    private class FlakyLoader : MediaImageLoader {
        var calls = 0

        @Composable
        override fun load(model: Any, maxSize: Int): MediaImageState {
            calls++
            return if (calls == 1) MediaImageState.Failed else MediaImageState.Ready(ColorPainter(Color.Red))
        }
    }

    @Test
    fun `a ready picture is drawn and described`() = runComposeUiTest {
        setContent {
            ArecaThemeProvider {
                CompositionLocalProvider(LocalMediaImageLoader provides FlakyLoader().also { it.calls = 1 }) {
                    MediaFrame(model = "shoe", contentDescription = "Runner 2", modifier = Modifier.width(200.dp), aspectRatio = 1f)
                }
            }
        }

        onNodeWithContentDescription("Runner 2").assertIsDisplayed()
    }

    @Test
    fun `a failed picture says so and loads again on retry`() = runComposeUiTest {
        val loader = FlakyLoader()
        setContent {
            ArecaThemeProvider {
                CompositionLocalProvider(LocalMediaImageLoader provides loader) {
                    MediaFrame(model = "shoe", contentDescription = "Runner 2", modifier = Modifier.width(240.dp), aspectRatio = 1f)
                }
            }
        }
        onNodeWithText("Couldn't load this picture").assertIsDisplayed()

        onNodeWithText("Retry").performClick()
        waitForIdle()

        onNodeWithContentDescription("Runner 2").assertIsDisplayed()
        assertTrue(loader.calls >= 2)
    }

    @Test
    fun `a picture with no model fails without a retry`() = runComposeUiTest {
        setContent {
            ArecaThemeProvider {
                MediaFrame(model = null, contentDescription = null, modifier = Modifier.width(240.dp), aspectRatio = 1f)
            }
        }

        onNodeWithText("Couldn't load this picture").assertIsDisplayed()
        onAllNodes(hasText("Retry")).assertCountEquals(0)
    }
}
