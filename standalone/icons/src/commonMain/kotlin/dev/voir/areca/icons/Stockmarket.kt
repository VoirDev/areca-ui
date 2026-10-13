package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chart no axes combined icon.
 *
 * Same drawing as `ArecaIcons.BarLineChart`; both names are kept.
 *
 * Converted from Lucide `chart-no-axes-combined` (ISC).
 */
public val ArecaIcons.Stockmarket: ImageVector
    get() = cachedStockmarket ?: ImageVector.Builder(
        name = "Stockmarket",
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
            moveTo(12f, 16f)
            verticalLineTo(21f)
            moveTo(16f, 14.639f)
            verticalLineTo(21f)
            moveTo(20f, 10.656f)
            verticalLineTo(21f)
            moveTo(22f, 3f)
            lineTo(13.354f, 11.646f)
            arcTo(0.5f, 0.5f, 0f, false, true, 12.646f, 11.646f)
            lineTo(9.354f, 8.354f)
            arcTo(0.5f, 0.5f, 0f, false, false, 8.647f, 8.354f)
            lineTo(2f, 15f)
            moveTo(4f, 18.463f)
            verticalLineTo(21f)
            moveTo(8f, 14.656f)
            verticalLineTo(21f)
        }
    }.build().also { cachedStockmarket = it }

/** `Stockmarket` vector, built on first access and reused afterwards. */
private var cachedStockmarket: ImageVector? = null
