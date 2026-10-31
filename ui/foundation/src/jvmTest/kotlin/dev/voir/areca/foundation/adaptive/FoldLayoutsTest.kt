package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.icon.Bell
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.window.WindowClassBox
import dev.voir.areca.window.WindowFold
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** How the adaptive layouts meet a foldable's fold. */
@OptIn(ExperimentalTestApi::class)
class FoldLayoutsTest {
    /** Draws [content] in a window of [width] × [height] with [fold] across it, laid out at that size. */
    @Composable
    private fun Foldable(width: Int, height: Int, fold: WindowFold, content: @Composable () -> Unit) {
        ArecaThemeProvider(inputMode = InputMode.Touch) {
            ProvideWindowClass(WindowClass(DpSize(width.dp, height.dp), InputMode.Touch, fold)) {
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(width.dp, height.dp)) { content() }
            }
        }
    }

    @Test
    fun `a box beside the fold sees none, a box across it sees it from where it sits`() = runComposeUiTest {
        var beside: WindowClass? = null
        var across: WindowClass? = null
        setContent {
            Foldable(760, 720, WindowFold.vertical(at = 380.dp, height = 720.dp)) {
                Row(Modifier.fillMaxSize()) {
                    WindowClassBox(Modifier.width(300.dp).fillMaxHeight()) { beside = ArecaTheme.window }
                    WindowClassBox(Modifier.width(460.dp).fillMaxHeight()) { across = ArecaTheme.window }
                }
            }
        }
        waitForIdle()

        assertNull(checkNotNull(beside).fold)
        assertEquals(80.dp, assertNotNull(checkNotNull(across).fold).bounds.left)
    }

    private fun listPaneWidth(foldAt: Int): Dp {
        var width: Dp? = null
        runComposeUiTest {
            setContent {
                Foldable(760, 720, WindowFold.vertical(at = foldAt.dp, height = 720.dp)) {
                    ListDetail(
                        showDetail = true,
                        modifier = Modifier.fillMaxSize(),
                        list = {
                            Box(Modifier.fillMaxSize().onGloballyPositioned { width = with(density) { it.size.width.toDp() } })
                        },
                        detail = {},
                    )
                }
            }
            waitForIdle()
        }
        return checkNotNull(width)
    }

    @Test
    fun `two panes meet at a vertical fold that leaves both their minimum widths`() {
        assertEquals(380.dp, listPaneWidth(foldAt = 380))
        // At 200dp the list would be narrower than 240dp: the fold is ignored, the list takes its own width.
        assertEquals(AdaptiveDefaults.listPaneWidth(760.dp), listPaneWidth(foldAt = 200))
    }

    @Test
    fun `the column keeps its clusters either side of a horizontal fold`() = runComposeUiTest {
        var topEnd: Float? = null
        var bottomStart: Float? = null
        setContent {
            Foldable(720, 760, WindowFold.horizontal(at = 380.dp, width = 720.dp)) {
                EdgeColumn(
                    modifier = Modifier.fillMaxHeight(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    top = {
                        Box(Modifier.size(48.dp).onGloballyPositioned { topEnd = it.positionInRoot().y + it.size.height })
                    },
                    bottom = {
                        Box(Modifier.size(48.dp).onGloballyPositioned { bottomStart = it.positionInRoot().y })
                    },
                )
            }
        }
        waitForIdle()

        val fold = with(density) { 380.dp.toPx() }
        assertTrue(checkNotNull(topEnd) <= fold, "top cluster ends at $topEnd, above the fold at $fold")
        assertTrue(checkNotNull(bottomStart) >= fold, "bottom cluster starts at $bottomStart, below the fold at $fold")
        // Without the fold the top cluster would sit at the top and the bottom one at the foot; with it
        // the bottom one still sits at the foot of the window.
        assertTrue(checkNotNull(bottomStart) > with(density) { 600.dp.toPx() })
    }

    @Test
    fun `the column takes the end side when no cutout says otherwise`() = runComposeUiTest {
        var end: Dp? = null
        setContent {
            Foldable(760, 720, WindowFold.vertical(at = 380.dp, height = 720.dp)) {
                AdaptiveNavigation(listOf(NavDestination("Home", "Home", BundledIcons.Bell)), "Home", {}) {
                    end = contentPadding.calculateRightPadding(LayoutDirection.Ltr)
                }
            }
        }
        waitForIdle()

        // 12dp inset, a 56dp touch column and a 12dp gap, on the end side.
        assertEquals(80.dp, end)
    }
}
