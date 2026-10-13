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
 * Monitor icon.
 *
 * Converted from Lucide `monitor` (ISC).
 *
 * Copy of `ArecaIcons.Monitor` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Monitor: ImageVector
    get() = cachedMonitor ?: ImageVector.Builder(
        name = "Monitor",
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
            moveTo(4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 20f, 17f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 15f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            close()
            moveTo(8f, 21f)
            lineTo(16f, 21f)
            moveTo(12f, 17f)
            lineTo(12f, 21f)
        }
    }.build().also { cachedMonitor = it }

/** `Monitor` vector, built on first access and reused afterwards. */
private var cachedMonitor: ImageVector? = null
