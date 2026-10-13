package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart bar icon.
 *
 * Converted from Lucide `chart-bar` (ISC).
 */
public val ArecaIcons.HorizontalBarChart: ImageVector
    get() = cachedHorizontalBarChart ?: ImageVector.Builder(
        name = "HorizontalBarChart",
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
            moveTo(7f, 16f)
            horizontalLineTo(15f)
            moveTo(7f, 11f)
            horizontalLineTo(19f)
            moveTo(7f, 6f)
            horizontalLineTo(10f)
        }
    }.build().also { cachedHorizontalBarChart = it }

/** `HorizontalBarChart` vector, built on first access and reused afterwards. */
private var cachedHorizontalBarChart: ImageVector? = null
