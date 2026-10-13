package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Truck return icon.
 *
 * Converted from Tabler Icons `truck-return` (MIT).
 */
public val ArecaIcons.TruckReturn: ImageVector
    get() = cachedTruckReturn ?: ImageVector.Builder(
        name = "TruckReturn",
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
            moveTo(5f, 17f)
            arcTo(2f, 2f, 0f, true, false, 9f, 17f)
            arcTo(2f, 2f, 0f, true, false, 5f, 17f)
            moveTo(15f, 17f)
            arcTo(2f, 2f, 0f, true, false, 19f, 17f)
            arcTo(2f, 2f, 0f, true, false, 15f, 17f)
            moveTo(5f, 17f)
            horizontalLineTo(3f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 4f, 5f)
            horizontalLineTo(13f)
            verticalLineTo(11f)
            horizontalLineTo(8f)
            lineTo(10f, 13f)
            moveTo(10f, 9f)
            lineTo(8f, 11f)
            moveTo(9f, 17f)
            lineTo(15f, 17f)
            moveTo(13f, 6f)
            horizontalLineTo(18f)
            lineTo(21f, 11f)
            verticalLineTo(17f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedTruckReturn = it }

/** `TruckReturn` vector, built on first access and reused afterwards. */
private var cachedTruckReturn: ImageVector? = null
