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
 * Eye off icon.
 *
 * Same drawing as `ArecaIcons.EyeOff`; both names are kept.
 *
 * Converted from Lucide `eye-off` (ISC).
 *
 * Copy of `ArecaIcons.EyeHide` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.EyeHide: ImageVector
    get() = cachedEyeHide ?: ImageVector.Builder(
        name = "EyeHide",
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
            moveTo(10.733f, 5.076f)
            arcTo(10.744f, 10.744f, 0f, false, true, 21.938f, 11.651f)
            arcTo(1f, 1f, 0f, false, true, 21.938f, 12.347f)
            arcTo(10.747f, 10.747f, 0f, false, true, 20.494f, 14.837f)
            moveTo(14.084f, 14.158f)
            arcTo(3f, 3f, 0f, false, true, 9.842f, 9.916f)
            moveTo(17.479f, 17.499f)
            arcTo(10.75f, 10.75f, 0f, false, true, 2.062f, 12.348f)
            arcTo(1f, 1f, 0f, false, true, 2.062f, 11.652f)
            arcTo(10.75f, 10.75f, 0f, false, true, 6.508f, 6.509f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedEyeHide = it }

/** `EyeHide` vector, built on first access and reused afterwards. */
private var cachedEyeHide: ImageVector? = null
