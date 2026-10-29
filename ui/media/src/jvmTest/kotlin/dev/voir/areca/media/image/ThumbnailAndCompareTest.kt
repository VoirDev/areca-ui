package dev.voir.areca.media.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.voir.areca.media.compare.CompareSlider
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/** Thumbnails pick and say what is picked; a divider is a value a screen reader can set. */
@OptIn(ExperimentalTestApi::class)
class ThumbnailAndCompareTest {
    @Test
    fun `pressing a thumbnail selects it`() = runComposeUiTest {
        var picked = -1
        setContent {
            ArecaThemeProvider {
                var selected by remember { mutableIntStateOf(0) }
                ThumbnailStrip(
                    count = 5,
                    selectedIndex = selected,
                    onSelect = { selected = it; picked = it },
                    modifier = Modifier.width(400.dp),
                ) { Box(Modifier) }
            }
        }

        onNodeWithContentDescription("Picture 3 of 5").performClick()
        waitForIdle()

        assertEquals(2, picked)
        onNodeWithContentDescription("Picture 3 of 5").assertIsSelected()
    }

    @Test
    fun `a screen reader moves the divider by setting its value`() = runComposeUiTest {
        setContent {
            ArecaThemeProvider {
                CompareSlider(before = {}, after = {}, modifier = Modifier.width(300.dp))
            }
        }
        val divider = onNodeWithContentDescription("Before and after")

        divider.performSemanticsAction(SemanticsActions.SetProgress) { setProgress -> setProgress(0.2f) }
        waitForIdle()

        val range = divider.fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo]
        assertEquals(0.2f, range.current)
    }
}
