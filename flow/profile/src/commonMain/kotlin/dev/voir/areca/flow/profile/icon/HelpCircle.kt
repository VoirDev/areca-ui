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
 * Circle question mark icon.
 *
 * Same drawing as `ArecaIcons.CircleHelp`; both names are kept.
 *
 * Converted from Lucide `circle-question-mark` (ISC).
 *
 * Copy of `ArecaIcons.HelpCircle` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.profile.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.HelpCircle: ImageVector
    get() = cachedHelpCircle ?: ImageVector.Builder(
        name = "HelpCircle",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(9.09f, 9f)
            arcTo(3f, 3f, 0f, false, true, 14.92f, 10f)
            curveTo(14.92f, 12f, 11.92f, 13f, 11.92f, 13f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedHelpCircle = it }

/** `HelpCircle` vector, built on first access and reused afterwards. */
private var cachedHelpCircle: ImageVector? = null
