package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Pages swiped sideways, with their controls docked beneath them — an onboarding tour, a feature
 * walkthrough, a what's-new sequence.
 *
 * The pages take all the height the controls leave. Swiping moves them; so does anything holding
 * [controller], inside [controls] or elsewhere on the screen, such as a back or skip control in a
 * top bar. The controls stay put while the pages move under a finger, which is why they are here
 * rather than on each page.
 *
 * @param controller Moves the pages and reports where they are. From [rememberPagerController].
 * @param modifier Modifier applied to the pages and controls together.
 * @param userScrollEnabled Whether the pages can be swiped. Turn it off only for pages that must be
 * left through a button, such as one that has to be answered.
 * @param beyondViewportPageCount Pages composed either side of the visible one, ahead of a swipe.
 * @param controls Drawn under the pages, normally [PagerControls].
 * @param page Draws the page at an index.
 */
@Composable
public fun SwipePager(
    controller: PagerController,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    beyondViewportPageCount: Int = 0,
    controls: (@Composable () -> Unit)? = null,
    page: @Composable (index: Int) -> Unit,
) {
    Column(modifier = modifier) {
        HorizontalPager(
            state = controller.pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = userScrollEnabled,
            beyondViewportPageCount = beyondViewportPageCount,
        ) { index ->
            page(index)
        }

        controls?.invoke()
    }
}
