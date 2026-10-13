package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency yen icon.
 *
 * Converted from Tabler Icons `currency-yen` (MIT).
 */
public val ArecaIcons.CurrencyYen: ImageVector
    get() = cachedCurrencyYen ?: ImageVector.Builder(
        name = "CurrencyYen",
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
            moveTo(12f, 19f)
            verticalLineTo(12f)
            lineTo(7f, 5f)
            moveTo(17f, 5f)
            lineTo(12f, 12f)
            moveTo(8f, 17f)
            lineTo(16f, 17f)
            moveTo(8f, 13f)
            lineTo(16f, 13f)
        }
    }.build().also { cachedCurrencyYen = it }

/** `CurrencyYen` vector, built on first access and reused afterwards. */
private var cachedCurrencyYen: ImageVector? = null
