package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Timer icon.
 *
 * Converted from Lucide `timer` (ISC).
 */
public val ArecaIcons.ClockStopwatch: ImageVector
    get() = cachedClockStopwatch ?: ImageVector.Builder(
        name = "ClockStopwatch",
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
            moveTo(10f, 2f)
            lineTo(14f, 2f)
            moveTo(12f, 14f)
            lineTo(15f, 11f)
            moveTo(4f, 14f)
            arcTo(8f, 8f, 0f, true, true, 20f, 14f)
            arcTo(8f, 8f, 0f, true, true, 4f, 14f)
            close()
        }
    }.build().also { cachedClockStopwatch = it }

/** `ClockStopwatch` vector, built on first access and reused afterwards. */
private var cachedClockStopwatch: ImageVector? = null
