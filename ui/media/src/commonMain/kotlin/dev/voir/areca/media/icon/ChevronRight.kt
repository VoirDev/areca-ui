// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.media.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevron right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `chevron-right` (ISC).
 *
 * Copy of `ArecaIcons.ChevronRight` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.media.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.ChevronRight: ImageVector
    get() = cachedChevronRight ?: ImageVector.Builder(
        name = "ChevronRight",
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
            moveTo(9f, 18f)
            lineTo(15f, 12f)
            lineTo(9f, 6f)
        }
    }.build().also { cachedChevronRight = it }

/** `ChevronRight` vector, built on first access and reused afterwards. */
private var cachedChevronRight: ImageVector? = null
