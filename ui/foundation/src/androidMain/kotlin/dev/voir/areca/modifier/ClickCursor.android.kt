package dev.voir.areca.modifier

import androidx.compose.ui.Modifier

/**
 * No-op on Android.
 *
 * Android draws no cursor for touch, and the system already renders its own pointer for a connected
 * mouse or stylus, so a hover icon here would change nothing the user sees.
 */
public actual fun Modifier.clickCursor(enabled: Boolean): Modifier = this
