package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Drone icon.
 *
 * Converted from Lucide `drone` (ISC).
 */
public val ArecaIcons.Drone: ImageVector
    get() = cachedDrone ?: ImageVector.Builder(
        name = "Drone",
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
            moveTo(10f, 10f)
            lineTo(7f, 7f)
            moveTo(10f, 14f)
            lineTo(7f, 17f)
            moveTo(14f, 10f)
            lineTo(17f, 7f)
            moveTo(14f, 14f)
            lineTo(17f, 17f)
            moveTo(14.205f, 4.139f)
            arcTo(4f, 4f, 0f, true, true, 19.644f, 10.002f)
            moveTo(19.637f, 14f)
            arcTo(4f, 4f, 0f, true, true, 14.205f, 19.868f)
            moveTo(4.367f, 10f)
            arcTo(4f, 4f, 0f, true, true, 9.805f, 4.138f)
            moveTo(9.795f, 19.862f)
            arcTo(4f, 4f, 0f, true, true, 4.366f, 13.989f)
            moveTo(11f, 8f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 9f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 13f, 16f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 15f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 11f, 8f)
            close()
        }
    }.build().also { cachedDrone = it }

/** `Drone` vector, built on first access and reused afterwards. */
private var cachedDrone: ImageVector? = null
