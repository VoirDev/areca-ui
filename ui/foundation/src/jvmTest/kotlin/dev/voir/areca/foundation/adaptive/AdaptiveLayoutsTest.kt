package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.icon.Bell
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.window.WindowClassBox
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Which form each adaptive layout takes for the window or container it is given. */
@OptIn(ExperimentalTestApi::class)
class AdaptiveLayoutsTest {
    private val icon: ImageVector = BundledIcons.Bell
    private val destinations = listOf("Home", "Activity", "Wallets").map { NavDestination(it, it, icon) }

    /** Draws [content] in a window of [width] × [height] under [input], laid out at that size. */
    @Composable
    private fun Window(width: Int, height: Int, input: InputMode = InputMode.Touch, content: @Composable () -> Unit) {
        ArecaThemeProvider(inputMode = input) {
            ProvideWindowClass(WindowClass(DpSize(width.dp, height.dp), input)) {
                // Laid out at its own size even where the test window is smaller, pinned to the top start.
                Box(Modifier.wrapContentSize(Alignment.TopStart, unbounded = true).requiredSize(width.dp, height.dp)) { content() }
            }
        }
    }

    private fun navigationLayout(
        width: Int,
        height: Int,
        style: AdaptiveNavigationStyle = AdaptiveNavigationStyle.EdgeColumn,
        sidebarVisible: Boolean = style == AdaptiveNavigationStyle.Rail,
        compactNavigation: CompactNavigation = CompactNavigation.Bar,
        navigationVisible: Boolean = true,
    ): AdaptiveNavigationLayout {
        var drawn: AdaptiveNavigationLayout? = null
        runComposeUiTest {
            setContent {
                Window(width, height) {
                    AdaptiveNavigation(
                        destinations = destinations,
                        selectedValue = "Home",
                        onValueSelected = {},
                        style = style,
                        sidebarVisible = sidebarVisible,
                        compactNavigation = compactNavigation,
                        navigationVisible = navigationVisible,
                    ) { drawn = layout }
                }
            }
            waitForIdle()
        }
        return checkNotNull(drawn)
    }

    @Test
    fun `navigation follows the window`() {
        assertEquals(AdaptiveNavigationLayout.BottomBar, navigationLayout(393, 852))
        assertEquals(AdaptiveNavigationLayout.Rail, navigationLayout(852, 393))
        assertEquals(AdaptiveNavigationLayout.EdgeColumn, navigationLayout(690, 830))
        assertEquals(AdaptiveNavigationLayout.EdgeColumn, navigationLayout(1180, 820))
        assertEquals(AdaptiveNavigationLayout.EdgeColumn, navigationLayout(1440, 900))
    }

    @Test
    fun `the rail style keeps the rail and the sidebar`() {
        val rail = AdaptiveNavigationStyle.Rail
        assertEquals(AdaptiveNavigationLayout.BottomBar, navigationLayout(393, 852, rail))
        assertEquals(AdaptiveNavigationLayout.Rail, navigationLayout(852, 393, rail))
        assertEquals(AdaptiveNavigationLayout.Rail, navigationLayout(690, 830, rail))
        assertEquals(AdaptiveNavigationLayout.Rail, navigationLayout(1180, 820, rail))
        assertEquals(AdaptiveNavigationLayout.Sidebar, navigationLayout(1440, 900, rail))
        assertEquals(AdaptiveNavigationLayout.Rail, navigationLayout(1440, 900, rail, sidebarVisible = false))
    }

    @Test
    fun `no compact navigation leaves a phone without a bar or a rail, and wider windows as they are`() {
        val none = CompactNavigation.None
        assertEquals(AdaptiveNavigationLayout.None, navigationLayout(393, 852, compactNavigation = none))
        assertEquals(AdaptiveNavigationLayout.None, navigationLayout(852, 393, compactNavigation = none))
        assertEquals(AdaptiveNavigationLayout.EdgeColumn, navigationLayout(1180, 820, compactNavigation = none))
        assertEquals(
            AdaptiveNavigationLayout.Rail,
            navigationLayout(1180, 820, AdaptiveNavigationStyle.Rail, compactNavigation = none),
        )
    }

