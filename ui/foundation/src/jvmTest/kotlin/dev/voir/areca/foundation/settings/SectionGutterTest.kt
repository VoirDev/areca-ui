package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertEquals

/** A section keeps the screen gutter of the place it is drawn, wherever its sizes were built. */
@OptIn(ExperimentalTestApi::class)
class SectionGutterTest {
    /**
     * Builds the sizes in a window of [windowWidth], as an app's theme does at its root, and draws a
     * section and a gutter-inset reference in a pane of [paneWidth] inside it. [outerPadding] is the
     * section's padding when the app sets one.
     */
    private fun content(windowWidth: Int, paneWidth: Int, outerPadding: PaddingValues? = null): @Composable () -> Unit = {
        ArecaThemeProvider {
            ProvideWindowClass(WindowClass(DpSize(windowWidth.dp, 800.dp), InputMode.Touch)) {
                val section = if (outerPadding != null) {
                    SettingsDefaults.sectionSize(outerPadding = outerPadding, sectionGap = 20.dp)
                } else {
                    SettingsDefaults.sectionSize(sectionGap = 20.dp)
                }
                val rootSizes = SettingsDefaults.sizes(section = section)
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(paneWidth.dp, 800.dp)) {
                    ProvideWindowClass(WindowClass(DpSize(paneWidth.dp, 800.dp), InputMode.Touch)) {
                        CompositionLocalProvider(LocalSettingsSizes provides rootSizes) {
                            Column {
                                Box(Modifier.fillMaxWidth().horizontalContentInsets()) {
                                    Box(Modifier.fillMaxWidth().height(1.dp).testTag(Reference))
                                }
                                Settings {
                                    Section {
                                        SettingsRow(title = { Text("Row") }, modifier = Modifier.testTag(Row))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `a section in a pane takes the pane's gutter, not the window's`() = runComposeUiTest {
        // An expanded window (a 32dp gutter) around a medium pane (a 24dp gutter).
        setContent(content(windowWidth = 1100, paneWidth = 640))
        val reference = onNodeWithTag(Reference).getBoundsInRoot()
        val row = onNodeWithTag(Row).getBoundsInRoot()
        assertEquals(24.dp, reference.left)
        assertEquals(reference.left, row.left)
        assertEquals(reference.right, row.right)
    }

    @Test
    fun `a section in the window takes the window's gutter`() = runComposeUiTest {
        setContent(content(windowWidth = 1100, paneWidth = 1100))
        assertEquals(32.dp, onNodeWithTag(Row).getBoundsInRoot().left)
    }

    @Test
    fun `an outer padding the app sets is kept`() = runComposeUiTest {
        setContent(content(windowWidth = 1100, paneWidth = 640, outerPadding = PaddingValues(horizontal = 8.dp)))
        assertEquals(8.dp, onNodeWithTag(Row).getBoundsInRoot().left)
    }

    private companion object {
        const val Reference = "reference"
        const val Row = "row"
    }
}
