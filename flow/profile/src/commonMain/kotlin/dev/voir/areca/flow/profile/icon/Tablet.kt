// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.flow.profile.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tablet icon.
 *
 * Converted from Lucide `tablet` (ISC).
 *
 * Copy of `ArecaIcons.Tablet` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Tablet: ImageVector
    get() = cachedTablet ?: ImageVector.Builder(
        name = "Tablet",
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
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
            moveTo(12f, 18f)
            lineTo(12.01f, 18f)
        }
    }.build().also { cachedTablet = it }

/** `Tablet` vector, built on first access and reused afterwards. */
private var cachedTablet: ImageVector? = null
