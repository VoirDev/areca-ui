package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Trending up icon.
 *
 * Same drawing as `ArecaIcons.LineChartUpArrow`; both names are kept.
 *
 * Converted from Lucide `trending-up` (ISC).
 */
public val ArecaIcons.TrendUp: ImageVector
    get() = cachedTrendUp ?: ImageVector.Builder(
        name = "TrendUp",
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
            moveTo(16f, 7f)
            horizontalLineTo(22f)
            verticalLineTo(13f)
            moveTo(22f, 7f)
            lineTo(13.5f, 15.5f)
            lineTo(8.5f, 10.5f)
            lineTo(2f, 17f)
        }
    }.build().also { cachedTrendUp = it }

/** `TrendUp` vector, built on first access and reused afterwards. */
private var cachedTrendUp: ImageVector? = null
