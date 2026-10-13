// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.flow.attachments.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * External link icon.
 *
 * Converted from Lucide `external-link` (ISC).
 *
 * Copy of `ArecaIcons.LinkExternal` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.attachments.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.LinkExternal: ImageVector
    get() = cachedLinkExternal ?: ImageVector.Builder(
        name = "LinkExternal",
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
            moveTo(15f, 3f)
            horizontalLineTo(21f)
            verticalLineTo(9f)
            moveTo(10f, 14f)
            lineTo(21f, 3f)
            moveTo(18f, 13f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 16f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 5f, 6f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedLinkExternal = it }

/** `LinkExternal` vector, built on first access and reused afterwards. */
private var cachedLinkExternal: ImageVector? = null
