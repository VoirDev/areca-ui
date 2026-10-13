package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rotate ccw clock icon.
 *
 * Converted from Lucide `rotate-ccw-clock` (ISC).
 */
public val ArecaIcons.ClockRewind: ImageVector
    get() = cachedClockRewind ?: ImageVector.Builder(
        name = "ClockRewind",
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
            arcTo(9f, 9f, 0f, true, false, 12f, 3f)
            arcTo(9.75f, 9.75f, 0f, false, false, 5.26f, 5.74f)
            lineTo(3f, 8f)
            moveTo(3f, 3f)
            verticalLineTo(8f)
            horizontalLineTo(8f)
            moveTo(12f, 7f)
            verticalLineTo(12f)
            lineTo(16f, 14f)
        }
    }.build().also { cachedClockRewind = it }

/** `ClockRewind` vector, built on first access and reused afterwards. */
private var cachedClockRewind: ImageVector? = null
