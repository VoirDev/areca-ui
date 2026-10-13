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
 * Menu icon.
 *
 * Converted from Lucide `menu` (ISC).
 *
 * Copy of `ArecaIcons.Menu01` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Menu01: ImageVector
    get() = cachedMenu01 ?: ImageVector.Builder(
        name = "Menu01",
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
            moveTo(4f, 5f)
            horizontalLineTo(20f)
            moveTo(4f, 12f)
            horizontalLineTo(20f)
            moveTo(4f, 19f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedMenu01 = it }

/** `Menu01` vector, built on first access and reused afterwards. */
private var cachedMenu01: ImageVector? = null
