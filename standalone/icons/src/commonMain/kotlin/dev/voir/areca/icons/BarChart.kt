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
 * Converted from Tabler Icons `chart-bar` (MIT).
 */
public val ArecaIcons.BarChart: ImageVector
    get() = cachedBarChart ?: ImageVector.Builder(
        name = "BarChart",
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
            moveTo(3f, 13f)
            arcTo(1f, 1f, 0f, false, true, 4f, 12f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 9f, 13f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 8f, 20f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 19f)
            lineTo(3f, 13f)
            moveTo(15f, 9f)
            arcTo(1f, 1f, 0f, false, true, 16f, 8f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 9f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 20f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 15f, 19f)
            lineTo(15f, 9f)
            moveTo(9f, 5f)
            arcTo(1f, 1f, 0f, false, true, 10f, 4f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 15f, 5f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 14f, 20f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 9f, 19f)
            lineTo(9f, 5f)
            moveTo(4f, 20f)
            horizontalLineTo(18f)
        }
    }.build().also { cachedBarChart = it }

/** `BarChart` vector, built on first access and reused afterwards. */
private var cachedBarChart: ImageVector? = null
