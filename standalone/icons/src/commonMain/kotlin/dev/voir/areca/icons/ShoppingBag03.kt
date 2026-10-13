package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shopping bag icon.
 *
 * Converted from Lucide `shopping-bag` (ISC).
 */
public val ArecaIcons.ShoppingBag03: ImageVector
    get() = cachedShoppingBag03 ?: ImageVector.Builder(
        name = "ShoppingBag03",
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
            moveTo(16f, 10f)
            arcTo(4f, 4f, 0f, false, true, 8f, 10f)
            moveTo(3.103f, 6.034f)
            horizontalLineTo(20.897f)
            moveTo(3.4f, 5.467f)
            arcTo(2f, 2f, 0f, false, false, 3f, 6.667f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 5f, 22f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 20f)
            verticalLineTo(6.667f)
            arcTo(2f, 2f, 0f, false, false, 20.6f, 5.467f)
            lineTo(18.6f, 2.8f)
            arcTo(2f, 2f, 0f, false, false, 17f, 2f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5.4f, 2.8f)
            close()
        }
    }.build().also { cachedShoppingBag03 = it }

/** `ShoppingBag03` vector, built on first access and reused afterwards. */
private var cachedShoppingBag03: ImageVector? = null
