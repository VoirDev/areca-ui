package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart column big icon.
 *
 * Same drawing as `ArecaIcons.BarChartLineAlt`; both names are kept.
 *
 * Converted from Lucide `chart-column-big` (ISC).
 */
public val ArecaIcons.BarChartSquare: ImageVector
    get() = cachedBarChartSquare ?: ImageVector.Builder(
        name = "BarChartSquare",
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
            moveTo(16f, 5f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 6f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 18f, 17f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 15f, 16f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 16f, 5f)
            close()
            moveTo(8f, 8f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 11f, 9f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 10f, 17f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 7f, 16f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 8f, 8f)
            close()
        }
    }.build().also { cachedBarChartSquare = it }

/** `BarChartSquare` vector, built on first access and reused afterwards. */
private var cachedBarChartSquare: ImageVector? = null
