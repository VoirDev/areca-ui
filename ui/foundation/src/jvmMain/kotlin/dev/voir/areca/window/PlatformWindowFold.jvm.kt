package dev.voir.areca.window

import androidx.compose.runtime.Composable

/** None on the desktop: a desktop window has no fold. */
@Composable
internal actual fun platformWindowFold(): WindowFold? = null
