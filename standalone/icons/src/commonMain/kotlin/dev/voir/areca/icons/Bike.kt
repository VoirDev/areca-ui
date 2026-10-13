package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bike icon.
 *
 * Converted from Lucide `bike` (ISC).
 */
public val ArecaIcons.Bike: ImageVector
    get() = cachedBike ?: ImageVector.Builder(
        name = "Bike",
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
            moveTo(15f, 17.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 22f, 17.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 15f, 17.5f)
            close()
            moveTo(2f, 17.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 9f, 17.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 2f, 17.5f)
            close()
            moveTo(14f, 5f)
            arcTo(1f, 1f, 0f, true, true, 16f, 5f)
            arcTo(1f, 1f, 0f, true, true, 14f, 5f)
            close()
            moveTo(12f, 17.5f)
            verticalLineTo(14f)
            lineTo(9f, 11f)
            lineTo(13f, 8f)
            lineTo(15f, 11f)
            horizontalLineTo(17f)
        }
    }.build().also { cachedBike = it }

/** `Bike` vector, built on first access and reused afterwards. */
private var cachedBike: ImageVector? = null
