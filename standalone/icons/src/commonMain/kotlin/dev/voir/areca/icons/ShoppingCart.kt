package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shopping cart icon.
 *
 * Converted from Lucide `shopping-cart` (ISC).
 */
public val ArecaIcons.ShoppingCart: ImageVector
    get() = cachedShoppingCart ?: ImageVector.Builder(
        name = "ShoppingCart",
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
            moveTo(2.05f, 2.05f)
            lineTo(3.149f, 2.022f)
            arcTo(1f, 1f, 0f, false, true, 4.157f, 2.837f)
            lineTo(6.847f, 17.184f)
            arcTo(1f, 1f, 0f, false, false, 7.83f, 18f)
            horizontalLineTo(18f)
            moveTo(4.563f, 5f)
            horizontalLineTo(20.998f)
            arcTo(1f, 1f, 0f, false, true, 21.979f, 6.204f)
            lineTo(20.953f, 12.43f)
            arcTo(2f, 2f, 0f, false, true, 18.962f, 14f)
            horizontalLineTo(6.25f)
            moveTo(16f, 20f)
            arcTo(2f, 2f, 0f, true, true, 20f, 20f)
            arcTo(2f, 2f, 0f, true, true, 16f, 20f)
            close()
            moveTo(6f, 20f)
            arcTo(2f, 2f, 0f, true, true, 10f, 20f)
            arcTo(2f, 2f, 0f, true, true, 6f, 20f)
            close()
        }
    }.build().also { cachedShoppingCart = it }

/** `ShoppingCart` vector, built on first access and reused afterwards. */
private var cachedShoppingCart: ImageVector? = null
