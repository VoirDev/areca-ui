package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle play icon.
 *
 * Converted from Lucide `circle-play` (ISC).
 */
public val ArecaIcons.PlayCircle: ImageVector
    get() = cachedPlayCircle ?: ImageVector.Builder(
        name = "PlayCircle",
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
            moveTo(9f, 9.003f)
            arcTo(1f, 1f, 0f, false, true, 10.517f, 8.144f)
            lineTo(15.514f, 11.141f)
            arcTo(1f, 1f, 0f, false, true, 15.514f, 12.859f)
            lineTo(10.517f, 15.856f)
            arcTo(1f, 1f, 0f, false, true, 9f, 14.996f)
            close()
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
        }
    }.build().also { cachedPlayCircle = it }

/** `PlayCircle` vector, built on first access and reused afterwards. */
private var cachedPlayCircle: ImageVector? = null
