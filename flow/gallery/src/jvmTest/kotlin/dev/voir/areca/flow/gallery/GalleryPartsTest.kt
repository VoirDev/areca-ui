package dev.voir.areca.flow.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.gallery.grid.GalleryGroupHeader
import dev.voir.areca.flow.gallery.grid.GalleryTile
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertTrue

/** The gallery parts say what they show and do what they are pressed for. */
@OptIn(ExperimentalTestApi::class)
class GalleryPartsTest {
    @Test
    fun `a tile shows whose photo it is and opens when pressed`() = runComposeUiTest {
        var opened = false
        setContent {
            ArecaThemeProvider {
                GalleryTile(model = null, contentDescription = null, onClick = { opened = true }, modifier = Modifier.width(120.dp), label = "Ines")
            }
        }

        onNodeWithText("Ines").assertIsDisplayed().performClick()

        assertTrue(opened)
    }

    @Test
    fun `a group header is read as a heading`() = runComposeUiTest {
        setContent { ArecaThemeProvider { GalleryGroupHeader(title = "Readers' photos", count = "3") } }

        val header = onNodeWithText("Readers' photos").fetchSemanticsNode()
        assertTrue(SemanticsProperties.Heading in header.config)
    }

    @Test
    fun `a group header keeps the gutter unless a grid keeps it`() = runComposeUiTest {
        setContent {
            ArecaThemeProvider {
                Column {
                    GalleryGroupHeader(title = "From the brand")
                    GalleryGroupHeader(title = "Readers' photos", horizontalPadding = 0.dp)
                }
            }
        }

        onNodeWithText("From the brand").assertLeftPositionInRootIsEqualTo(16.dp)
        onNodeWithText("Readers' photos").assertLeftPositionInRootIsEqualTo(0.dp)
    }

    @Test
    fun `a caption puts its kind in capitals above its title`() = runComposeUiTest {
        setContent { ArecaThemeProvider { GalleryCaption(title = "Laces, close up", kicker = "From the brand") } }

        onNodeWithText("FROM THE BRAND", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText("Laces, close up", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `the primary action does what it says`() = runComposeUiTest {
        var added = false
        setContent { ArecaThemeProvider { GalleryPrimaryAction(text = "Add to bag", onClick = { added = true }) } }

        onNodeWithText("Add to bag", useUnmergedTree = true).performClick()

        assertTrue(added)
    }
}
