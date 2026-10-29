package dev.voir.areca.media.carousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

/** How a [Carousel] moves, under a finger, from its state, and on its own. */
@OptIn(ExperimentalTestApi::class)
class CarouselBehaviourTest {
    @Test
    fun `a swipe moves to the next item`() = runComposeUiTest {
        lateinit var state: CarouselState
        setContent {
            ArecaThemeProvider {
                state = rememberCarouselState(itemCount = 5)
                Carousel(state, Modifier.width(300.dp).height(200.dp).testTag("carousel")) { Box(Modifier.fillMaxWidth()) }
            }
        }

        onNodeWithTag("carousel").performTouchInput { swipeLeft() }
        waitForIdle()

        assertEquals(1, state.currentItem)
    }

    @Test
    fun `two to a page move as a pair and are counted as pages`() = runComposeUiTest {
        lateinit var state: CarouselState
        setContent {
            ArecaThemeProvider {
                state = rememberCarouselState(itemCount = 7)
                Carousel(
                    state,
                    Modifier.width(300.dp).height(200.dp).testTag("carousel"),
                    pageSize = CarouselPageSize.PerPage(2),
                    snap = CarouselSnap.Page,
                ) { Box(Modifier.fillMaxWidth()) }
            }
        }
        waitForIdle()
        assertEquals(4, state.pageCount)

        onNodeWithTag("carousel").performTouchInput { swipeLeft() }
        waitForIdle()

        assertEquals(1, state.currentPage)
        assertEquals(2, state.currentItem)
    }

    @Test
    fun `going to an item from outside lands on its page`() = runComposeUiTest {
        lateinit var state: CarouselState
        setContent {
            ArecaThemeProvider {
                state = rememberCarouselState(itemCount = 5)
                Carousel(state, Modifier.width(300.dp).height(200.dp)) { Box(Modifier.fillMaxWidth()) }
            }
        }

        runOnIdle { state.goToItem(3, animated = false) }
        waitForIdle()

        assertEquals(3, state.currentItem)
    }

    @Test
    fun `a looping carousel runs on from the last item to the first`() = runComposeUiTest {
        lateinit var state: CarouselState
        setContent {
            ArecaThemeProvider {
                state = rememberCarouselState(itemCount = 3, initialItem = 2, loop = true)
                Carousel(state, Modifier.width(300.dp).height(200.dp)) { Box(Modifier.fillMaxWidth()) }
            }
        }

        runOnIdle { state.next() }
        waitForIdle()

        assertEquals(0, state.currentItem)
    }

    @Test
    fun `autoplay advances once the period has run`() = runComposeUiTest {
        mainClock.autoAdvance = false
        lateinit var state: CarouselState
        setContent {
            ArecaThemeProvider {
                state = rememberCarouselState(itemCount = 3)
                Carousel(state, Modifier.width(300.dp).height(200.dp), autoplay = 2.seconds) { Box(Modifier.fillMaxWidth()) }
            }
        }

        mainClock.advanceTimeBy(1_000)
        assertEquals(0, state.currentPage)
        mainClock.advanceTimeBy(2_500)

        assertEquals(1, state.currentPage)
    }
}
