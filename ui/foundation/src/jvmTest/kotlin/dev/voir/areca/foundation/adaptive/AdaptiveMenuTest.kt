package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
    /** A window of [size] under [input] with a trigger for [state] and [menu] anchored to it. */
    @Composable
    private fun MenuWindow(
        state: DropdownPopupState,
        size: DpSize,
        input: InputMode,
        motion: ArecaMotion = ArecaMotion(),
        menu: @Composable () -> Unit,
    ) {
        ArecaThemeProvider(inputMode = input, motion = motion) {
            ProvideWindowClass(WindowClass(size, input)) {
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(size)) {
                    Box(Modifier.align(Alignment.TopEnd).requiredSize(48.dp).anchored(state.anchorState))
                    menu()
                }
            }
        }
    }

    /** A phone-sized touch window with a trigger for [state] and its grouped menu. */
    @Composable
    private fun PhoneMenu(
        state: DropdownPopupState,
        groups: List<List<AdaptiveMenuItem>>,
        presentation: AdaptiveMenuPresentation = AdaptiveMenuPresentation.Dropdown,
        motion: ArecaMotion = ArecaMotion(),
    ) {
        MenuWindow(state, Phone, InputMode.Touch, motion) {
            AdaptiveMenu(state = state, groups = groups, presentation = presentation)
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
    fun `the sheet form is a sheet on a phone`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            PhoneMenu(state, listOf(listOf(item("Edit"))), presentation = AdaptiveMenuPresentation.Sheet)
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(0, popups())
        assertEquals(1, dialogs())
    }

    @Test
    fun `the sheet form stays a sheet on a desktop under a pointer`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            MenuWindow(state, Desktop, InputMode.Pointer) {
                AdaptiveMenu(state = state, groups = listOf(listOf(item("Edit"))), presentation = AdaptiveMenuPresentation.Sheet)
            }
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(0, popups())
        assertEquals(1, dialogs())
    }

    @Test
    fun `the list of actions takes the form the app provides, on any window`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            MenuWindow(state, Desktop, InputMode.Pointer) {
                CompositionLocalProvider(LocalAdaptiveMenuPresentation provides AdaptiveMenuPresentation.Sheet) {
                    AdaptiveMenu(state = state, actions = listOf(item("Edit")))
                }
            }
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(0, popups())
        assertEquals(1, dialogs())
    }

    @Test
    fun `the list of actions is a card on a phone by default`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            MenuWindow(state, Phone, InputMode.Touch) {
                AdaptiveMenu(state = state, actions = listOf(item("Edit")))
            }
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(1, popups())
        assertEquals(0, dialogs())
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

    private companion object {
        val Phone = DpSize(393.dp, 852.dp)
        val Desktop = DpSize(1440.dp, 900.dp)
    }
}
