package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.select.SelectFlowModal
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A [FlowModal] is as tall as its content up to its cap, has a partial height only when its content
 * is taller than that, and says in its state which height it shows.
 */
@OptIn(ExperimentalTestApi::class)
class FlowModalHeightTest {

    private val threeOptions = listOf("System", "Light", "Dark").map { SelectOption(it, it, it) }

    /** Height of the window the sheet opens in: the scrim covers it. */
    private fun ComposeUiTest.windowHeight(): Dp =
        onNodeWithContentDescription("Dismiss").getBoundsInRoot().let { it.bottom - it.top }

    /** Distance from the window's top to the sheet's header. */
    private fun ComposeUiTest.headerTop(title: String): Dp = onNodeWithText(title).getBoundsInRoot().top

    @Test
    fun `a short sheet opens at the height of its content, fully, on the first opening`() = runComposeUiTest {
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal()
                LaunchedEffect(Unit) { controller.show(partial = true) }
                FlowModal(controller = controller, header = { Text("Short") }) {
                    repeat(3) { Text("Row $it") }
                }
            }
        }
        waitForIdle()

        assertEquals(FlowModalState.Expanded, controller.state)
        assertTrue(headerTop("Short") > windowHeight() * 0.6f, "a three-row sheet takes the bottom of the window")
    }

    @Test
    fun `a tall sheet opens partially, expands to its cap and keeps its footer in sight`() = runComposeUiTest {
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal(allowPartial = true, maxHeightFraction = 0.9f, partialMultiplier = 0.5f)
                LaunchedEffect(Unit) { controller.show(partial = true) }
                FlowModal(
                    controller = controller,
                    header = { Text("Tall") },
                    footer = { Text("Footer") },
                ) {
                    LazyColumn { items((1..200).toList()) { Text("Row $it") } }
                }
            }
        }
        waitForIdle()
        val window = windowHeight()

        assertEquals(FlowModalState.PartiallyOpened, controller.state)
        val partialTop = headerTop("Tall")
        assertTrue(partialTop > window * 0.5f && partialTop < window * 0.6f, "partial: half of the cap, at $partialTop of $window")
        assertTrue(onNodeWithText("Footer").getBoundsInRoot().bottom <= window, "the footer shows while partial")

        controller.expand()
        waitForIdle()

        assertEquals(FlowModalState.Expanded, controller.state)
        assertTrue(headerTop("Tall") < window * 0.15f, "expanded: up to the cap")
        assertTrue(onNodeWithText("Footer").getBoundsInRoot().bottom <= window, "the footer shows while expanded")
    }

    @Test
    fun `a tall form opens at its full height by default, with nothing cut off`() = runComposeUiTest {
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal()
                LaunchedEffect(Unit) { controller.show() }
                FlowModal(controller = controller, header = { Text("Form") }, footer = { Text("Save") }) {
                    LazyColumn { items((1..200).toList()) { Text("Field $it") } }
                }
            }
        }
        waitForIdle()

        assertEquals(FlowModalState.Expanded, controller.state)
        assertTrue(headerTop("Form") < windowHeight() * 0.15f, "opens at the cap, not partway")
    }

    @Test
    fun `a short picker keeps its height when the screen behind it recomposes`() = runComposeUiTest {
        var ticks by mutableIntStateOf(0)
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal(allowPartial = false, allowExpand = true)
                Text("tick $ticks")
                LaunchedEffect(Unit) { controller.show() }
                SelectFlowModal(controller = controller, title = "Theme", values = setOf("System"), options = threeOptions)
            }
        }
        waitForIdle()
        val before = headerTop("Theme")

        ticks++
        waitForIdle()

        assertEquals(FlowModalState.Expanded, controller.state)
        assertEquals(before, headerTop("Theme"))
        assertTrue(before > windowHeight() * 0.5f, "three options take the bottom of the window")
    }

    @Test
    fun `a searchable picker takes the whole sheet`() = runComposeUiTest {
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal(allowPartial = false)
                LaunchedEffect(Unit) { controller.show() }
                SelectFlowModal(
                    controller = controller,
                    title = "Bank",
                    values = emptySet(),
                    options = threeOptions,
                    onQueryChange = {},
                )
            }
        }
        waitForIdle()

        assertTrue(headerTop("Bank") < windowHeight() * 0.15f, "a search keeps the sheet at its cap")
    }

    @Test
    fun `content that grows while the sheet opens bends the motion instead of restarting it`() = runComposeUiTest {
        mainClock.autoAdvance = false
        var loaded by mutableStateOf(false)
        lateinit var controller: FlowModalController
        setContent {
            ArecaThemeProvider {
                controller = rememberFlowModal()
                LaunchedEffect(Unit) { controller.show() }
                FlowModal(controller = controller, header = { Text("Details") }) {
                    // A placeholder first, then the data, as a sheet that loads what it shows.
                    Column { repeat(if (loaded) 24 else 3) { Text("Row $it") } }
                }
            }
        }
        val tops = mutableListOf<Dp>()
        fun frame() {
            mainClock.advanceTimeByFrame()
            tops += headerTop("Details")
        }
        repeat(8) { frame() }
        val grownAt = tops.size
        loaded = true
        repeat(60) { frame() }

        val rises = tops.zipWithNext { above, below -> above - below }
        val before = rises[grownAt - 2]
        assertTrue(before > 0.dp, "the sheet was still rising when the content grew: $tops")
        val after = rises.subList(grownAt - 1, grownAt + 4)
        assertTrue(
            after.all { it >= before * 0.5f },
            "the sheet keeps its speed toward the taller height: rose $before a frame before, then $after",
        )
        assertEquals(FlowModalState.Expanded, controller.state)
        assertTrue(tops.last() < tops[grownAt - 1], "the sheet ends taller than the placeholder made it")
    }
}
