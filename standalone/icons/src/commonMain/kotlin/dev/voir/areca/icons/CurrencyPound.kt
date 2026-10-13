package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency pound icon.
 *
 * Converted from Tabler Icons `currency-pound` (MIT).
 */
public val ArecaIcons.CurrencyPound: ImageVector
    get() = cachedCurrencyPound ?: ImageVector.Builder(
        name = "CurrencyPound",
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
            moveTo(17f, 18.5f)
            arcTo(6f, 6f, 0f, false, true, 12f, 18.5f)
            arcTo(6f, 6f, 0f, false, false, 7f, 19f)
            arcTo(3f, 3f, 0f, false, false, 9f, 16.5f)
            verticalLineTo(9f)
            arcTo(4f, 4f, 0f, false, true, 16.45f, 7f)
            moveTo(13.9f, 13f)
            horizontalLineTo(6.9f)
        }
    }.build().also { cachedCurrencyPound = it }

/** `CurrencyPound` vector, built on first access and reused afterwards. */
private var cachedCurrencyPound: ImageVector? = null
