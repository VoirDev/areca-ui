package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Truck icon.
 *
 * Converted from Lucide `truck` (ISC).
 */
public val ArecaIcons.Truck: ImageVector
    get() = cachedTruck ?: ImageVector.Builder(
        name = "Truck",
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
            moveTo(14f, 18f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 12f, 4f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 6f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 3f, 18f)
            horizontalLineTo(5f)
            moveTo(15f, 18f)
            horizontalLineTo(9f)
            moveTo(19f, 18f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, false, 22f, 17f)
            verticalLineTo(13.35f)
            arcTo(1f, 1f, 0f, false, false, 21.78f, 12.726f)
            lineTo(18.3f, 8.376f)
            arcTo(1f, 1f, 0f, false, false, 17.52f, 8f)
            horizontalLineTo(14f)
            moveTo(15f, 18f)
            arcTo(2f, 2f, 0f, true, true, 19f, 18f)
            arcTo(2f, 2f, 0f, true, true, 15f, 18f)
            close()
            moveTo(5f, 18f)
            arcTo(2f, 2f, 0f, true, true, 9f, 18f)
            arcTo(2f, 2f, 0f, true, true, 5f, 18f)
            close()
        }
    }.build().also { cachedTruck = it }

/** `Truck` vector, built on first access and reused afterwards. */
private var cachedTruck: ImageVector? = null
