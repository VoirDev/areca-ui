package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.status.StatusScene
import dev.voir.areca.foundation.status.StatusSceneActions
import dev.voir.areca.theme.ArecaTheme

/**
 * The screen when the profile could not be loaded and there is therefore nothing to show.
 *
 * Only for that case. A profile that loaded from memory but could not be refreshed is still a
 * profile, and takes [StaleDetailsNotice] above it instead; a section that failed on its own — the
 * device list, say — reports it inside that section and leaves the rest of the screen working.
 *
 * A foundation `StatusScene` with no mark: the heading, the message under it and the retry button,
 * centred on the space it is given and inset by the page's widest gutter. It is the whole screen, so
 * it owns its width: on a tablet or a desktop it stops at [AdaptiveDefaults.ReadableWidth], centred,
 * so the message keeps a readable line instead of running across the window.
 *
 * @param message What went wrong, already worded.
 * @param onRetry Called to try again.
 * @param modifier Modifier applied to the screen. Fills the space it is given.
 * @param title Heading.
 * @param retrying Whether the retry is in flight: the button shows its spinner and refuses a second
 *   press, so the screen says it heard the first one.
 * @param retryLabel Label of the retry button.
 */
@Composable
public fun ProfileLoadFailure(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Couldn't load your account",
    retrying: Boolean = false,
    retryLabel: String = "Try again",
) {
    StatusScene(
        title = title,
        modifier = modifier
            .padding(ArecaTheme.spacing.xl3)
            // Narrower than this on a phone, where it changes nothing.
            .wrapContentWidth()
            .widthIn(max = AdaptiveDefaults.ReadableWidth),
        message = message,
        actions = {
            StatusSceneActions(
                primary = retryLabel,
                onPrimary = onRetry,
                loading = retrying,
            )
        },
    )
}
