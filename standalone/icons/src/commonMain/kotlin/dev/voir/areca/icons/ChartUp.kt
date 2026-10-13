package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart line icon.
 *
 * Converted from Lucide `chart-line` (ISC).
 */
public val ArecaIcons.ChartUp: ImageVector
    get() = cachedChartUp ?: ImageVector.Builder(
        name = "ChartUp",
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
            moveTo(3f, 3f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(21f)
            moveTo(19f, 9f)
            lineTo(14f, 14f)
            lineTo(10f, 10f)
            lineTo(7f, 13f)
        }
    }.build().also { cachedChartUp = it }

/** `ChartUp` vector, built on first access and reused afterwards. */
private var cachedChartUp: ImageVector? = null
