package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Utensils crossed icon.
 *
 * Converted from Lucide `utensils-crossed` (ISC).
 */
public val ArecaIcons.UtensilsCrossed: ImageVector
    get() = cachedUtensilsCrossed ?: ImageVector.Builder(
        name = "UtensilsCrossed",
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
            moveTo(16f, 2f)
            lineTo(13.7f, 4.3f)
            arcTo(3f, 3f, 0f, false, false, 13.7f, 8.5f)
            lineTo(15.5f, 10.3f)
            arcTo(3f, 3f, 0f, false, false, 19.7f, 10.3f)
            lineTo(22f, 8f)
            moveTo(15f, 15f)
            lineTo(3.3f, 3.3f)
            arcTo(4.2f, 4.2f, 0f, false, false, 3.3f, 9.3f)
            lineTo(10.6f, 16.6f)
            curveTo(11.3f, 17.3f, 12.6f, 17.3f, 13.4f, 16.6f)
            lineTo(15f, 15f)
            close()
            moveTo(15f, 15f)
            lineTo(22f, 22f)
            moveTo(2.1f, 21.8f)
            lineTo(8.5f, 15.5f)
            moveTo(19f, 5f)
            lineTo(12f, 12f)
        }
    }.build().also { cachedUtensilsCrossed = it }

/** `UtensilsCrossed` vector, built on first access and reused afterwards. */
private var cachedUtensilsCrossed: ImageVector? = null
