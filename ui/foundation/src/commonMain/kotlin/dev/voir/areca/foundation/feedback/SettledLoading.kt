package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

/**
 * Whether to show a loading face for [loading], with the flicker taken out.
 *
 * A load that finishes within [delay] never shows a skeleton at all, and one that does show keeps it
 * for at least [minimumDuration], so a fast network does not flash a placeholder for one frame and
 * a slow one does not swap it out the instant it has appeared.
 *
 * ```kotlin
 * val showSkeleton = rememberSettledLoading(state.loading)
 * if (showSkeleton) ListItemSkeleton() else Rows(state.items)
 * ```
 *
 * @param loading Whether the content is loading now.
 * @param delay How long a load runs before its loading face shows.
 * @param minimumDuration How long a loading face, once shown, stays.
 * @return Whether to show the loading face.
 */
@Composable
public fun rememberSettledLoading(
    loading: Boolean,
    delay: Duration = 150.milliseconds,
    minimumDuration: Duration = 400.milliseconds,
): Boolean {
    var showing by remember { mutableStateOf(false) }
    var shownAt by remember { mutableStateOf<TimeSource.Monotonic.ValueTimeMark?>(null) }

    LaunchedEffect(loading) {
        if (loading) {
            if (!showing) {
                delay(delay)
                showing = true
                shownAt = TimeSource.Monotonic.markNow()
            }
        } else if (showing) {
            val elapsed = shownAt?.elapsedNow() ?: minimumDuration
            if (elapsed < minimumDuration) delay(minimumDuration - elapsed)
            showing = false
            shownAt = null
        }
    }
    return showing
}
