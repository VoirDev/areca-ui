package dev.voir.areca.foundation.overlay

import androidx.compose.ui.window.DialogProperties

/**
 * Turns off `usePlatformInsets`, which otherwise limits the dialog's content to the safe area;
 * the content applies those insets itself. The keyboard inset stays with the platform,
 * which then leaves it out of the insets the content sees, so it is never applied twice.
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
    usePlatformInsets = false,
)
