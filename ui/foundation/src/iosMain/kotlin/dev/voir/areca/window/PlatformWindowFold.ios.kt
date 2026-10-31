package dev.voir.areca.window

import androidx.compose.runtime.Composable

/** None on iOS, which reports no fold to an app. */
@Composable
internal actual fun platformWindowFold(): WindowFold? = null
