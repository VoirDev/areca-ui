package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shopping basket icon.
 *
 * Converted from Lucide `shopping-basket` (ISC).
 */
public val ArecaIcons.ShoppingBasket: ImageVector
    get() = cachedShoppingBasket ?: ImageVector.Builder(
        name = "ShoppingBasket",
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
            moveTo(15f, 11f)
            lineTo(14f, 20f)
            moveTo(19f, 11f)
            lineTo(15f, 4f)
            moveTo(2f, 11f)
            horizontalLineTo(22f)
            moveTo(3.5f, 11f)
            lineTo(5.1f, 18.4f)
            arcTo(2f, 2f, 0f, false, false, 7.1f, 20f)
            horizontalLineTo(16.9f)
            arcTo(2f, 2f, 0f, false, false, 18.9f, 18.4f)
            lineTo(20.6f, 11f)
            moveTo(4.5f, 15.5f)
            horizontalLineTo(19.5f)
            moveTo(5f, 11f)
            lineTo(9f, 4f)
            moveTo(9f, 11f)
            lineTo(10f, 20f)
        }
    }.build().also { cachedShoppingBasket = it }

/** `ShoppingBasket` vector, built on first access and reused afterwards. */
private var cachedShoppingBasket: ImageVector? = null
