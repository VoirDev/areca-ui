// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.flow.profile.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Camera icon.
 *
 * Converted from Lucide `camera` (ISC).
 *
 * Copy of `ArecaIcons.Camera` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Camera: ImageVector
    get() = cachedCamera ?: ImageVector.Builder(
        name = "Camera",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(13.997f, 4f)
            arcTo(2f, 2f, 0f, false, true, 15.757f, 5.05f)
            lineTo(16.243f, 5.95f)
            arcTo(2f, 2f, 0f, false, false, 18.003f, 7f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 9f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 4f, 7f)
            horizontalLineTo(5.997f)
            arcTo(2f, 2f, 0f, false, false, 7.756f, 5.952f)
            lineTo(8.245f, 5.048f)
            arcTo(2f, 2f, 0f, false, true, 10.004f, 4f)
            close()
            moveTo(9f, 13f)
            arcTo(3f, 3f, 0f, true, true, 15f, 13f)
            arcTo(3f, 3f, 0f, true, true, 9f, 13f)
            close()
        }
    }.build().also { cachedCamera = it }

/** `Camera` vector, built on first access and reused afterwards. */
private var cachedCamera: ImageVector? = null
