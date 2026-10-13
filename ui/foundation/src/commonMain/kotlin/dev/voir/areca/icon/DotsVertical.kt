// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ellipsis vertical icon.
 *
 * Converted from Lucide `ellipsis-vertical` (ISC).
 *
 * Copy of `ArecaIcons.DotsVertical` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.DotsVertical: ImageVector
    get() = cachedDotsVertical ?: ImageVector.Builder(
        name = "DotsVertical",
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
            moveTo(11f, 12f)
            arcTo(1f, 1f, 0f, true, true, 13f, 12f)
            arcTo(1f, 1f, 0f, true, true, 11f, 12f)
            close()
            moveTo(11f, 5f)
            arcTo(1f, 1f, 0f, true, true, 13f, 5f)
            arcTo(1f, 1f, 0f, true, true, 11f, 5f)
            close()
            moveTo(11f, 19f)
            arcTo(1f, 1f, 0f, true, true, 13f, 19f)
            arcTo(1f, 1f, 0f, true, true, 11f, 19f)
            close()
        }
    }.build().also { cachedDotsVertical = it }

/** `DotsVertical` vector, built on first access and reused afterwards. */
private var cachedDotsVertical: ImageVector? = null
