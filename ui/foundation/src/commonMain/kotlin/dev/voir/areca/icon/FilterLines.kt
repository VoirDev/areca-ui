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
 * List filter icon.
 *
 * Converted from Lucide `list-filter` (ISC).
 *
 * Copy of `ArecaIcons.FilterLines` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.FilterLines: ImageVector
    get() = cachedFilterLines ?: ImageVector.Builder(
        name = "FilterLines",
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
            moveTo(2f, 5f)
            horizontalLineTo(22f)
            moveTo(6f, 12f)
            horizontalLineTo(18f)
            moveTo(9f, 19f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedFilterLines = it }

/** `FilterLines` vector, built on first access and reused afterwards. */
private var cachedFilterLines: ImageVector? = null
