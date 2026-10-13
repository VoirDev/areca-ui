package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shopping cart dollar icon.
 *
 * Converted from Tabler Icons `shopping-cart-dollar` (MIT).
 */
public val ArecaIcons.ShoppingCartDollar: ImageVector
    get() = cachedShoppingCartDollar ?: ImageVector.Builder(
        name = "ShoppingCartDollar",
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
            moveTo(4f, 19f)
            arcTo(2f, 2f, 0f, true, false, 8f, 19f)
            arcTo(2f, 2f, 0f, false, false, 4f, 19f)
            moveTo(13f, 17f)
            horizontalLineTo(6f)
            verticalLineTo(3f)
            horizontalLineTo(4f)
            moveTo(6f, 5f)
            lineTo(20f, 6f)
            lineTo(19.425f, 10.022f)
            moveTo(14.5f, 13f)
            horizontalLineTo(6f)
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 18f)
            horizontalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
        }
    }.build().also { cachedShoppingCartDollar = it }

/** `ShoppingCartDollar` vector, built on first access and reused afterwards. */
private var cachedShoppingCartDollar: ImageVector? = null
