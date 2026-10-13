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
 * Triangle alert icon.
 *
 * Converted from Lucide `triangle-alert` (ISC).
 *
 * Copy of `ArecaIcons.AlertTriangle` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.AlertTriangle: ImageVector
    get() = cachedAlertTriangle ?: ImageVector.Builder(
        name = "AlertTriangle",
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
            moveTo(21.73f, 18f)
            lineTo(13.73f, 4f)
            arcTo(2f, 2f, 0f, false, false, 10.25f, 4f)
            lineTo(2.25f, 18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 21.73f, 18f)
            moveTo(12f, 9f)
            verticalLineTo(13f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedAlertTriangle = it }

/** `AlertTriangle` vector, built on first access and reused afterwards. */
private var cachedAlertTriangle: ImageVector? = null
