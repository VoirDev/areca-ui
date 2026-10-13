package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Basket icon.
 *
 * Converted from Tabler Icons `basket` (MIT).
 */
public val ArecaIcons.Basket: ImageVector
    get() = cachedBasket ?: ImageVector.Builder(
        name = "Basket",
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
            moveTo(10f, 14f)
            arcTo(2f, 2f, 0f, true, false, 14f, 14f)
            arcTo(2f, 2f, 0f, false, false, 10f, 14f)
            moveTo(5.001f, 8f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20.977f, 10.304f)
            lineTo(19.722f, 17.456f)
            arcTo(3f, 3f, 0f, false, true, 16.756f, 20f)
            horizontalLineTo(7.244f)
            arcTo(3f, 3f, 0f, false, true, 4.279f, 17.456f)
            lineTo(3.024f, 10.304f)
            arcTo(2f, 2f, 0f, false, true, 5.001f, 8f)
            moveTo(17f, 10f)
            lineTo(15f, 4f)
            moveTo(7f, 10f)
            lineTo(9f, 4f)
        }
    }.build().also { cachedBasket = it }

/** `Basket` vector, built on first access and reused afterwards. */
private var cachedBasket: ImageVector? = null
