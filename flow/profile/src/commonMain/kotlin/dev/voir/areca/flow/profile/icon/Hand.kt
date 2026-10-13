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
 * Hand icon.
 *
 * Converted from Lucide `hand` (ISC).
 *
 * Copy of `ArecaIcons.Hand` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Hand: ImageVector
    get() = cachedHand ?: ImageVector.Builder(
        name = "Hand",
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
            moveTo(18f, 11f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 16f, 4f)
            arcTo(2f, 2f, 0f, false, false, 14f, 6f)
            moveTo(14f, 10f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 12f, 2f)
            arcTo(2f, 2f, 0f, false, false, 10f, 4f)
            verticalLineTo(6f)
            moveTo(10f, 10.5f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 8f, 4f)
            arcTo(2f, 2f, 0f, false, false, 6f, 6f)
            verticalLineTo(14f)
            moveTo(18f, 8f)
            arcTo(2f, 2f, 0f, true, true, 22f, 8f)
            verticalLineTo(14f)
            arcTo(8f, 8f, 0f, false, true, 14f, 22f)
            horizontalLineTo(12f)
            curveTo(9.2f, 22f, 7.5f, 21.14f, 6.01f, 19.66f)
            lineTo(2.41f, 16.06f)
            arcTo(2f, 2f, 0f, false, true, 5.24f, 13.24f)
            lineTo(7f, 15f)
        }
    }.build().also { cachedHand = it }

/** `Hand` vector, built on first access and reused afterwards. */
private var cachedHand: ImageVector? = null
