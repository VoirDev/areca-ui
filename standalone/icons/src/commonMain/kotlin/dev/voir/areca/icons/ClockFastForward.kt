package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rotate cw clock icon.
 *
 * Converted from Lucide `rotate-cw-clock` (ISC).
 */
public val ArecaIcons.ClockFastForward: ImageVector
    get() = cachedClockFastForward ?: ImageVector.Builder(
        name = "ClockFastForward",
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
            moveTo(12f, 7f)
            verticalLineTo(12f)
            lineTo(16f, 14f)
            moveTo(16f, 8f)
            horizontalLineTo(21f)
            verticalLineTo(3f)
            moveTo(21f, 8f)
            lineTo(18.7f, 5.7f)
            arcTo(9.7f, 9.7f, 0f, false, false, 12f, 3f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
        }
    }.build().also { cachedClockFastForward = it }

/** `ClockFastForward` vector, built on first access and reused afterwards. */
private var cachedClockFastForward: ImageVector? = null
