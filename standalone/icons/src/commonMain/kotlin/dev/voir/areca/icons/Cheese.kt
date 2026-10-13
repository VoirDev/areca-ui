package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cheese icon.
 *
 * Converted from Tabler Icons `cheese` (MIT).
 */
public val ArecaIcons.Cheese: ImageVector
    get() = cachedCheese ?: ImageVector.Builder(
        name = "Cheese",
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
            moveTo(4.519f, 20.008f)
            lineTo(21f, 20f)
            verticalLineTo(16.5f)
            arcTo(2f, 2f, 0f, true, true, 21f, 12.5f)
            verticalLineTo(9f)
            horizontalLineTo(4.278f)
            moveTo(21f, 9f)
            lineTo(11.615f, 4.008f)
            curveTo(9.103f, 4.128f, 6.857f, 5.428f, 5.288f, 7.433f)
            curveTo(3.865f, 9.253f, 3f, 11.654f, 3f, 14.287f)
            curveTo(3f, 16.404f, 3.56f, 18.372f, 4.519f, 20.008f)
            moveTo(15f, 13f)
            verticalLineTo(13.01f)
            moveTo(8f, 13f)
            verticalLineTo(13.01f)
            moveTo(11f, 16f)
            verticalLineTo(16.01f)
        }
    }.build().also { cachedCheese = it }

/** `Cheese` vector, built on first access and reused afterwards. */
private var cachedCheese: ImageVector? = null
