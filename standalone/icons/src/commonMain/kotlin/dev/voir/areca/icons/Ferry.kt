package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ferry icon.
 *
 * Converted from Tabler Icons `ferry` (MIT).
 */
public val ArecaIcons.Ferry: ImageVector
    get() = cachedFerry ?: ImageVector.Builder(
        name = "Ferry",
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
            moveTo(2f, 18f)
            horizontalLineTo(17.293f)
            curveTo(18.313f, 18f, 19.265f, 17.497f, 19.829f, 16.66f)
            lineTo(22f, 13f)
            horizontalLineTo(3.521f)
            lineTo(2f, 18f)
            moveTo(14f, 8f)
            lineTo(13f, 6f)
            moveTo(6.107f, 12.675f)
            lineTo(7.491f, 8f)
            horizontalLineTo(15.491f)
            lineTo(18.166f, 12.598f)
        }
    }.build().also { cachedFerry = it }

/** `Ferry` vector, built on first access and reused afterwards. */
private var cachedFerry: ImageVector? = null
