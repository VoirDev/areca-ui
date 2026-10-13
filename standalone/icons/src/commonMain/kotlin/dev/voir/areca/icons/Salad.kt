package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Salad icon.
 *
 * Converted from Lucide `salad` (ISC).
 */
public val ArecaIcons.Salad: ImageVector
    get() = cachedSalad ?: ImageVector.Builder(
        name = "Salad",
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
            moveTo(19.496f, 12f)
            arcTo(2.5f, 2.5f, 0f, false, false, 19.895f, 9.786f)
            arcTo(2f, 2f, 0f, false, false, 20.32f, 6.5f)
            arcTo(2f, 2f, 0f, false, false, 19f, 3f)
            arcTo(2f, 2f, 0f, false, false, 17.5f, 3.68f)
            arcTo(2f, 2f, 0f, false, false, 14.213f, 4.104f)
            arcTo(2.5f, 2.5f, 0f, false, false, 11.024f, 6.164f)
            arcTo(3f, 3f, 0f, false, false, 12f, 12f)
            lineTo(16f, 8f)
            moveTo(4f, 12f)
            arcTo(1f, 1f, 0f, false, false, 3.01f, 13.133f)
            arcTo(9f, 9f, 0f, false, false, 12f, 21f)
            arcTo(9f, 9f, 0f, false, false, 20.99f, 13.133f)
            arcTo(1f, 1f, 0f, false, false, 20f, 12f)
            close()
            moveTo(7f, 21f)
            horizontalLineTo(17f)
            moveTo(9.85f, 6.907f)
            arcTo(3.5f, 3.5f, 0f, false, false, 5.05f, 12f)
        }
    }.build().also { cachedSalad = it }

/** `Salad` vector, built on first access and reused afterwards. */
private var cachedSalad: ImageVector? = null
