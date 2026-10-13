package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device airpods icon.
 *
 * Converted from Tabler Icons `device-airpods` (MIT).
 */
public val ArecaIcons.Airpods: ImageVector
    get() = cachedAirpods ?: ImageVector.Builder(
        name = "Airpods",
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
            moveTo(6f, 4f)
            arcTo(4f, 4f, 0f, false, true, 10f, 7.8f)
            lineTo(10f, 8f)
            verticalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 7f, 18.5f)
            verticalLineTo(12f)
            horizontalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 2f, 8.2f)
            lineTo(2f, 8f)
            arcTo(4f, 4f, 0f, false, true, 6f, 4f)
            moveTo(18f, 4f)
            arcTo(4f, 4f, 0f, false, false, 14f, 7.8f)
            lineTo(14f, 8f)
            verticalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 17f, 18.5f)
            verticalLineTo(12f)
            horizontalLineTo(18f)
            arcTo(4f, 4f, 0f, false, false, 22f, 8.2f)
            lineTo(22f, 8f)
            arcTo(4f, 4f, 0f, false, false, 18f, 4f)
        }
    }.build().also { cachedAirpods = it }

/** `Airpods` vector, built on first access and reused afterwards. */
private var cachedAirpods: ImageVector? = null