    @Test
    fun `hidden navigation draws nothing in every form`() {
        for ((width, height) in listOf(393 to 852, 852 to 393, 1180 to 820)) {
            assertEquals(AdaptiveNavigationLayout.None, navigationLayout(width, height, navigationVisible = false))
            assertEquals(
                AdaptiveNavigationLayout.None,
                navigationLayout(width, height, AdaptiveNavigationStyle.Rail, navigationVisible = false),
            )
        }
        assertEquals(
            AdaptiveNavigationLayout.None,
            navigationLayout(1440, 900, AdaptiveNavigationStyle.Rail, navigationVisible = false),
        )
    }

    @Test
    fun `hiding the navigation gives the screen the window and keeps its state`() {
        for (style in AdaptiveNavigationStyle.entries) {
            runComposeUiTest {
                var visible by mutableStateOf(true)
                val states = mutableListOf<Any>()
                var screenWidth: Int? = null
                var covered: Dp? = null
                setContent {
                    Window(1180, 820) {
                        AdaptiveNavigation(
                            destinations = destinations,
                            selectedValue = "Home",
                            onValueSelected = {},
                            style = style,
                            navigationVisible = visible,
                        ) {
                            val state = remember { Any() }
                            SideEffect { if (states.lastOrNull() !== state) states += state }
                            covered = contentPadding.calculateEndPadding(LayoutDirection.Ltr) +
                                contentPadding.calculateStartPadding(LayoutDirection.Ltr)
                            Box(Modifier.fillMaxSize().onGloballyPositioned { screenWidth = it.size.width })
                        }
                    }
                }
                waitForIdle()
                assertTrue(screenWidth!! < with(density) { 1180.dp.roundToPx() } || covered!! > 0.dp, "$style shows its navigation")

                visible = false
                waitForIdle()
                assertEquals(with(density) { 1180.dp.roundToPx() }, screenWidth, "$style gives the screen the window")
                assertEquals(0.dp, covered, "$style covers nothing")

                visible = true
                waitForIdle()
                assertEquals(1, states.size, "$style keeps the screen's state")
            }
        }
    }

    @Test
    fun `the screen keeps its state through every change of form`() {
        // Each step is a window, whether the sidebar shows and whether the navigation shows.
        data class Step(val width: Int, val height: Int, val sidebar: Boolean = true, val visible: Boolean = true)
        val steps = listOf(
            Step(1180, 820),
            Step(1440, 900),
            Step(1440, 900, sidebar = false),
            Step(1440, 900),
            Step(393, 852),
            Step(852, 393),
            Step(393, 852, visible = false),
            Step(1180, 820, visible = false),
            Step(1180, 820),
            Step(393, 852),
            Step(1440, 900),
        )
        for (style in AdaptiveNavigationStyle.entries) {
            for (ownBar in listOf(false, true)) {
                runComposeUiTest {
                    var step by mutableStateOf(steps.first())
                    val states = mutableListOf<Any>()
                    val layouts = mutableSetOf<AdaptiveNavigationLayout>()
                    setContent {
                        Window(step.width, step.height) {
                            AdaptiveNavigation(
                                destinations = destinations,
                                selectedValue = "Home",
                                onValueSelected = {},
                                style = style,
                                sidebarVisible = step.sidebar,
                                bottomBar = if (ownBar) ({ Box(Modifier.fillMaxWidth().height(72.dp)) }) else null,
                                navigationVisible = step.visible,
                            ) {
                                val state = remember { Any() }
                                SideEffect {
                                    if (states.lastOrNull() !== state) states += state
                                    layouts += layout
                                }
                            }
                        }
                    }
                    for (next in steps) {
                        step = next
                        waitForIdle()
                    }

                    val expected = if (style == AdaptiveNavigationStyle.Rail) {
                        setOf(
                            AdaptiveNavigationLayout.Rail,
                            AdaptiveNavigationLayout.Sidebar,
                            AdaptiveNavigationLayout.BottomBar,
                            AdaptiveNavigationLayout.None,
                        )
                    } else {
                        setOf(
                            AdaptiveNavigationLayout.EdgeColumn,
                            AdaptiveNavigationLayout.Rail,
                            AdaptiveNavigationLayout.BottomBar,
                            AdaptiveNavigationLayout.None,
                        )
                    }
                    assertEquals(expected, layouts, "$style, own bar $ownBar, went through every form")
                    assertEquals(1, states.size, "$style, own bar $ownBar, kept the screen's state")
                }
            }
        }
    }

