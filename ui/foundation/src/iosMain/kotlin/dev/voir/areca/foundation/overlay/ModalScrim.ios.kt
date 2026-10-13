package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

/** The dialog layer draws `DialogProperties.scrimColor` over the whole window and fades it. */
internal actual val platformDrawsModalScrim: Boolean = true

/**
 * Copies every property, `animateTransition` included, with `scrimColor` replaced.
 *
 * @param scrim Colour of the scrim behind the window's content.
 * @return A copy of the receiver whose window scrim is [scrim].
 */
@OptIn(ExperimentalComposeUiApi::class)
internal actual fun DialogProperties.withScrim(scrim: Color): DialogProperties = DialogProperties(
    dismissOnBackPress = dismissOnBackPress,
    dismissOnClickOutside = dismissOnClickOutside,
    usePlatformDefaultWidth = usePlatformDefaultWidth,
    usePlatformInsets = usePlatformInsets,
    useSoftwareKeyboardInset = useSoftwareKeyboardInset,
    scrimColor = scrim,
    animateTransition = animateTransition,
)

/** The window's scrim is the component's own, so there is no dim to clear. */
@Composable
internal actual fun ClearPlatformDim() = Unit
