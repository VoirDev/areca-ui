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
 * Search icon.
 *
 * Converted from Lucide `search` (ISC).
 *
 * Copy of `ArecaIcons.Search` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Search: ImageVector
    get() = cachedSearch ?: ImageVector.Builder(
        name = "Search",
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
            moveTo(21f, 21f)
            lineTo(16.66f, 16.66f)
            moveTo(3f, 11f)
            arcTo(8f, 8f, 0f, true, true, 19f, 11f)
            arcTo(8f, 8f, 0f, true, true, 3f, 11f)
            close()
        }
    }.build().also { cachedSearch = it }

/** `Search` vector, built on first access and reused afterwards. */
private var cachedSearch: ImageVector? = null
