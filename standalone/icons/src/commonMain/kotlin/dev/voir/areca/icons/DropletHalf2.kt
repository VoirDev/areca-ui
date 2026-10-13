package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Droplet half 2 icon.
 *
 * Converted from Tabler Icons `droplet-half-2` (MIT).
 */
public val ArecaIcons.DropletHalf2: ImageVector
    get() = cachedDropletHalf2 ?: ImageVector.Builder(
        name = "DropletHalf2",
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
            moveTo(7.502f, 19.423f)
            curveTo(10.104f, 21.528f, 13.897f, 21.528f, 16.498f, 19.423f)
            curveTo(19.1f, 17.318f, 19.76f, 13.715f, 18.064f, 10.877f)
            lineTo(13.174f, 3.617f)
            curveTo(12.754f, 2.992f, 11.887f, 2.814f, 11.238f, 3.22f)
            arcTo(1.376f, 1.376f, 0f, false, false, 10.828f, 3.617f)
            lineTo(5.935f, 10.877f)
            curveTo(4.24f, 13.715f, 4.9f, 17.318f, 7.502f, 19.423f)
            moveTo(5f, 14f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedDropletHalf2 = it }

/** `DropletHalf2` vector, built on first access and reused afterwards. */
private var cachedDropletHalf2: ImageVector? = null
