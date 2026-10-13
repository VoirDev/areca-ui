package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency bitcoin icon.
 *
 * Converted from Tabler Icons `currency-bitcoin` (MIT).
 */
public val ArecaIcons.CurrencyBitcoin: ImageVector
    get() = cachedCurrencyBitcoin ?: ImageVector.Builder(
        name = "CurrencyBitcoin",
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
            moveTo(6f, 6f)
            horizontalLineTo(14f)
            arcTo(3f, 3f, 0f, false, true, 14f, 12f)
            arcTo(3f, 3f, 0f, false, true, 14f, 18f)
            horizontalLineTo(6f)
            moveTo(8f, 6f)
            lineTo(8f, 18f)
            moveTo(8f, 12f)
            lineTo(14f, 12f)
            moveTo(9f, 3f)
            lineTo(9f, 6f)
            moveTo(13f, 3f)
            lineTo(13f, 6f)
            moveTo(9f, 18f)
            lineTo(9f, 21f)
            moveTo(13f, 18f)
            lineTo(13f, 21f)
        }
    }.build().also { cachedCurrencyBitcoin = it }

/** `CurrencyBitcoin` vector, built on first access and reused afterwards. */
private var cachedCurrencyBitcoin: ImageVector? = null
