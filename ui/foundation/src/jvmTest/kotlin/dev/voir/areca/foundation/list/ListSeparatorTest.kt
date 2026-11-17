package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/** A list's hairline starts at the text of the row under it, past the leading slot. */
@OptIn(ExperimentalTestApi::class)
class ListSeparatorTest {

    private val separator = Color(0xFFFF0000)
    private val style = ListStyle(bg = Color.White, border = Color.Unspecified, separator = separator)
    private val size = ListSize(
        shape = RectangleShape,
        borderWidth = 0.dp,
        separatorThickness = 2.dp,
        separatorInset = 0.dp,
    )

    private fun assertHairlineFromText(list: @androidx.compose.runtime.Composable () -> Unit) = runComposeUiTest {
        setContent { ArecaThemeProvider { Box(Modifier.width(320.dp).testTag("list")) { list() } } }
        waitForIdle()

        val root = onNodeWithTag("list").getBoundsInRoot()
        val textStart = onNodeWithText("Second", useUnmergedTree = true).getBoundsInRoot().left - root.left
        // The boundary lies below the first title; scan down from there for the hairline.
        val boundary = onNodeWithText("First", useUnmergedTree = true).getBoundsInRoot().bottom - root.top
        val pixels = onNodeWithTag("list").captureToImage().toPixelMap()
        val density = pixels.width / 320f
        val textX = (textStart.value * density).roundToInt()
        val y = (boundary.value * density).roundToInt()
        val hairlineY = (y until pixels.height).first { row -> pixels[textX + 4, row] == separator }

        assertEquals(separator, pixels[pixels.width - 2, hairlineY], "the hairline runs to the far edge")
        assertNotEquals(separator, pixels[textX - 4, hairlineY], "the hairline starts at the text, not under the icon")
    }

    @Test
    fun `a composed list starts its hairline at the row's text`() = assertHairlineFromText {
        List(listStyle = style, listSize = size) {
            listOf("First", "Second").forEach { title ->
                item(
                    key = title,
                    title = { Text(title) },
                    leading = { Box(Modifier.size(40.dp)) },
                )
            }
        }
    }

    @Test
    fun `a lazy list starts its hairline at the row's text`() = assertHairlineFromText {
        LazyList(
            entries = listOf("First", "Second").map { title ->
                ListEntry.Item(
                    key = title,
                    title = { Text(title) },
                    leading = { Box(Modifier.size(40.dp)) },
                )
            },
            listStyle = style,
            listSize = size,
        )
    }
}
