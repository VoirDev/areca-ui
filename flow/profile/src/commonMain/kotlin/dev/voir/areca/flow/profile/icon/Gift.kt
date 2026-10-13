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
 * Gift icon.
 *
 * Converted from Lucide `gift` (ISC).
 *
 * Copy of `ArecaIcons.Gift` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Gift: ImageVector
    get() = cachedGift ?: ImageVector.Builder(
        name = "Gift",
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
            moveTo(12f, 7f)
            verticalLineTo(21f)
            moveTo(20f, 11f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 18f, 21f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 19f)
            verticalLineTo(11f)
            moveTo(7.5f, 7f)
            arcTo(1f, 1f, 0f, false, true, 7.5f, 2f)
            arcTo(4.8f, 8f, 0f, false, true, 12f, 7f)
            arcTo(4.8f, 8f, 0f, false, true, 16.5f, 2f)
            arcTo(1f, 1f, 0f, false, true, 16.5f, 7f)
            moveTo(4f, 7f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 8f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 20f, 11f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 10f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 4f, 7f)
            close()
        }
    }.build().also { cachedGift = it }

/** `Gift` vector, built on first access and reused afterwards. */
private var cachedGift: ImageVector? = null
