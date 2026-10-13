package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency rupee icon.
 *
 * Converted from Tabler Icons `currency-rupee` (MIT).
 */
public val ArecaIcons.CurrencyRupee: ImageVector
    get() = cachedCurrencyRupee ?: ImageVector.Builder(
        name = "CurrencyRupee",
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
            moveTo(18f, 5f)
            horizontalLineTo(7f)
            horizontalLineTo(10f)
            arcTo(4f, 4f, 0f, false, true, 10f, 13f)
            horizontalLineTo(7f)
            lineTo(13f, 19f)
            moveTo(7f, 9f)
            lineTo(18f, 9f)
        }
    }.build().also { cachedCurrencyRupee = it }

/** `CurrencyRupee` vector, built on first access and reused afterwards. */
private var cachedCurrencyRupee: ImageVector? = null
