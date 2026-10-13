package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Egg fried icon.
 *
 * Converted from Lucide `egg-fried` (ISC).
 */
public val ArecaIcons.EggFried: ImageVector
    get() = cachedEggFried ?: ImageVector.Builder(
        name = "EggFried",
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
            moveTo(8f, 12.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 15f, 12.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 8f, 12.5f)
            close()
            moveTo(3f, 8f)
            curveTo(3f, 4.5f, 5.5f, 2f, 9.5f, 2f)
            curveTo(14.5f, 2f, 14.33f, 5f, 17f, 7f)
            reflectiveCurveTo(22f, 9f, 22f, 13f)
            curveTo(22f, 17.5f, 19.5f, 19.5f, 15f, 19.5f)
            curveTo(12.5f, 19.5f, 12.5f, 22f, 9f, 22f)
            reflectiveCurveTo(2f, 20f, 2f, 16.5f)
            curveTo(2f, 13.5f, 3.5f, 13.5f, 3.5f, 11.5f)
            curveTo(3.5f, 10f, 3f, 9f, 3f, 8f)
            close()
        }
    }.build().also { cachedEggFried = it }

/** `EggFried` vector, built on first access and reused afterwards. */
private var cachedEggFried: ImageVector? = null
