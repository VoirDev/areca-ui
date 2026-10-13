package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Currency dollar icon.
 *
 * Converted from Tabler Icons `currency-dollar` (MIT).
 */
public val ArecaIcons.CurrencyDollar: ImageVector
    get() = cachedCurrencyDollar ?: ImageVector.Builder(
        name = "CurrencyDollar",
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
            moveTo(16.7f, 8f)
            arcTo(3f, 3f, 0f, false, false, 14f, 6f)
            horizontalLineTo(10f)
            arcTo(3f, 3f, 0f, false, false, 10f, 12f)
            horizontalLineTo(14f)
            arcTo(3f, 3f, 0f, false, true, 14f, 18f)
            horizontalLineTo(10f)
            arcTo(3f, 3f, 0f, false, true, 7.3f, 16f)
            moveTo(12f, 3f)
            verticalLineTo(6f)
            moveTo(12f, 18f)
            verticalLineTo(21f)
        }
    }.build().also { cachedCurrencyDollar = it }

/** `CurrencyDollar` vector, built on first access and reused afterwards. */
private var cachedCurrencyDollar: ImageVector? = null
