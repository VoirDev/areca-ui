package dev.voir.areca.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Sets the system bars of the window this is composed in, for as long as it stays composed.
 *
 * For a screen that paints its own ground under the bars and needs the bars to read as part of it —
 * a dark editor or viewer in an otherwise light application. Composed inside a dialog it changes the
 * dialog's window; anywhere else, the activity's. What it changed is put back when it leaves the
 * composition, so a destination can call it and the screens around it keep their own bars.
 *
 * On Android:
 * - the bars are painted with [color], which shows before Android 15; from 15 every window is edge
 *   to edge and the screen's own ground shows through instead, so draw it behind the bars;
 * - the contrast scrim Android lays over the bars is turned off, so they show [color] or the ground,
 *   not a translucent band;
 * - the icons are light or dark as [lightIcons] says;
 * - in a dialog's window, the dim behind the window is removed. A dialog that paints its bars covers
 *   the screen, and the dim would only show while it fades in.
 *
 * On iOS the status bar's text keeps the style the application's view controller gives it, and on
 * the desktop there are no bars; both do nothing.
 *
 * Pair it with a window that draws behind the bars: an activity drawing edge to edge, or a dialog
 * with [dev.voir.areca.foundation.overlay.edgeToEdgeDialogProperties].
 *
 * @param color Color behind the bars.
 * @param lightIcons Whether the bars' icons are light, for a dark [color]. Defaults to whichever
 *   reads on [color].
 */
@Composable
public expect fun SystemBars(color: Color, lightIcons: Boolean = color.luminance() < 0.5f)
