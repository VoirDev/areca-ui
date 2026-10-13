package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart pie icon.
 *
 * Converted from Lucide `chart-pie` (ISC).
 */
public val ArecaIcons.PieChart: ImageVector
    get() = cachedPieChart ?: ImageVector.Builder(
        name = "PieChart",
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
            moveTo(21f, 12f)
            curveTo(21.552f, 12f, 22.005f, 11.551f, 21.95f, 11.002f)
            arcTo(10f, 10f, 0f, false, false, 12.997f, 2.051f)
            curveTo(12.447f, 1.996f, 11.999f, 2.449f, 11.999f, 3.001f)
            verticalLineTo(11.001f)
            arcTo(1f, 1f, 0f, false, false, 12.999f, 12.001f)
            close()
            moveTo(21.21f, 15.89f)
            arcTo(10f, 10f, 0f, true, true, 8f, 2.83f)
        }
    }.build().also { cachedPieChart = it }

/** `PieChart` vector, built on first access and reused afterwards. */
private var cachedPieChart: ImageVector? = null
