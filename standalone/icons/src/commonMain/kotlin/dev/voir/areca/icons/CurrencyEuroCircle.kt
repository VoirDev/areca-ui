package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coin euro icon.
 *
 * Converted from Tabler Icons `coin-euro` (MIT).
 */
public val ArecaIcons.CurrencyEuroCircle: ImageVector
    get() = cachedCurrencyEuroCircle ?: ImageVector.Builder(
        name = "CurrencyEuroCircle",
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
            moveTo(14.401f, 8f)
            curveTo(13.732f, 7.372f, 12.901f, 7f, 12f, 7f)
            curveTo(9.79f, 7f, 8f, 9.239f, 8f, 12f)
            reflectiveCurveTo(9.79f, 17f, 12f, 17f)
            curveTo(12.9f, 17f, 13.731f, 16.628f, 14.4f, 16f)
            moveTo(7f, 10.5f)
            horizontalLineTo(11f)
            moveTo(7f, 13.5f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedCurrencyEuroCircle = it }

/** `CurrencyEuroCircle` vector, built on first access and reused afterwards. */
private var cachedCurrencyEuroCircle: ImageVector? = null
