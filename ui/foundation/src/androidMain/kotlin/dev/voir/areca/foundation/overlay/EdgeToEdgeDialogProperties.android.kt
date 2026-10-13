package dev.voir.areca.foundation.overlay

import androidx.compose.ui.window.DialogProperties

/**
 * Turns off `decorFitsSystemWindows`, which otherwise pads the dialog's content by the system bars
 * and the keyboard; the content applies those insets itself.
 *
 * @param dismissOnBackPress Whether the back gesture or button dismisses the dialog.
 * @param dismissOnClickOutside Whether a press outside the dialog's content dismisses it.
 * @return Properties for the platform dialog.
 */
public actual fun edgeToEdgeDialogProperties(
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
): DialogProperties = DialogProperties(
    dismissOnBackPress = dismissOnBackPress,
    dismissOnClickOutside = dismissOnClickOutside,
    usePlatformDefaultWidth = false,
    decorFitsSystemWindows = false,
)
