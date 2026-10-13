package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart no axes column increasing icon.
 *
 * Converted from Lucide `chart-no-axes-column-increasing` (ISC).
 */
public val ArecaIcons.BarChartAsc: ImageVector
    get() = cachedBarChartAsc ?: ImageVector.Builder(
        name = "BarChartAsc",
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
            moveTo(5f, 21f)
            verticalLineTo(15f)
            moveTo(12f, 21f)
            verticalLineTo(9f)
            moveTo(19f, 21f)
            verticalLineTo(3f)
        }
    }.build().also { cachedBarChartAsc = it }

/** `BarChartAsc` vector, built on first access and reused afterwards. */
private var cachedBarChartAsc: ImageVector? = null
