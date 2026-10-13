package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coin icon.
 *
 * Same drawing as `ArecaIcons.Coin`; both names are kept.
 *
 * Converted from Tabler Icons `coin` (MIT).
 */
public val ArecaIcons.CurrencyDollarCircle: ImageVector
    get() = cachedCurrencyDollarCircle ?: ImageVector.Builder(
        name = "CurrencyDollarCircle",
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
            moveTo(14.8f, 9f)
            arcTo(2f, 2f, 0f, false, false, 13f, 8f)
            horizontalLineTo(11f)
            arcTo(2f, 2f, 0f, true, false, 11f, 12f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, true, true, 13f, 16f)
            horizontalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 9.2f, 15f)
            moveTo(12f, 7f)
            verticalLineTo(17f)
        }
    }.build().also { cachedCurrencyDollarCircle = it }

/** `CurrencyDollarCircle` vector, built on first access and reused afterwards. */
private var cachedCurrencyDollarCircle: ImageVector? = null
