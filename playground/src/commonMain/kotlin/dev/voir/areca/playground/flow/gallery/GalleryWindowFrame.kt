package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset

/**
 * An adaptive gallery screen at [preset], as tall as the page's preview allows.
 *
 * Hands [WindowFrame] the preview area's height, so a phone fills the preview as it does in
 * `DeviceFrame` and a wide window shrinks to fit its width — the page never chooses a scale of its
 * own.
 *
 * @param preset Window to render at.
 * @param content The screen.
 */
@Composable
internal fun GalleryWindowFrame(preset: WindowPreset, content: @Composable () -> Unit) {
    BoxWithConstraints(contentAlignment = Alignment.Center) {
        WindowFrame(preset = preset, maxHeight = maxHeight, content = content)
    }
}
