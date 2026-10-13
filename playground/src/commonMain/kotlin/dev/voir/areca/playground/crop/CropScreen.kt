package dev.voir.areca.playground.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.crop.CropToolbar
import dev.voir.areca.crop.ImageCropper
import dev.voir.areca.crop.ImageCropperStyle
import dev.voir.areca.crop.rememberImageCropState

/**
 * The application's crop screen: the one an application writes once and opens from every place it
 * crops, each time with that place's [CropSpec].
 *
 * It is the `:ui:crop` README's quick start, shared by the cropper's own page and by the flow recipes
 * that open it, the way a product shares one screen between its avatar and its attachments. Kept
 * `internal` so the render tests can draw it.
 *
 * @param photo The photo, decoded by the application.
 * @param spec What this use of the cropper decides.
 * @param title Heading of the screen, in the middle of the toolbar's bar.
 * @param onCancel Called by the bar's Cancel: the screen is left without a crop.
 * @param onDone Receives the rendered crop when the bar's Done is pressed.
 * @param windowInsets Regions the system occupies.
 * @param applyMask Whether the rendered crop is cut to the spec's mask; `false` keeps it rectangular
 *   for a component that clips it where it is shown.
 */
@Composable
internal fun CropScreen(
    photo: Painter,
    spec: CropSpec,
    title: String,
    onCancel: () -> Unit,
    onDone: (ImageBitmap) -> Unit,
    windowInsets: WindowInsets,
    applyMask: Boolean = true,
) {
    val state = rememberImageCropState(spec, key = photo)

    // The cropper is dark whatever the theme; paint the system bars' regions with its stage.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ImageCropperStyle.Default.stage)
            .windowInsetsPadding(windowInsets),
    ) {
        ImageCropper(
            painter = photo,
            state = state,
            contentDescription = "Photo being cropped",
            modifier = Modifier.weight(1f).fillMaxWidth(),
        )
        CropToolbar(
            state = state,
            title = title,
            onCancel = onCancel,
            onDone = { state.render(photo, applyMask = applyMask)?.let(onDone) },
        )
    }
}
