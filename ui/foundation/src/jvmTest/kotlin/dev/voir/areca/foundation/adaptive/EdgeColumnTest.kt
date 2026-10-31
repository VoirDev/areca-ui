package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.icon.Bell
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** What the edge column holds, and how screens hand it their toolbars. */
@OptIn(ExperimentalTestApi::class)
class EdgeColumnTest {
    private val icon: ImageVector = BundledIcons.Bell
    private val destinations = listOf("Home", "Activity", "Wallets").map { NavDestination(it, it, icon) }

    /**
     * Draws [content] in a tablet-sized window under touch, laid out at that size: on its side by
     * default, upright when [size] is taller than wide.
     */
    @Composable
    private fun Tablet(size: DpSize = DpSize(1180.dp, 820.dp), content: @Composable () -> Unit) {
        ArecaThemeProvider(inputMode = InputMode.Touch) {
            ProvideWindowClass(WindowClass(size, InputMode.Touch)) {
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(size)) { content() }
            }
        }
    }

    @Test
    fun `the screen keeps its text clear of the column on the column's side`() = runComposeUiTest {
        var start: Dp? = null
        var end: Dp? = null
        var clearance: Dp? = null
        setContent {
            Tablet {
                clearance = EdgeColumnSize.Default.clearance
                AdaptiveNavigation(destinations, "Home", {}) {
                    start = contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)
                    end = contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)
                }
            }
        }
        waitForIdle()

        assertEquals(0.dp, start)
        assertEquals(clearance, end)
        // 12dp inset, a 56dp touch column and a 12dp gap.
        assertEquals(80.dp, end)
    }

    @Test
    fun `a pushed screen's back takes the toggle's place, and leaving gives it back`() = runComposeUiTest {
        var pushed by mutableStateOf(false)
        setContent {
            Tablet {
                AdaptiveNavigation(
                    destinations = destinations,
                    selectedValue = "Home",
                    onValueSelected = {},
                    onSidebarVisibleChange = {},
                ) {
                    if (pushed) {
                        AdaptiveToolbar(
                            title = "Lidl",
                            onBack = { pushed = false },
                            actions = listOf(AdaptiveToolbarAction(icon, "Edit", onClick = {})),
                        )
                    } else {
                        AdaptiveToolbar(title = "Home")
                    }
                }
            }
        }

        onNodeWithContentDescription("Show navigation").assertExists()
        onAllNodesWithContentDescription("Back").assertCountEquals(0)

        pushed = true
        waitForIdle()
        onNodeWithContentDescription("Back").assertExists()
        onNodeWithText("Edit").assertExists()
        onAllNodesWithContentDescription("Show navigation").assertCountEquals(0)

        pushed = false
        waitForIdle()
        onNodeWithContentDescription("Show navigation").assertExists()
        onAllNodesWithText("Edit").assertCountEquals(0)
    }

    @Test
    fun `on a tablet on its side the actions are labelled buttons beside the title`() = runComposeUiTest {
        setContent {
            Tablet {
                AdaptiveNavigation(destinations, "Home", {}, onSidebarVisibleChange = {}) {
                    AdaptiveToolbar(
                        title = "Home",
                        actions = listOf("Edit", "Share", "Archive", "Delete").map { AdaptiveToolbarAction(icon, it, onClick = {}) },
                    )
                }
            }
        }

        // Three places beside the title: two actions, and More for the other two.
        onNodeWithText("Edit").assertExists()
        onNodeWithText("Share").assertExists()
        onNodeWithContentDescription("More").assertExists()
        onAllNodesWithText("Archive").assertCountEquals(0)
        onAllNodesWithText("Delete").assertCountEquals(0)
        // None of them reaches the column.
        onAllNodesWithContentDescription("Edit").assertCountEquals(0)
    }

    @Test
    fun `on a tablet held upright the actions go to the foot of the column, between the tabs and search`() =
        runComposeUiTest {
            setContent {
                Tablet(size = DpSize(820.dp, 1180.dp)) {
                    AdaptiveNavigation(destinations, "Home", {}, onSearch = {}) {
                        AdaptiveToolbar(
                            title = "Lidl",
                            onBack = {},
                            actions = listOf("Edit", "Share", "Archive", "Delete").map { AdaptiveToolbarAction(icon, it, onClick = {}) },
                        )
                    }
                }
            }

            val back = onNodeWithContentDescription("Back").getBoundsInRoot()
            val lastTab = onNodeWithContentDescription("Wallets").getBoundsInRoot()
            val edit = onNodeWithContentDescription("Edit").getBoundsInRoot()
            val more = onNodeWithContentDescription("More").getBoundsInRoot()
            val search = onNodeWithContentDescription("Search").getBoundsInRoot()

            // Back stays at the head of the column; the actions sit under the tabs, over search.
            assertTrue(back.bottom < lastTab.top)
            assertTrue(lastTab.bottom <= edit.top)
            assertTrue(edit.bottom <= more.top)
            assertTrue(more.bottom <= search.top)
            // Three round buttons: two actions and More; nothing beside the title.
            onNodeWithContentDescription("Share").assertExists()
            onAllNodesWithContentDescription("Archive").assertCountEquals(0)
            onAllNodesWithText("Edit").assertCountEquals(0)
        }

    @Test
    fun `an unfolded foldable on its side is held from below too`() = runComposeUiTest {
        setContent {
            Tablet(size = DpSize(841.dp, 701.dp)) {
                AdaptiveNavigation(destinations, "Home", {}) {
                    AdaptiveToolbar(title = "Home", actions = listOf(AdaptiveToolbarAction(icon, "Edit", onClick = {})))
                }
            }
        }

        onNodeWithContentDescription("Edit").assertExists()
        onAllNodesWithText("Edit").assertCountEquals(0)
    }

    @Test
    fun `outside the column the toolbar is a bar over the screen`() = runComposeUiTest {
        setContent {
            Tablet {
                AdaptiveNavigation(destinations, "Home", {}, style = AdaptiveNavigationStyle.Rail) {
                    AdaptiveToolbar(title = "Lidl", onBack = {}, actions = listOf(AdaptiveToolbarAction(icon, "Edit", onClick = {})))
                }
            }
        }

        onNodeWithText("Lidl").assertExists()
        onNodeWithContentDescription("Back").assertExists()
        onNodeWithContentDescription("Edit").assertExists()
    }

    @Test
    fun `a sixth destination goes to More`() = runComposeUiTest {
        val six = listOf("Home", "Activity", "Wallets", "Budgets", "Goals", "Settings").map { NavDestination(it, it, icon) }
        setContent {
            Tablet {
                VerticalTabBar(destinations = six, selectedValue = "Home", onValueSelected = {})
            }
        }

        onNodeWithContentDescription("Budgets").assertExists()
        onNodeWithContentDescription("More").assertExists()
        onAllNodesWithContentDescription("Goals").assertCountEquals(0)
        onAllNodesWithContentDescription("Settings").assertCountEquals(0)
    }

    @Test
    fun `the open sidebar replaces the tabs and closes from its scrim`() = runComposeUiTest {
        var visible by mutableStateOf(true)
        setContent {
            Tablet {
                AdaptiveNavigation(
                    destinations = destinations,
                    selectedValue = "Home",
                    onValueSelected = {},
                    sidebarVisible = visible,
                    onSidebarVisibleChange = { visible = it },
                ) {}
            }
        }

        // The sidebar's rows name the destinations; the capsule, which would announce them too, is gone.
        onNodeWithText("Activity").assertExists()
        onAllNodesWithContentDescription("Activity").assertCountEquals(0)

        onNode(
            SemanticsMatcher("closes the sidebar") {
                it.config.getOrElseNullable(SemanticsActions.OnClick) { null }?.label == "Close navigation"
            },
        ).performSemanticsAction(SemanticsActions.OnClick)
        waitForIdle()

        assertFalse(visible)
        onNodeWithContentDescription("Activity").assertExists()
    }
}
