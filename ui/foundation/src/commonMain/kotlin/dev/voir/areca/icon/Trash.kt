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
 * Trash icon.
 *
 * Converted from Lucide `trash` (ISC).
 *
 * Copy of `ArecaIcons.Trash` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Trash: ImageVector
    get() = cachedTrash ?: ImageVector.Builder(
        name = "Trash",
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
            moveTo(10f, 11f)
            verticalLineTo(17f)
            moveTo(14f, 11f)
            verticalLineTo(17f)
            moveTo(19f, 6f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 20f)
            verticalLineTo(6f)
            moveTo(3f, 6f)
            horizontalLineTo(21f)
            moveTo(8f, 6f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 10f, 2f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 4f)
            verticalLineTo(6f)
        }
    }.build().also { cachedTrash = it }

/** `Trash` vector, built on first access and reused afterwards. */
private var cachedTrash: ImageVector? = null
