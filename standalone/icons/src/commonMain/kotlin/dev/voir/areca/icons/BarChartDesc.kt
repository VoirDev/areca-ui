package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart no axes column decreasing icon.
 *
 * Converted from Lucide `chart-no-axes-column-decreasing` (ISC).
 */
public val ArecaIcons.BarChartDesc: ImageVector
    get() = cachedBarChartDesc ?: ImageVector.Builder(
        name = "BarChartDesc",
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
            verticalLineTo(3f)
            moveTo(12f, 21f)
            verticalLineTo(9f)
            moveTo(19f, 21f)
            verticalLineTo(15f)
        }
    }.build().also { cachedBarChartDesc = it }

/** `BarChartDesc` vector, built on first access and reused afterwards. */
private var cachedBarChartDesc: ImageVector? = null
