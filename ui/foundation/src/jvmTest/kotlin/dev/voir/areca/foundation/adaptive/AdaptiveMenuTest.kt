package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.overlay.DropdownPopupState
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** The grouped [AdaptiveMenu]: its form, the order of its rows, and what choosing one does. */
@OptIn(ExperimentalTestApi::class)
class AdaptiveMenuTest {
    /** A phone-sized touch window with a trigger for [state] and its grouped menu. */
    @Composable
    private fun PhoneMenu(
        state: DropdownPopupState,
        groups: List<List<AdaptiveMenuItem>>,
        presentation: AdaptiveMenuPresentation = AdaptiveMenuPresentation.Dropdown,
        motion: ArecaMotion = ArecaMotion(),
    ) {
        ArecaThemeProvider(inputMode = InputMode.Touch, motion = motion) {
            ProvideWindowClass(WindowClass(DpSize(393.dp, 852.dp), InputMode.Touch)) {
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(393.dp, 852.dp)) {
                    Box(Modifier.align(Alignment.TopEnd).requiredSize(48.dp).anchored(state.anchorState))
                    AdaptiveMenu(state = state, groups = groups, presentation = presentation)
                }
            }
        }
    }

    private fun ComposeUiTest.popups(): Int = onAllNodes(isPopup()).fetchSemanticsNodes().size

    private fun ComposeUiTest.dialogs(): Int = onAllNodes(isDialog()).fetchSemanticsNodes().size

    private fun ComposeUiTest.top(label: String): Float =
        onNodeWithText(label).fetchSemanticsNode().boundsInRoot.top

    private fun item(label: String, destructive: Boolean = false, onClick: () -> Unit = {}) =
        AdaptiveMenuItem(label = label, destructive = destructive, onClick = onClick)

    @Test
    fun `the dropdown form is a card under touch on a phone`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit"))))
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(1, popups())
        assertEquals(0, dialogs())
        onNodeWithText("Edit").assertExists()
    }

    @Test
    fun `the adaptive form is still a sheet under touch on a phone`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit"))), presentation = AdaptiveMenuPresentation.Adaptive)
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(0, popups())
        assertEquals(1, dialogs())
    }

    @Test
    fun `rows come out group by group with destructive actions last`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(
                state,
                listOf(
                    listOf(item("Delete", destructive = true), item("Edit")),
                    listOf(item("End")),
                ),
            )
        }
        runOnIdle { state.show() }
        waitForIdle()
        val edit = top("Edit")
        val end = top("End")
        val delete = top("Delete")
        assertTrue(edit < end, "Edit above End")
        assertTrue(end < delete, "End above Delete")
    }

    @Test
    fun `a row is one control tall under touch`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit"))))
        }
        runOnIdle { state.show() }
        waitForIdle()
        onNodeWithText("Edit").assertHeightIsEqualTo(48.dp)
    }

    @Test
    fun `choosing an action closes the menu and runs the action`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        var chosen = false
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit") { chosen = true })))
        }
        runOnIdle { state.show() }
        waitForIdle()
        onNodeWithText("Edit").performClick()
        waitForIdle()
        assertTrue(chosen)
        assertFalse(state.isOpen)
        assertEquals(0, popups())
    }

    @Test
    fun `with reduced motion the card goes on the frame after a choice`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit"))), motion = ArecaMotion(reduceMotion = true))
        }
        runOnIdle { state.show() }
        waitForIdle()
        mainClock.autoAdvance = false
        onNodeWithText("Edit").performClick()
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(0, popups())
    }
}
