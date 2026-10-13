package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Horseshoe icon.
 *
 * Converted from Tabler Icons `horseshoe` (MIT).
 */
public val ArecaIcons.Horseshoe: ImageVector
    get() = cachedHorseshoe ?: ImageVector.Builder(
        name = "Horseshoe",
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
            moveTo(19f, 17f)
            curveTo(19.5f, 15.758f, 21f, 15f, 21f, 12f)
            reflectiveCurveTo(20f, 3f, 12f, 3f)
            reflectiveCurveTo(3f, 9f, 3f, 12f)
            reflectiveCurveTo(4.495f, 15.749f, 5f, 17f)
            lineTo(3f, 18f)
            lineTo(5f, 21f)
            lineTo(7.406f, 19.853f)
            curveTo(8.656f, 19.139f, 9.184f, 17.773f, 8.609f, 16.49f)
            curveTo(7.531f, 14.083f, 7f, 8f, 12f, 8f)
            reflectiveCurveTo(16.469f, 14.083f, 15.39f, 16.49f)
            curveTo(14.816f, 17.774f, 15.345f, 19.139f, 16.594f, 19.853f)
            lineTo(19f, 21f)
            lineTo(21f, 18f)
            lineTo(19f, 17f)
        }
    }.build().also { cachedHorseshoe = it }

/** `Horseshoe` vector, built on first access and reused afterwards. */
private var cachedHorseshoe: ImageVector? = null
