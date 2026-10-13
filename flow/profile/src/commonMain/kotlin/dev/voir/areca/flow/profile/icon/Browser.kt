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
 * App window icon.
 *
 * Same drawing as `ArecaIcons.AppWindow`; both names are kept.
 *
 * Converted from Lucide `app-window` (ISC).
 *
 * Copy of `ArecaIcons.Browser` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Browser: ImageVector
    get() = cachedBrowser ?: ImageVector.Builder(
        name = "Browser",
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
            moveTo(4f, 4f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 4f)
            close()
            moveTo(10f, 4f)
            verticalLineTo(8f)
            moveTo(2f, 8f)
            horizontalLineTo(22f)
            moveTo(6f, 4f)
            verticalLineTo(8f)
        }
    }.build().also { cachedBrowser = it }

/** `Browser` vector, built on first access and reused afterwards. */
private var cachedBrowser: ImageVector? = null
