package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.rightClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text
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

/** Tooltips, context menus and the resizable divider: what a pointer and a long press do. */
@OptIn(ExperimentalTestApi::class)
class PointerAffordancesTest {
    /** Draws [content] in a desktop-sized window under [input]. */
    @Composable
    private fun Window(input: InputMode, width: Int = 1000, height: Int = 700, content: @Composable () -> Unit) {
        ArecaThemeProvider(inputMode = input) {
            ProvideWindowClass(WindowClass(DpSize(width.dp, height.dp), input)) {
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(width.dp, height.dp)) {
                    content()
                }
            }
        }
    }

    /** How many popups are up; a tooltip is one, and has no semantics of its own to find it by. */
    private fun androidx.compose.ui.test.ComposeUiTest.popups(): Int = onAllNodes(isPopup()).fetchSemanticsNodes().size

    @Test
    fun `an icon button names itself in a tooltip once the pointer rests on it`() = runComposeUiTest {
        mainClock.autoAdvance = false
        setContent {
            Window(InputMode.Pointer) {
                IconButton(imageVector = BundledIcons.Bell, contentDescription = "Notifications", onClick = {})
            }
        }
        onNodeWithContentDescription("Notifications").performMouseInput { moveTo(center) }
        mainClock.advanceTimeBy(200)
        assertEquals(0, popups())
        mainClock.advanceTimeBy(600)
        waitForIdle()
        assertEquals(1, popups())
        onNodeWithContentDescription("Notifications").performMouseInput { moveTo(center.copy(x = -200f)) }
        mainClock.advanceTimeBy(100)
        waitForIdle()
        assertEquals(0, popups())
    }

    @Test
    fun `a touched icon button shows no tooltip`() = runComposeUiTest {
        mainClock.autoAdvance = false
        setContent {
            Window(InputMode.Touch) {
                IconButton(imageVector = BundledIcons.Bell, contentDescription = "Notifications", onClick = {})
            }
        }
        onNodeWithContentDescription("Notifications").performMouseInput { moveTo(center) }
        mainClock.advanceTimeBy(1000)
        waitForIdle()
        assertEquals(0, popups())
    }

    @Test
    fun `a right-click opens the menu and does not click`() = runComposeUiTest {
        var clicks = 0
        var open = false
        setContent {
            Window(InputMode.Pointer) {
                val menu = rememberDropdownPopup()
                open = menu.isOpen
                Box(
                    Modifier.testTag("row").fillMaxWidth().height(48.dp)
                        .contextMenu(menu)
                        .clickable { clicks++ },
                )
            }
        }
        onNodeWithTag("row").performMouseInput { rightClick(center) }
        waitForIdle()
        assertTrue(open)
        assertEquals(0, clicks)
    }

    @Test
    fun `a long press opens the menu and does not click`() = runComposeUiTest {
        var clicks = 0
        var open = false
        setContent {
            Window(InputMode.Touch, width = 400, height = 800) {
                val menu = rememberDropdownPopup()
                open = menu.isOpen
                Box(
                    Modifier.testTag("row").fillMaxWidth().height(56.dp)
                        .contextMenu(menu)
                        .clickable { clicks++ },
                )
            }
        }
        onNodeWithTag("row").performTouchInput { click(center) }
        waitForIdle()
        assertFalse(open)
        assertEquals(1, clicks)

        onNodeWithTag("row").performTouchInput { longClick(center) }
        waitForIdle()
        assertTrue(open)
        assertEquals(1, clicks)
    }

    @Test
    fun `assistive technology opens the menu with a long-click action`() = runComposeUiTest {
        var open = false
        setContent {
            Window(InputMode.Touch, width = 400, height = 800) {
                val menu = rememberDropdownPopup()
                open = menu.isOpen
                Box(Modifier.testTag("row").fillMaxWidth().height(56.dp).contextMenu(menu).clickable {})
            }
        }
        onNodeWithTag("row").performSemanticsAction(SemanticsActions.OnLongClick)
        waitForIdle()
        assertTrue(open)
    }

    @Test
    fun `a pointer drags the divider between list and detail`() = runComposeUiTest {
        var listWidth = 0
        setContent {
            Window(InputMode.Pointer) {
                ListDetail(
                    showDetail = false,
                    modifier = Modifier.fillMaxSize().testTag("panes"),
                    list = { Box(Modifier.fillMaxSize().onGloballyPositioned { listWidth = it.size.width }) },
                    detail = {},
                )
            }
        }
        waitForIdle()
        // 1000dp container: a 320dp list, the handle centred on its edge.
        assertEquals(320, listWidth)
        onNodeWithTag("panes").performMouseInput {
            moveTo(centerLeft.copy(x = 320f))
            press()
            moveBy(Offset(80f, 0f))
            moveBy(Offset(20f, 0f))
            release()
        }
        waitForIdle()
        // The drag moves the edge by what it travelled past the slop.
        assertTrue(listWidth in 380..420, "list is $listWidth")

        // Never past the detail's minimum.
        val edge = listWidth.toFloat()
        onNodeWithTag("panes").performMouseInput {
            moveTo(centerLeft.copy(x = edge))
            press()
            moveBy(Offset(600f, 0f))
            release()
        }
        waitForIdle()
        assertEquals(1000 - 320, listWidth)
    }

    @Test
    fun `a touched divider stays put`() = runComposeUiTest {
        var listWidth = 0
        setContent {
            Window(InputMode.Touch) {
                ListDetail(
                    showDetail = false,
                    modifier = Modifier.fillMaxSize().testTag("panes"),
                    list = { Box(Modifier.fillMaxSize().onGloballyPositioned { listWidth = it.size.width }) },
                    detail = { Text("Detail") },
                )
            }
        }
        onNodeWithTag("panes").performMouseInput {
            moveTo(centerLeft.copy(x = 320f))
            press()
            moveBy(Offset(100f, 0f))
            release()
        }
        waitForIdle()
        assertEquals(320, listWidth)
    }
}
