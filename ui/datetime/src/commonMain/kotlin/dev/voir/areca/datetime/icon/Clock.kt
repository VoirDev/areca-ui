// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.datetime.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clock icon.
 *
 * Converted from Lucide `clock` (ISC).
 *
 * Copy of `ArecaIcons.Clock` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.datetime.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Clock: ImageVector
    get() = cachedClock ?: ImageVector.Builder(
        name = "Clock",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(12f, 6f)
            verticalLineTo(12f)
            lineTo(16f, 14f)
        }
    }.build().also { cachedClock = it }

/** `Clock` vector, built on first access and reused afterwards. */
private var cachedClock: ImageVector? = null
