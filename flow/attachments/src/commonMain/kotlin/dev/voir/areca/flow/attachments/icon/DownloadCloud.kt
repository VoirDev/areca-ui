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
 * Cloud download icon.
 *
 * Converted from Lucide `cloud-download` (ISC).
 *
 * Copy of `ArecaIcons.DownloadCloud` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.attachments.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.DownloadCloud: ImageVector
    get() = cachedDownloadCloud ?: ImageVector.Builder(
        name = "DownloadCloud",
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
            moveTo(12f, 13f)
            verticalLineTo(21f)
            lineTo(8f, 17f)
            moveTo(12f, 21f)
            lineTo(16f, 17f)
            moveTo(4.393f, 15.269f)
            arcTo(7f, 7f, 0f, true, true, 15.71f, 8f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, false, true, 19.936f, 16.284f)
        }
    }.build().also { cachedDownloadCloud = it }

/** `DownloadCloud` vector, built on first access and reused afterwards. */
private var cachedDownloadCloud: ImageVector? = null
