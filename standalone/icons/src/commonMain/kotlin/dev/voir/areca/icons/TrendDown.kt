package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Trending down icon.
 *
 * Same drawing as `ArecaIcons.LineChartDown`; both names are kept.
 *
 * Converted from Lucide `trending-down` (ISC).
 */
public val ArecaIcons.TrendDown: ImageVector
    get() = cachedTrendDown ?: ImageVector.Builder(
        name = "TrendDown",
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
            moveTo(16f, 17f)
            horizontalLineTo(22f)
            verticalLineTo(11f)
            moveTo(22f, 17f)
            lineTo(13.5f, 8.5f)
            lineTo(8.5f, 13.5f)
            lineTo(2f, 7f)
        }
    }.build().also { cachedTrendDown = it }

/** `TrendDown` vector, built on first access and reused afterwards. */
private var cachedTrendDown: ImageVector? = null
