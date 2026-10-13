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
 * Chevron down icon.
 *
 * Converted from Lucide `chevron-down` (ISC).
 *
 * Copy of `ArecaIcons.ChevronDown` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.keyboard.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.ChevronDown: ImageVector
    get() = cachedChevronDown ?: ImageVector.Builder(
        name = "ChevronDown",
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
            moveTo(6f, 9f)
            lineTo(12f, 15f)
            lineTo(18f, 9f)
        }
    }.build().also { cachedChevronDown = it }

/** `ChevronDown` vector, built on first access and reused afterwards. */
private var cachedChevronDown: ImageVector? = null
