package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Drama icon.
 *
 * Converted from Lucide `drama` (ISC).
 */
public val ArecaIcons.Drama: ImageVector
    get() = cachedDrama ?: ImageVector.Builder(
        name = "Drama",
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
            moveTo(10f, 11f)
            horizontalLineTo(10.01f)
            moveTo(14f, 6f)
            horizontalLineTo(14.01f)
            moveTo(18f, 6f)
            horizontalLineTo(18.01f)
            moveTo(6.5f, 13.1f)
            horizontalLineTo(6.51f)
            moveTo(22f, 5f)
            curveTo(22f, 14f, 18f, 17f, 16f, 17f)
            reflectiveCurveTo(10f, 14f, 10f, 5f)
            curveTo(10f, 3f, 12f, 2f, 16f, 2f)
            reflectiveCurveTo(22f, 3f, 22f, 5f)
            moveTo(17.4f, 9.9f)
            curveTo(16.6f, 10.7f, 15.4f, 10.7f, 14.6f, 9.9f)
            moveTo(10.1f, 7.1f)
            curveTo(9f, 7.2f, 7.7f, 7.7f, 6f, 8.6f)
            curveTo(2.5f, 10.6f, 1.3f, 12.5f, 2.3f, 14.2f)
            curveTo(6.8f, 22f, 11.8f, 22.6f, 13.5f, 21.6f)
            curveTo(14.4f, 21.1f, 15.4f, 19.5f, 15.4f, 16.9f)
            moveTo(9.1f, 16.5f)
            curveTo(9.4f, 15.4f, 10.5f, 14.8f, 11.5f, 15.1f)
        }
    }.build().also { cachedDrama = it }

/** `Drama` vector, built on first access and reused afterwards. */
private var cachedDrama: ImageVector? = null