    @Test
    fun `an app's own bottom bar floats over the full-height screen and says what it covers`() = runComposeUiTest {
        var screenHeight: Int? = null
        var covered: Dp? = null
        setContent {
            Window(393, 852) {
                AdaptiveNavigation(
                    destinations = destinations,
                    selectedValue = "Home",
                    onValueSelected = {},
                    bottomBar = { Box(Modifier.fillMaxWidth().height(72.dp)) },
                ) {
                    covered = contentPadding.calculateBottomPadding()
                    Box(Modifier.fillMaxSize().onGloballyPositioned { screenHeight = it.size.height })
                }
            }
        }
        waitForIdle()

        assertEquals(72.dp, covered)
        // The screen keeps the window's whole height: the bar is drawn over it, not beside it.
        assertEquals(with(density) { 852.dp.roundToPx() }, screenHeight)
    }

    @Test
    fun `the footer shows on the rail and the sidebar, never on the bottom bar`() {
        fun footerDrawn(width: Int, height: Int): Boolean {
            var drawn = false
            runComposeUiTest {
                setContent {
                    Window(width, height) {
                        AdaptiveNavigation(
                            destinations = destinations,
                            selectedValue = "Home",
                            onValueSelected = {},
                            style = AdaptiveNavigationStyle.Rail,
                            footer = { drawn = true },
                        ) {}
                    }
                }
                waitForIdle()
            }
            return drawn
        }

        assertFalse(footerDrawn(393, 852))
        assertTrue(footerDrawn(1180, 820))
        assertTrue(footerDrawn(1440, 900))
    }

    @Test
    fun `the screen beside a rail reads the space the rail leaves`() = runComposeUiTest {
        var screen: WindowClass? = null
        setContent {
            Window(690, 830) {
                AdaptiveNavigation(destinations, "Home", {}, style = AdaptiveNavigationStyle.Rail) {
                    screen = ArecaTheme.window
                }
            }
        }
        waitForIdle()

        // 690dp less the 80dp touch rail and its divider leaves 609dp: a medium screen, two panes.
        assertEquals(WidthClass.Medium, checkNotNull(screen).width)
    }

    private fun listDetail(width: Int, showDetail: Boolean): Pair<Boolean, List<String>> {
        var twoPane = false
        val drawn = mutableListOf<String>()
        runComposeUiTest {
            setContent {
                Window(width, 800) {
                    ListDetail(
                        showDetail = showDetail,
                        modifier = Modifier.fillMaxSize(),
                        detailPlaceholder = { drawn += "placeholder" },
                        list = {
                            twoPane = isTwoPane
                            drawn += "list"
                        },
                        detail = { drawn += "detail" },
                    )
                }
            }
            waitForIdle()
        }
        return twoPane to drawn.distinct()
    }

    @Test
    fun `list and detail sit side by side from a 600dp container`() {
        assertEquals(true to listOf("list", "placeholder"), listDetail(700, showDetail = false))
        assertEquals(true to listOf("list", "detail"), listDetail(700, showDetail = true))
    }

    @Test
    fun `a narrow container shows one pane at a time`() {
        assertEquals(false to listOf("list"), listDetail(393, showDetail = false))
        val (_, drawn) = listDetail(393, showDetail = true)
        assertEquals(listOf("detail"), drawn)
    }

    @Test
    fun `the list pane widens with the container`() {
        assertEquals(280.dp, AdaptiveDefaults.listPaneWidth(700.dp))
        assertEquals(320.dp, AdaptiveDefaults.listPaneWidth(1000.dp))
        assertEquals(360.dp, AdaptiveDefaults.listPaneWidth(1200.dp))
    }

