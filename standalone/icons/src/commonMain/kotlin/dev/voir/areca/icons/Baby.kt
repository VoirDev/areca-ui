package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Baby icon.
 *
 * Converted from Lucide `baby` (ISC).
 */
public val ArecaIcons.Baby: ImageVector
    get() = cachedBaby ?: ImageVector.Builder(
        name = "Baby",
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
            moveTo(10f, 16f)
            curveTo(10.5f, 16.3f, 11.2f, 16.5f, 12f, 16.5f)
            reflectiveCurveTo(13.5f, 16.3f, 14f, 16f)
            moveTo(15f, 12f)
            horizontalLineTo(15.01f)
            moveTo(19.38f, 6.813f)
            arcTo(9f, 9f, 0f, false, true, 20.8f, 10.2f)
            arcTo(2f, 2f, 0f, false, true, 20.8f, 13.8f)
            arcTo(9f, 9f, 0f, false, true, 3.2f, 13.8f)
            arcTo(2f, 2f, 0f, false, true, 3.2f, 10.2f)
            arcTo(9f, 9f, 0f, false, true, 12f, 3f)
            curveTo(14f, 3f, 15.5f, 4.1f, 15.5f, 5.5f)
            reflectiveCurveTo(14.6f, 8f, 13.5f, 8f)
            curveTo(12.7f, 8f, 12f, 7.6f, 12f, 7f)
            moveTo(9f, 12f)
            horizontalLineTo(9.01f)
        }
    }.build().also { cachedBaby = it }

/** `Baby` vector, built on first access and reused afterwards. */
private var cachedBaby: ImageVector? = null
