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
 * Forward icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `forward` (ISC).
 *
 * Copy of `ArecaIcons.Share` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Share: ImageVector
    get() = cachedShare ?: ImageVector.Builder(
        name = "Share",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(15f, 17f)
            lineTo(20f, 12f)
            lineTo(15f, 7f)
            moveTo(4f, 18f)
            verticalLineTo(16f)
            arcTo(4f, 4f, 0f, false, true, 8f, 12f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedShare = it }

/** `Share` vector, built on first access and reused afterwards. */
private var cachedShare: ImageVector? = null
