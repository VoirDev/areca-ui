package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart candlestick icon.
 *
 * Converted from Lucide `chart-candlestick` (ISC).
 */
public val ArecaIcons.CandlestickChart: ImageVector
    get() = cachedCandlestickChart ?: ImageVector.Builder(
        name = "CandlestickChart",
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
            moveTo(9f, 5f)
            verticalLineTo(9f)
            moveTo(8f, 9f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 11f, 10f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 10f, 15f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 7f, 14f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 8f, 9f)
            close()
            moveTo(9f, 15f)
            verticalLineTo(17f)
            moveTo(17f, 3f)
            verticalLineTo(5f)
            moveTo(16f, 5f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 6f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, true, 18f, 13f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 15f, 12f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 16f, 5f)
            close()
            moveTo(17f, 13f)
            verticalLineTo(16f)
            moveTo(3f, 3f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedCandlestickChart = it }

/** `CandlestickChart` vector, built on first access and reused afterwards. */
private var cachedCandlestickChart: ImageVector? = null
