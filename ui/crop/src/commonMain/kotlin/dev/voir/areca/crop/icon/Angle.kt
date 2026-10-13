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
 * Angle icon.
 *
 * Converted from Tabler Icons `angle` (MIT).
 *
 * Copy of `ArecaIcons.Angle` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.crop.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Angle: ImageVector
    get() = cachedAngle ?: ImageVector.Builder(
        name = "Angle",
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
            moveTo(21f, 19f)
            horizontalLineTo(3f)
            lineTo(12f, 4f)
            moveTo(20.615f, 15.171f)
            horizontalLineTo(20.63f)
            moveTo(19.515f, 11.771f)
            horizontalLineTo(19.53f)
            moveTo(17.715f, 8.671f)
            horizontalLineTo(17.73f)
            moveTo(15.415f, 5.971f)
            horizontalLineTo(15.43f)
        }
    }.build().also { cachedAngle = it }

/** `Angle` vector, built on first access and reused afterwards. */
private var cachedAngle: ImageVector? = null
