package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrows exchange icon.
 *
 * Converted from Tabler Icons `arrows-exchange` (MIT).
 */
public val ArecaIcons.Exchange: ImageVector
    get() = cachedExchange ?: ImageVector.Builder(
        name = "Exchange",
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
            moveTo(7f, 10f)
            horizontalLineTo(21f)
            lineTo(17f, 6f)
            moveTo(17f, 14f)
            horizontalLineTo(3f)
            lineTo(7f, 18f)
        }
    }.build().also { cachedExchange = it }

/** `Exchange` vector, built on first access and reused afterwards. */
private var cachedExchange: ImageVector? = null
