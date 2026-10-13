package dev.voir.areca.foundation.overlay

import androidx.compose.ui.window.DialogProperties

/**
 * [DialogProperties] for a window that draws edge to edge: full width, and behind the status bar, the
 * navigation bar and any display cutout.
 *
 * A dialog left to the platform defaults is inset by those bars and leaves them to the window's
 * theme, so whatever the dialog paints stops short of the screen's edges and the bars read as bands
 * above and below it. Drawing edge to edge lets it reach them; the content then pads itself by
 * `WindowInsets.safeDrawing` or by what it needs to clear. Pair it with
 * [dev.voir.areca.platform.SystemBars] for a dialog that also sets the bars' colour and icons.
 *
 * [FlowModal] uses it through [flowModalDialogProperties].
 *
 * @param dismissOnBackPress Whether the back gesture or button dismisses the dialog.
 * @param dismissOnClickOutside Whether a press outside the dialog's content dismisses it.
 * @return Properties for the platform dialog.
 */
public expect fun edgeToEdgeDialogProperties(
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
): DialogProperties
