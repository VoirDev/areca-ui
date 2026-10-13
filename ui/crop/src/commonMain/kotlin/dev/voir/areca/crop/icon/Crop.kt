// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.crop.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Crop icon.
 *
 * Converted from Lucide `crop` (ISC).
 *
 * Copy of `ArecaIcons.Crop` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.crop.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Crop: ImageVector
    get() = cachedCrop ?: ImageVector.Builder(
        name = "Crop",
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
            moveTo(6f, 2f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 8f, 18f)
            horizontalLineTo(22f)
            moveTo(18f, 22f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 16f, 6f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedCrop = it }

/** `Crop` vector, built on first access and reused afterwards. */
private var cachedCrop: ImageVector? = null
