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
 * Heart icon.
 *
 * Converted from Lucide `heart` (ISC).
 *
 * Copy of `ArecaIcons.Heart` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Heart: ImageVector
    get() = cachedHeart ?: ImageVector.Builder(
        name = "Heart",
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
            moveTo(2f, 9.5f)
            arcTo(5.5f, 5.5f, 0f, false, true, 11.591f, 5.824f)
            arcTo(0.56f, 0.56f, 0f, false, false, 12.409f, 5.824f)
            arcTo(5.49f, 5.49f, 0f, false, true, 22f, 9.5f)
            curveTo(22f, 11.79f, 20.5f, 13.5f, 19f, 15f)
            lineTo(13.508f, 20.313f)
            arcTo(2f, 2f, 0f, false, true, 10.508f, 20.332f)
            lineTo(5f, 15f)
            curveTo(3.5f, 13.5f, 2f, 11.8f, 2f, 9.5f)
        }
    }.build().also { cachedHeart = it }

/** `Heart` vector, built on first access and reused afterwards. */
private var cachedHeart: ImageVector? = null
