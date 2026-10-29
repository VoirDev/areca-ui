package dev.voir.areca.media.zoom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.pinch
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Which gesture belongs to whom on zoomable content, driven with real pointer input. */
@OptIn(ExperimentalTestApi::class)
class ZoomGestureTest {
    @Test
    fun `a double tap zooms a picture and another returns it`() = runComposeUiTest {
        lateinit var state: ZoomState
        setContent {
            ArecaThemeProvider {
                state = rememberZoomState()
                Box(Modifier.size(200.dp).testTag("picture").zoomable(state))
            }
        }

        onNodeWithTag("picture").performTouchInput { doubleClick(center) }
        waitForIdle()
        assertTrue(state.isZoomed)

        onNodeWithTag("picture").performTouchInput { doubleClick(center) }
        waitForIdle()
        assertFalse(state.isZoomed)
    }

    @Test
    fun `a pinch on a sticky picture zooms it and it stays`() = runComposeUiTest {
        lateinit var state: ZoomState
        setContent {
            ArecaThemeProvider {
                state = rememberZoomState()
                Box(Modifier.size(200.dp).testTag("picture").zoomable(state))
            }
        }

        onNodeWithTag("picture").performTouchInput {
            pinch(center - Offset(10f, 0f), center - Offset(80f, 0f), center + Offset(10f, 0f), center + Offset(80f, 0f))
        }
        waitForIdle()

        assertTrue(state.scale > 2f)
    }

    @Test
    fun `a peek zooms while pinched and springs back when the fingers lift`() = runComposeUiTest {
        lateinit var state: ZoomState
        var largest = 1f
        setContent {
            ArecaThemeProvider {
                state = rememberZoomState()
                LaunchedEffect(state) { snapshotFlow { state.scale }.collect { largest = maxOf(largest, it) } }
                Box(Modifier.size(200.dp).testTag("picture").zoomable(state, behavior = ZoomBehavior.Peek))
            }
        }

        onNodeWithTag("picture").performTouchInput {
            pinch(center - Offset(10f, 0f), center - Offset(80f, 0f), center + Offset(10f, 0f), center + Offset(80f, 0f))
        }
        waitForIdle()

        assertTrue(largest > 2f)
        assertEquals(1f, state.scale)
    }

    @Test
    fun `one finger at the fitted size turns the page`() = runComposeUiTest {
        lateinit var pager: PagerState
        setContent {
            ArecaThemeProvider {
                pager = rememberPagerState { 2 }
                HorizontalPager(pager, Modifier.size(200.dp).testTag("pager")) {
                    Box(Modifier.size(200.dp).zoomable(rememberZoomState()))
                }
            }
        }

        onNodeWithTag("pager").performTouchInput { swipeLeft() }
        waitForIdle()

        assertEquals(1, pager.currentPage)
    }

    @Test
    fun `once zoomed one finger pans the picture and the page stays`() = runComposeUiTest {
        lateinit var pager: PagerState
        lateinit var state: ZoomState
        setContent {
            ArecaThemeProvider {
                pager = rememberPagerState { 2 }
                state = rememberZoomState()
                HorizontalPager(pager, Modifier.size(200.dp).testTag("pager")) { page ->
                    Box(Modifier.size(200.dp).zoomable(if (page == 0) state else rememberZoomState()))
                }
            }
        }
        runOnIdle { state.zoom(3f, Offset(100f, 100f)) }
        val before = state.offset

        onNodeWithTag("pager").performTouchInput { swipeLeft(startX = centerX + 20f, endX = centerX - 20f) }
        waitForIdle()

        assertEquals(0, pager.currentPage)
        assertTrue(state.offset.x < before.x)
    }

    @Test
    fun `a mouse over a hover picture magnifies it and leaving lets go`() = runComposeUiTest {
        lateinit var state: ZoomState
        setContent {
            ArecaThemeProvider {
                state = rememberZoomState()
                Box(Modifier.size(200.dp).testTag("picture").zoomable(state, behavior = ZoomBehavior.Hover))
            }
        }

        onNodeWithTag("picture").performMouseInput { moveTo(center) }
        waitForIdle()
        assertTrue(state.isZoomed)

        onNodeWithTag("picture").performMouseInput { exit() }
        waitForIdle()
        assertFalse(state.isZoomed)
    }
}
