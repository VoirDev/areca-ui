package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

/** The window's dim is black at the theme's amount, so the component draws the scrim. */
internal actual val platformDrawsModalScrim: Boolean = false

/**
 * Android's dialog window takes no scrim colour; the properties are returned unchanged.
 *
 * @param scrim Colour of the scrim behind the window's content.
 * @return The receiver, unchanged.
 */
internal actual fun DialogProperties.withScrim(scrim: Color): DialogProperties = this

/**
 * Sets the dim amount of the enclosing dialog window to zero. The window goes away with the
 * dialog, so there is nothing to put back.
 */
@Composable
internal actual fun ClearPlatformDim() {
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window ?: return
    DisposableEffect(window) {
        window.setDimAmount(0f)
        onDispose {}
    }
}
