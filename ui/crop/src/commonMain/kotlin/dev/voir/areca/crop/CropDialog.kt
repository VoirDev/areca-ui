package dev.voir.areca.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import dev.voir.areca.foundation.overlay.edgeToEdgeDialogProperties
import dev.voir.areca.platform.SystemBars

/**
 * Shows the application's crop screen over everything, edge to edge, in the cropper's color.
 *
 * A crop screen drawn over a screen of the product inherits that window's system bars: on Android
 * their light scrims and dark icons, which read as lighter bands above and below a dark editor. This
 * window draws behind the status bar, the navigation bar and any cutout, paints them with [color],
 * and asks for light bar icons and no contrast scrim while it is up. Nothing of the screen behind
 * shows through, so there is no dim either.
 *
 * The back gesture goes to the [CropToolbar] inside first: it closes an open tool, or asks before
 * throwing changes away. Only when there is nothing to close or confirm is [onDismissRequest] called.
 *
 * The content is laid out under the bars: pad it by `WindowInsets.safeDrawing`, as the crop screen
 * in the README's quick start does with its `windowInsets`.
 *
 * On iOS the bars are painted, but the status bar's text keeps the style the application's view
 * controller gives it; on the desktop the window is simply full size.
 *
 * @param onDismissRequest Called by the back gesture or button when the toolbar has nothing to close
 *   or confirm; usually the screen's Cancel.
 * @param color Ground of the whole window, bars included. Defaults to the stage's, from
 *   [LocalImageCropperStyle] when one is provided, so the editor and the bars read as one surface.
 * @param content The crop screen.
 */
@Composable
public fun CropDialog(
    onDismissRequest: () -> Unit,
    color: Color = (LocalImageCropperStyle.current ?: ImageCropperStyle.Default).stage,
    content: @Composable () -> Unit,
) {
    val back = remember { CropBackHandler() }
    Dialog(
        onDismissRequest = { if (back.onBack?.invoke() != true) onDismissRequest() },
        properties = edgeToEdgeDialogProperties(dismissOnClickOutside = false),
    ) {
        SystemBars(color)
        CompositionLocalProvider(LocalCropBackHandler provides back) {
            Box(modifier = Modifier.fillMaxSize().background(color)) {
                content()
            }
        }
    }
}

/**
 * Where a [CropToolbar] inside a [CropDialog] registers its answer to the back gesture.
 *
 * Common code has no back handler of its own, and the dialog is what receives the gesture, so the
 * dialog asks the toolbar first.
 *
 * Not thread-safe: the toolbar registers and clears its answer from a composition effect, and the
 * dialog reads it on the main thread.
 */
internal class CropBackHandler {
    /**
     * Called on back; returns `true` when it handled the gesture, so the dialog stays. `null` while no
     * toolbar is registered.
     */
    var onBack: (() -> Boolean)? = null
}

/** The [CropBackHandler] of the enclosing [CropDialog], or `null` outside one. */
internal val LocalCropBackHandler = staticCompositionLocalOf<CropBackHandler?> { null }
