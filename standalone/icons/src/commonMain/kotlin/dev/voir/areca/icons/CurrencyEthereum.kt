package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency ethereum icon.
 *
 * Converted from Tabler Icons `currency-ethereum` (MIT).
 */
public val ArecaIcons.CurrencyEthereum: ImageVector
    get() = cachedCurrencyEthereum ?: ImageVector.Builder(
        name = "CurrencyEthereum",
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
            moveTo(6f, 12f)
            lineTo(12f, 3f)
            lineTo(18f, 12f)
            lineTo(12f, 21f)
            lineTo(6f, 12f)
            moveTo(6f, 12f)
            lineTo(12f, 9f)
            lineTo(18f, 12f)
            lineTo(12f, 14f)
            lineTo(6f, 12f)
        }
    }.build().also { cachedCurrencyEthereum = it }

/** `CurrencyEthereum` vector, built on first access and reused afterwards. */
private var cachedCurrencyEthereum: ImageVector? = null
