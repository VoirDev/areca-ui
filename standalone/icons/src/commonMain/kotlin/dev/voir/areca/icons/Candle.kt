package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Candle icon.
 *
 * Converted from Tabler Icons `candle` (MIT).
 */
public val ArecaIcons.Candle: ImageVector
    get() = cachedCandle ?: ImageVector.Builder(
        name = "Candle",
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
            moveTo(9f, 21f)
            horizontalLineTo(15f)
            verticalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 14f, 10f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 9f, 11f)
            lineTo(9f, 21f)
            moveTo(12f, 2f)
            lineTo(13.465f, 3.638f)
            arcTo(2f, 2f, 0f, true, true, 10.45f, 3.737f)
            lineTo(12f, 2f)
        }
    }.build().also { cachedCandle = it }

/** `Candle` vector, built on first access and reused afterwards. */
private var cachedCandle: ImageVector? = null
