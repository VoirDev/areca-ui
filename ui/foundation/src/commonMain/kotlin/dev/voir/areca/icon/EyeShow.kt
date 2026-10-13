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
 * Eye icon.
 *
 * Same drawing as `ArecaIcons.Eye`; both names are kept.
 *
 * Converted from Lucide `eye` (ISC).
 *
 * Copy of `ArecaIcons.EyeShow` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.EyeShow: ImageVector
    get() = cachedEyeShow ?: ImageVector.Builder(
        name = "EyeShow",
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
            moveTo(2.062f, 12.348f)
            arcTo(1f, 1f, 0f, false, true, 2.062f, 11.652f)
            arcTo(10.75f, 10.75f, 0f, false, true, 21.938f, 11.652f)
            arcTo(1f, 1f, 0f, false, true, 21.938f, 12.348f)
            arcTo(10.75f, 10.75f, 0f, false, true, 2.062f, 12.348f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
        }
    }.build().also { cachedEyeShow = it }

/** `EyeShow` vector, built on first access and reused afterwards. */
private var cachedEyeShow: ImageVector? = null
