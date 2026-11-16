package dev.voir.areca.foundation.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/** Which ink a row hands its chevron, and which ink its trailing slot keeps. */
@OptIn(ExperimentalTestApi::class)
class ListItemChevronColorTest {
    /** The chevron ink and the trailing slot's content ink a row with [style] draws at rest. */
    private fun inks(selected: Boolean = false, style: @Composable () -> ListItemStyle): Pair<Color, Color> {
        var chevron = Color.Unspecified
        var slot = Color.Unspecified
        runComposeUiTest {
            setContent {
                ArecaThemeProvider {
                    ListItem(
                        title = { Text("Groceries") },
                        selected = selected,
                        style = style(),
                        trailing = {
                            chevron = LocalListItemChevronColor.current
                            slot = LocalContentColor.current
                        },
                    )
                }
            }
            waitForIdle()
        }
        return chevron to slot
    }

    @Test
    fun `by default the chevron takes the trailing slot's ink`() {
        val (chevron, slot) = inks { ListItemStyle.plain() }

        assertEquals(slot, chevron)
    }

    @Test
    fun `a chevron ink leaves the slot as it was`() {
        var extra = Color.Unspecified
        val (chevron, slot) = inks {
            ListItemStyle.plain().also { extra = it.extra }.withChevronColor(Color.Red)
        }

        assertEquals(Color.Red, chevron)
        assertEquals(extra, slot)
    }

    @Test
    fun `a flooded row reverses its slot and chevron out of the fill`() {
        val (chevron, slot) = inks(selected = true) { ListItemStyle.flood(content = Color.White) }

        assertEquals(Color.White, chevron)
        assertEquals(Color.White, slot)
    }
}
