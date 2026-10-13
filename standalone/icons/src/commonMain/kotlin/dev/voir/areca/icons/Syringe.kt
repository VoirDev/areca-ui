package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Syringe icon.
 *
 * Converted from Lucide `syringe` (ISC).
 */
public val ArecaIcons.Syringe: ImageVector
    get() = cachedSyringe ?: ImageVector.Builder(
        name = "Syringe",
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
            moveTo(18f, 2f)
            lineTo(22f, 6f)
            moveTo(17f, 7f)
            lineTo(20f, 4f)
            moveTo(19f, 9f)
            lineTo(8.7f, 19.3f)
            curveTo(7.7f, 20.3f, 6.2f, 20.3f, 5.3f, 19.3f)
            lineTo(4.7f, 18.7f)
            curveTo(3.7f, 17.7f, 3.7f, 16.2f, 4.7f, 15.3f)
            lineTo(15f, 5f)
            moveTo(9f, 11f)
            lineTo(13f, 15f)
            moveTo(5f, 19f)
            lineTo(2f, 22f)
            moveTo(14f, 4f)
            lineTo(20f, 10f)
        }
    }.build().also { cachedSyringe = it }

/** `Syringe` vector, built on first access and reused afterwards. */
private var cachedSyringe: ImageVector? = null
