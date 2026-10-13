package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

/*
 * Who draws the scrim behind a [Dialog] or a [FlowModal].
 *
 * Every platform's dialog window already dims what is behind it, so a component that painted its
 * own scrim on top would darken the screen twice. On iOS and the desktop the window's scrim takes
 * any colour, so it is handed the style's overlay and the component draws none. On Android the
 * window's dim is black at a fixed amount, which cannot follow a style or a dragged sheet, so the
 * component clears it and draws the scrim itself.
 */

/** Whether the dialog window draws the scrim, in the colour given to [withScrim]. */
internal expect val platformDrawsModalScrim: Boolean

/**
 * These properties with the window's scrim set to [scrim].
 *
 * @param scrim Colour of the scrim behind the window's content.
 * @return The same properties with [scrim] where the window draws one; unchanged where it does not
 *   (see [platformDrawsModalScrim]).
 */
internal expect fun DialogProperties.withScrim(scrim: Color): DialogProperties

/**
 * Turns off the dim the platform lays behind the dialog window this is composed in, for a
 * component that draws its own scrim. Does nothing where the window's scrim is already the
 * component's (see [platformDrawsModalScrim]).
 */
@Composable
internal expect fun ClearPlatformDim()
