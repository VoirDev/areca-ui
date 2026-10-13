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
 * Settings icon.
 *
 * Converted from Lucide `settings` (ISC).
 *
 * Copy of `ArecaIcons.Settings` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.Settings: ImageVector
    get() = cachedSettings ?: ImageVector.Builder(
        name = "Settings",
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
            moveTo(9.671f, 4.136f)
            arcTo(2.34f, 2.34f, 0f, false, true, 14.33f, 4.136f)
            arcTo(2.34f, 2.34f, 0f, false, false, 17.649f, 6.051f)
            arcTo(2.34f, 2.34f, 0f, false, true, 19.979f, 10.084f)
            arcTo(2.34f, 2.34f, 0f, false, false, 19.979f, 13.915f)
            arcTo(2.34f, 2.34f, 0f, false, true, 17.649f, 17.948f)
            arcTo(2.34f, 2.34f, 0f, false, false, 14.33f, 19.863f)
            arcTo(2.34f, 2.34f, 0f, false, true, 9.671f, 19.863f)
            arcTo(2.34f, 2.34f, 0f, false, false, 6.351f, 17.948f)
            arcTo(2.34f, 2.34f, 0f, false, true, 4.021f, 13.915f)
            arcTo(2.34f, 2.34f, 0f, false, false, 4.021f, 10.084f)
            arcTo(2.34f, 2.34f, 0f, false, true, 6.35f, 6.051f)
            arcTo(2.34f, 2.34f, 0f, false, false, 9.669f, 4.136f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
        }
    }.build().also { cachedSettings = it }

/** `Settings` vector, built on first access and reused afterwards. */
private var cachedSettings: ImageVector? = null
