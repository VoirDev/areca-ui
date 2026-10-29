// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.media.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Photo x icon.
 *
 * Converted from Tabler Icons `photo-x` (MIT).
 *
 * Copy of `ArecaIcons.ImageX` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.media.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.ImageX: ImageVector
    get() = cachedImageX ?: ImageVector.Builder(
        name = "ImageX",
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
            moveTo(15f, 8f)
            horizontalLineTo(15.01f)
            moveTo(13f, 21f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 18f)
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 6f, 3f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 6f)
            verticalLineTo(13f)
            moveTo(3f, 16f)
            lineTo(8f, 11f)
            curveTo(8.928f, 10.107f, 10.072f, 10.107f, 11f, 11f)
            lineTo(14f, 14f)
            moveTo(14f, 14f)
            lineTo(15f, 13f)
            curveTo(15.928f, 12.107f, 17.072f, 12.107f, 18f, 13f)
            moveTo(22f, 22f)
            lineTo(17f, 17f)
            moveTo(17f, 22f)
            lineTo(22f, 17f)
        }
    }.build().also { cachedImageX = it }

/** `ImageX` vector, built on first access and reused afterwards. */
private var cachedImageX: ImageVector? = null
