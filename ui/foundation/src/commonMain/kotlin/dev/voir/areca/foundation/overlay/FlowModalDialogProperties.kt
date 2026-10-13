package dev.voir.areca.foundation.overlay

import androidx.compose.ui.window.DialogProperties

/**
 * [DialogProperties] for a [FlowModal]: a full-width window that draws edge to edge, behind the
 * status bar, the navigation bar and any display cutout.
 *
 * A dialog left to the platform defaults is inset by those bars, so the sheet stops at the top of
 * the navigation bar while the platform's dim still covers the bar beneath it, leaving a strip of
 * overlay under the sheet. Drawing edge to edge lets the sheet reach the bottom of the screen;
 * [FlowModal] pads its content by the bars itself. The same window as [edgeToEdgeDialogProperties].
 *
 * Pass the result to [rememberFlowModal] when a flow needs to change the dismissal flags, so the
 * window keeps drawing edge to edge.
 *
 * @param dismissOnBackPress Whether the back gesture or button dismisses the modal.
 * @param dismissOnClickOutside Whether a press outside the window dismisses the modal.
 * @return Properties for the platform dialog that hosts the modal.
 */
public fun flowModalDialogProperties(
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
): DialogProperties = edgeToEdgeDialogProperties(
    dismissOnBackPress = dismissOnBackPress,
    dismissOnClickOutside = dismissOnClickOutside,
)
