package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square play icon.
 *
 * Converted from Lucide `square-play` (ISC).
 */
public val ArecaIcons.PlaySquare: ImageVector
    get() = cachedPlaySquare ?: ImageVector.Builder(
        name = "PlaySquare",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(9f, 9.003f)
            arcTo(1f, 1f, 0f, false, true, 10.517f, 8.144f)
            lineTo(15.514f, 11.141f)
            arcTo(1f, 1f, 0f, false, true, 15.514f, 12.859f)
            lineTo(10.517f, 15.856f)
            arcTo(1f, 1f, 0f, false, true, 9f, 14.996f)
            close()
        }
    }.build().also { cachedPlaySquare = it }

/** `PlaySquare` vector, built on first access and reused afterwards. */
private var cachedPlaySquare: ImageVector? = null
