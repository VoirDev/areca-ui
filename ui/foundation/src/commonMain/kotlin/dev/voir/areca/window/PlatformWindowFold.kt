package dev.voir.areca.window

import androidx.compose.runtime.Composable

/**
 * The fold or hinge across the window this composition is hosted in, in window coordinates, or
 * `null` for none. Follows the device as it is opened, closed and propped up.
 *
 * @return The window's fold, or `null`.
 */
@Composable
internal expect fun platformWindowFold(): WindowFold?
