package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency euro icon.
 *
 * Converted from Tabler Icons `currency-euro` (MIT).
 */
public val ArecaIcons.CurrencyEuro: ImageVector
    get() = cachedCurrencyEuro ?: ImageVector.Builder(
        name = "CurrencyEuro",
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
            moveTo(17.2f, 7f)
            arcTo(6f, 7f, 0f, true, false, 17.2f, 17f)
            moveTo(13f, 10f)
            horizontalLineTo(5f)
            moveTo(5f, 14f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedCurrencyEuro = it }

/** `CurrencyEuro` vector, built on first access and reused afterwards. */
private var cachedCurrencyEuro: ImageVector? = null
