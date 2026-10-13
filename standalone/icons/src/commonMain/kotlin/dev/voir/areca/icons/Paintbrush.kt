package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paintbrush icon.
 *
 * Converted from Lucide `paintbrush` (ISC).
 */
public val ArecaIcons.Paintbrush: ImageVector
    get() = cachedPaintbrush ?: ImageVector.Builder(
        name = "Paintbrush",
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
            moveTo(14.622f, 17.897f)
            lineTo(3.942f, 14.984f)
            moveTo(18.376f, 2.622f)
            arcTo(1f, 1f, 0f, true, true, 21.378f, 5.624f)
            lineTo(17.36f, 9.643f)
            arcTo(0.5f, 0.5f, 0f, false, false, 17.36f, 10.35f)
            lineTo(18.304f, 11.294f)
            arcTo(2.41f, 2.41f, 0f, false, true, 18.304f, 14.702f)
            lineTo(17.36f, 15.646f)
            arcTo(0.5f, 0.5f, 0f, false, true, 16.653f, 15.646f)
            lineTo(8.354f, 7.348f)
            arcTo(0.5f, 0.5f, 0f, false, true, 8.354f, 6.641f)
            lineTo(9.298f, 5.697f)
            arcTo(2.41f, 2.41f, 0f, false, true, 12.706f, 5.697f)
            lineTo(13.65f, 6.641f)
            arcTo(0.5f, 0.5f, 0f, false, false, 14.357f, 6.641f)
            close()
            moveTo(9f, 8f)
            curveTo(7.196f, 10.71f, 5.03f, 11.46f, 2.417f, 11.948f)
            arcTo(0.507f, 0.507f, 0f, false, false, 2.115f, 12.767f)
            lineTo(9.435f, 21.65f)
            arcTo(1f, 1f, 0f, false, false, 10.62f, 21.854f)
            curveTo(12.735f, 20.405f, 16f, 16.792f, 16f, 15f)
        }
    }.build().also { cachedPaintbrush = it }

/** `Paintbrush` vector, built on first access and reused afterwards. */
private var cachedPaintbrush: ImageVector? = null
