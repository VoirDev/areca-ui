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
 * Check icon.
 *
 * Converted from Lucide `check` (ISC).
 *
 * Copy of `ArecaIcons.Check` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Check: ImageVector
    get() = cachedCheck ?: ImageVector.Builder(
        name = "Check",
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
            moveTo(20f, 6f)
            lineTo(9f, 17f)
            lineTo(4f, 12f)
        }
    }.build().also { cachedCheck = it }

/** `Check` vector, built on first access and reused afterwards. */
private var cachedCheck: ImageVector? = null
