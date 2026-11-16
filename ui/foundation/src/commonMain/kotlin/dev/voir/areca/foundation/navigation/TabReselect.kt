package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Taps on the destination that is already selected, for the screen of that destination.
 *
 * Tapping the selected tab again is the platforms' way back to the top of it.
 * [dev.voir.areca.foundation.adaptive.AdaptiveNavigation] provides it to the selected destination's
 * screen; an app that draws its own `TabBar` or `FloatingTabBar` provides a flow of its own, emitting
 * when `onValueSelected` reports the value already selected. Switching destinations never emits, so a
 * screen left scrolled is found scrolled on the way back. Outside a navigation shell it never emits.
 */
public val LocalTabReselect: ProvidableCompositionLocal<Flow<Unit>> = staticCompositionLocalOf { emptyFlow() }

/**
 * Runs [action] each time the destination this is composed in is selected again.
 *
 * @param action What the tap does — usually a scroll back to the top, or a refresh.
 */
@Composable
public fun OnTabReselect(action: suspend () -> Unit) {
    val reselects = LocalTabReselect.current
    val currentAction by rememberUpdatedState(action)
    LaunchedEffect(reselects) {
        reselects.collect { currentAction() }
    }
}

/** Scrolls this column back to its top each time its destination is selected again. */
@Composable
public fun ScrollState.ScrollToTopOnTabReselect() {
    OnTabReselect { animateScrollTo(0) }
}

/** Scrolls this list back to its first item each time its destination is selected again. */
@Composable
public fun LazyListState.ScrollToTopOnTabReselect() {
    OnTabReselect { animateScrollToItem(0) }
}

/** Scrolls this grid back to its first item each time its destination is selected again. */
@Composable
public fun LazyGridState.ScrollToTopOnTabReselect() {
    OnTabReselect { animateScrollToItem(0) }
}
