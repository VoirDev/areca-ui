package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Steering wheel icon.
 *
 * Converted from Tabler Icons `steering-wheel` (MIT).
 */
public val ArecaIcons.SteeringWheel: ImageVector
    get() = cachedSteeringWheel ?: ImageVector.Builder(
        name = "SteeringWheel",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(10f, 12f)
            arcTo(2f, 2f, 0f, true, false, 14f, 12f)
            arcTo(2f, 2f, 0f, true, false, 10f, 12f)
            moveTo(12f, 14f)
            lineTo(12f, 21f)
            moveTo(10f, 12f)
            lineTo(3.25f, 10f)
            moveTo(14f, 12f)
            lineTo(20.75f, 10f)
        }
    }.build().also { cachedSteeringWheel = it }

/** `SteeringWheel` vector, built on first access and reused afterwards. */
private var cachedSteeringWheel: ImageVector? = null
