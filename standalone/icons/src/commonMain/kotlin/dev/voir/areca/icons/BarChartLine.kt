package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart column icon.
 *
 * Converted from Lucide `chart-column` (ISC).
 */
public val ArecaIcons.BarChartLine: ImageVector
    get() = cachedBarChartLine ?: ImageVector.Builder(
        name = "BarChartLine",
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
            moveTo(18f, 17f)
            verticalLineTo(9f)
            moveTo(13f, 17f)
            verticalLineTo(5f)
            moveTo(8f, 17f)
            verticalLineTo(14f)
        }
    }.build().also { cachedBarChartLine = it }

/** `BarChartLine` vector, built on first access and reused afterwards. */
private var cachedBarChartLine: ImageVector? = null
