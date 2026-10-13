package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coins icon.
 *
 * Converted from Lucide `coins` (ISC).
 */
public val ArecaIcons.Coins: ImageVector
    get() = cachedCoins ?: ImageVector.Builder(
        name = "Coins",
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
            moveTo(13.744f, 17.736f)
            arcTo(6f, 6f, 0f, true, true, 6.264f, 10.256f)
            moveTo(15f, 6f)
            horizontalLineTo(16f)
            verticalLineTo(10f)
            moveTo(6.134f, 14.768f)
            lineTo(7f, 14.268f)
            lineTo(9f, 17.732f)
            moveTo(10f, 8f)
            arcTo(6f, 6f, 0f, true, true, 22f, 8f)
            arcTo(6f, 6f, 0f, true, true, 10f, 8f)
            close()
        }
    }.build().also { cachedCoins = it }

/** `Coins` vector, built on first access and reused afterwards. */
private var cachedCoins: ImageVector? = null
