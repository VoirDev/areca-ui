package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Plant pot icon.
 *
 * Converted from Lucide `plant-pot` (ISC).
 */
public val ArecaIcons.PlantPot: ImageVector
    get() = cachedPlantPot ?: ImageVector.Builder(
        name = "PlantPot",
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
            moveTo(14f, 8.536f)
            verticalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 18f, 2f)
            horizontalLineTo(19.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 20f, 2.5f)
            verticalLineTo(4f)
            arcTo(4f, 4f, 0f, false, true, 16f, 8f)
            arcTo(4f, 4f, 0f, false, false, 12f, 12f)
            arcTo(5f, 5f, 0f, false, true, 4f, 8f)
            arcTo(5f, 5f, 0f, false, true, 12f, 12f)
            curveTo(12f, 14f, 13f, 15f, 13f, 17f)
            moveTo(18f, 17f)
            lineTo(16.915f, 20.58f)
            arcTo(2f, 2f, 0f, false, true, 15f, 22f)
            horizontalLineTo(9.002f)
            arcTo(2f, 2f, 0f, false, true, 7.089f, 20.582f)
            lineTo(6f, 17f)
            moveTo(5f, 17f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedPlantPot = it }

/** `PlantPot` vector, built on first access and reused afterwards. */
private var cachedPlantPot: ImageVector? = null
