package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/** A dropdown stays up while it animates out, and goes at once when motion is reduced. */
@OptIn(ExperimentalTestApi::class)
class DropdownPopupMotionTest {
    /** A trigger with [state]'s dropdown under it, in a theme with [motion]. */
    @Composable
    private fun Sample(state: DropdownPopupState, motion: ArecaMotion = ArecaMotion()) {
        ArecaThemeProvider(motion = motion) {
            Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(600.dp, 600.dp)) {
                Box(Modifier.requiredSize(40.dp).anchored(state.anchorState))
                DropdownPopup(state = state, onDismissRequest = state::dismiss) { Text("Edit") }
            }
        }
    }

    private fun ComposeUiTest.popups(): Int = onAllNodes(isPopup()).fetchSemanticsNodes().size

    @Test
    fun `a closed dropdown composes nothing`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            Sample(state)
        }
        waitForIdle()
        assertEquals(0, popups())
    }

    @Test
    fun `a dropdown stays up while it leaves and goes once the exit has run`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            Sample(state)
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(1, popups())

        mainClock.autoAdvance = false
        runOnIdle { state.dismiss() }
        mainClock.advanceTimeByFrame()
        mainClock.advanceTimeBy(50)
        assertEquals(1, popups())

        mainClock.advanceTimeBy(500)
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(0, popups())
    }

    @Test
    fun `with reduced motion a dropdown goes on the next frame`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            Sample(state, motion = ArecaMotion(reduceMotion = true))
        }
        runOnIdle { state.show() }
        waitForIdle()
        assertEquals(1, popups())

        mainClock.autoAdvance = false
        runOnIdle { state.dismiss() }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(0, popups())
    }

    @Test
    fun `a dropdown opened again while it leaves stays up`() = runComposeUiTest {
        lateinit var state: DropdownPopupState
        setContent {
            state = rememberDropdownPopup()
            Sample(state)
        }
        runOnIdle { state.show() }
        waitForIdle()
        mainClock.autoAdvance = false
        runOnIdle { state.dismiss() }
        mainClock.advanceTimeBy(50)
        runOnIdle { state.show() }
        mainClock.advanceTimeBy(1000)
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(1, popups())
    }
}
