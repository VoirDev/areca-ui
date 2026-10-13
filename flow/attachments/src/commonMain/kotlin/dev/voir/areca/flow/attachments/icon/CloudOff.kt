// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.flow.attachments.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud off icon.
 *
 * Converted from Lucide `cloud-off` (ISC).
 *
 * Copy of `ArecaIcons.CloudOff` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.attachments.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.CloudOff: ImageVector
    get() = cachedCloudOff ?: ImageVector.Builder(
        name = "CloudOff",
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
            moveTo(10.94f, 5.274f)
            arcTo(7f, 7f, 0f, false, true, 15.71f, 10f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, false, true, 21.722f, 16.057f)
            moveTo(18.796f, 18.81f)
            arcTo(4.5f, 4.5f, 0f, false, true, 17.5f, 19f)
            horizontalLineTo(9f)
            arcTo(7f, 7f, 0f, false, true, 5.79f, 5.78f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedCloudOff = it }

/** `CloudOff` vector, built on first access and reused afterwards. */
private var cachedCloudOff: ImageVector? = null
