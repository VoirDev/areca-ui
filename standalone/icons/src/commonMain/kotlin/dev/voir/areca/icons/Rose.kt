package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rose icon.
 *
 * Converted from Lucide `rose` (ISC).
 */
public val ArecaIcons.Rose: ImageVector
    get() = cachedRose ?: ImageVector.Builder(
        name = "Rose",
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
            moveTo(17f, 10f)
            horizontalLineTo(16f)
            arcTo(4f, 4f, 0f, true, true, 20f, 6f)
            verticalLineTo(6.534f)
            moveTo(17f, 6f)
            horizontalLineTo(18f)
            arcTo(4f, 4f, 0f, false, true, 19.42f, 13.74f)
            lineTo(17.13f, 14.61f)
            arcTo(6f, 6f, 0f, false, true, 11.791f, 3.93f)
            lineTo(13.86f, 2.62f)
            moveTo(4.5f, 17f)
            curveTo(7.3f, 16.5f, 8.9f, 17f, 10f, 17.8f)
            reflectiveCurveTo(11.8f, 20f, 12.3f, 21.5f)
            curveTo(10.3f, 21.9f, 8.8f, 21.9f, 7.5f, 21.2f)
            curveTo(6.3f, 20.6f, 5.2f, 19.3f, 4.5f, 17f)
            moveTo(9.77f, 12f)
            curveTo(4f, 15f, 2f, 22f, 2f, 22f)
            moveTo(15f, 8f)
            arcTo(2f, 2f, 0f, true, true, 19f, 8f)
            arcTo(2f, 2f, 0f, true, true, 15f, 8f)
            close()
        }
    }.build().also { cachedRose = it }

/** `Rose` vector, built on first access and reused afterwards. */
private var cachedRose: ImageVector? = null
