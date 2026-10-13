package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coin pound icon.
 *
 * Converted from Tabler Icons `coin-pound` (MIT).
 */
public val ArecaIcons.CurrencyPoundCircle: ImageVector
    get() = cachedCurrencyPoundCircle ?: ImageVector.Builder(
        name = "CurrencyPoundCircle",
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
            moveTo(15f, 9f)
            arcTo(2f, 2f, 0f, true, false, 11f, 9f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 9f, 16f)
            horizontalLineTo(15f)
            moveTo(9f, 12f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedCurrencyPoundCircle = it }

/** `CurrencyPoundCircle` vector, built on first access and reused afterwards. */
private var cachedCurrencyPoundCircle: ImageVector? = null
