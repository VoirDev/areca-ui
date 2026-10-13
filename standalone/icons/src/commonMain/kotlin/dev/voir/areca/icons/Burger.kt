package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Burger icon.
 *
 * Converted from Tabler Icons `burger` (MIT).
 */
public val ArecaIcons.Burger: ImageVector
    get() = cachedBurger ?: ImageVector.Builder(
        name = "Burger",
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
            moveTo(4f, 15f)
            horizontalLineTo(20f)
            arcTo(4f, 4f, 0f, false, true, 16f, 19f)
            horizontalLineTo(8f)
            arcTo(4f, 4f, 0f, false, true, 4f, 15f)
            moveTo(12f, 4f)
            curveTo(15.783f, 4f, 18.953f, 6.133f, 19.786f, 9f)
            horizontalLineTo(4.214f)
            curveTo(5.047f, 6.133f, 8.217f, 4f, 12f, 4f)
            moveTo(5f, 12f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedBurger = it }

/** `Burger` vector, built on first access and reused afterwards. */
private var cachedBurger: ImageVector? = null
