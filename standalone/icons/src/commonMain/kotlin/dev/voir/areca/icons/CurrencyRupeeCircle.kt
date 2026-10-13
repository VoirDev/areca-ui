package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coin rupee icon.
 *
 * Converted from Tabler Icons `coin-rupee` (MIT).
 */
public val ArecaIcons.CurrencyRupeeCircle: ImageVector
    get() = cachedCurrencyRupeeCircle ?: ImageVector.Builder(
        name = "CurrencyRupeeCircle",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(15f, 8f)
            horizontalLineTo(9f)
            horizontalLineTo(10f)
            arcTo(3f, 3f, 0f, false, true, 10f, 14f)
            horizontalLineTo(9f)
            lineTo(12f, 17f)
            moveTo(9f, 11f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedCurrencyRupeeCircle = it }

/** `CurrencyRupeeCircle` vector, built on first access and reused afterwards. */
private var cachedCurrencyRupeeCircle: ImageVector? = null
