// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.keyboard.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Delete icon.
 *
 * Converted from Lucide `delete` (ISC).
 *
 * Copy of `ArecaIcons.Delete` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.keyboard.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Delete: ImageVector
    get() = cachedDelete ?: ImageVector.Builder(
        name = "Delete",
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
            moveTo(10f, 5f)
            arcTo(2f, 2f, 0f, false, false, 8.656f, 5.519f)
            lineTo(2.328f, 11.259f)
            arcTo(1f, 1f, 0f, false, false, 2.328f, 12.74f)
            lineTo(8.656f, 18.481f)
            arcTo(2f, 2f, 0f, false, false, 10f, 19f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 17f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 20f, 5f)
            close()
            moveTo(12f, 9f)
            lineTo(18f, 15f)
            moveTo(18f, 9f)
            lineTo(12f, 15f)
        }
    }.build().also { cachedDelete = it }

/** `Delete` vector, built on first access and reused afterwards. */
private var cachedDelete: ImageVector? = null
