package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Alarm clock off icon.
 *
 * Converted from Lucide `alarm-clock-off` (ISC).
 */
public val ArecaIcons.AlarmClockOff: ImageVector
    get() = cachedAlarmClockOff ?: ImageVector.Builder(
        name = "AlarmClockOff",
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
            moveTo(6.87f, 6.87f)
            arcTo(8f, 8f, 0f, true, false, 18.13f, 18.13f)
            moveTo(19.9f, 14.25f)
            arcTo(8f, 8f, 0f, false, false, 10.75f, 5.1f)
            moveTo(22f, 6f)
            lineTo(19f, 3f)
            moveTo(6.26f, 18.67f)
            lineTo(4f, 21f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(4f, 4f)
            lineTo(2f, 6f)
        }
    }.build().also { cachedAlarmClockOff = it }

/** `AlarmClockOff` vector, built on first access and reused afterwards. */
private var cachedAlarmClockOff: ImageVector? = null
