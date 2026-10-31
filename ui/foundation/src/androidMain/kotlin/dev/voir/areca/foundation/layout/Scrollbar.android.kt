package dev.voir.areca.foundation.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Nothing on Android: a scrolling element there moves under the finger and shows its own indicator. */
@Composable
internal actual fun PlatformScrollbar(target: ScrollbarTarget, vertical: Boolean, modifier: Modifier) {
}