    private fun supportingInline(width: Int): Boolean {
        var inline: Boolean? = null
        runComposeUiTest {
            setContent {
                Window(width, 800) {
                    SupportingPane(
                        supportingVisible = false,
                        onSupportingDismissRequest = {},
                        modifier = Modifier.fillMaxSize(),
                        main = { inline = isSupportingInline },
                        supporting = {},
                    )
                }
            }
            waitForIdle()
        }
        return checkNotNull(inline)
    }

    @Test
    fun `supporting content stays on screen from a 920dp container`() {
        assertFalse(supportingInline(820))
        assertTrue(supportingInline(1100))
    }

    @Test
    fun `a content column stops growing at its maximum width and centres`() = runComposeUiTest {
        var left = 0.dp
        var width = 0.dp
        setContent {
            Window(1440, 900) {
                ContentColumn(modifier = Modifier.fillMaxSize()) {
                    Box(
                        Modifier.fillMaxWidth().height(10.dp).onGloballyPositioned { coordinates ->
                            left = with(density) { coordinates.positionInRoot().x.toDp() }
                            width = with(density) { coordinates.size.width.toDp() }
                        },
                    )
                }
            }
        }
        waitForIdle()

        // 640dp less the medium gutters of a 640dp column, centred in 1440dp.
        assertEquals(592.dp, width)
        assertEquals(424.dp, left)
    }

    private fun gridColumns(width: Int, minCell: Dp = 160.dp, maxColumns: Int = Int.MAX_VALUE): Int {
        val rows = mutableMapOf<Int, Int>()
        runComposeUiTest {
            setContent {
                Window(width, 800) {
                    AdaptiveGrid(modifier = Modifier.fillMaxSize(), minCellWidth = minCell, maxColumns = maxColumns) {
                        items(12) { index ->
                            Box(
                                Modifier.fillMaxWidth().height(40.dp).onGloballyPositioned { coordinates ->
                                    rows[index] = coordinates.positionInRoot().y.toInt()
                                },
                            )
                        }
                    }
                }
            }
            waitForIdle()
        }
        val firstRow = rows.getValue(0)
        return rows.count { it.value == firstRow }
    }

    @Test
    fun `a grid fits as many columns as its cells allow`() {
        // 393dp less 16dp gutters: two 160dp cells with a 12dp gap.
        assertEquals(2, gridColumns(393))
        // 1440dp caps at 1200dp of cells: seven 160dp cells with 12dp gaps.
        assertEquals(7, gridColumns(1440))
        assertEquals(4, gridColumns(1440, maxColumns = 4))
    }

    @Test
    fun `a rail is narrower under a pointer`() = runComposeUiTest {
        var touch = 0.dp
        var pointer = 0.dp
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) { touch = NavRailSize.Default.width }
            ArecaThemeProvider(inputMode = InputMode.Pointer) { pointer = NavRailSize.Default.width }
        }
        waitForIdle()

        assertEquals(80.dp, touch)
        assertEquals(72.dp, pointer)
    }


    @Test
    fun `sheets become dialogs from an expanded window that is tall enough`() {
        fun dialog(width: Int, height: Int) =
            AdaptiveDefaults.usesDialog(WindowClass(DpSize(width.dp, height.dp), InputMode.Touch))

        assertFalse(dialog(393, 852))
        assertFalse(dialog(690, 830))
        assertFalse(dialog(852, 393))
        assertTrue(dialog(1180, 820))
        assertTrue(dialog(1440, 900))
    }

    @Test
    fun `the host window ignores the pane it is read in`() = runComposeUiTest {
        var host: WindowClass? = null
        var pane: WindowClass? = null
        setContent {
            Window(1440, 900) {
                WindowClassBox(Modifier.size(360.dp, 900.dp)) {
                    host = ArecaTheme.hostWindow
                    pane = ArecaTheme.window
                }
            }
        }
        waitForIdle()

        assertEquals(WidthClass.Large, checkNotNull(host).width)
        assertEquals(WidthClass.Compact, checkNotNull(pane).width)
    }
}
