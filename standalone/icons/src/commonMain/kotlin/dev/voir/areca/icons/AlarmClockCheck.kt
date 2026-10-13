package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Alarm clock check icon.
 *
 * Converted from Lucide `alarm-clock-check` (ISC).
 */
public val ArecaIcons.AlarmClockCheck: ImageVector
    get() = cachedAlarmClockCheck ?: ImageVector.Builder(
        name = "AlarmClockCheck",
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
            moveTo(4f, 13f)
            arcTo(8f, 8f, 0f, true, true, 20f, 13f)
            arcTo(8f, 8f, 0f, true, true, 4f, 13f)
            close()
            moveTo(5f, 3f)
            lineTo(2f, 6f)
            moveTo(22f, 6f)
            lineTo(19f, 3f)
            moveTo(6.38f, 18.7f)
            lineTo(4f, 21f)
            moveTo(17.64f, 18.67f)
            lineTo(20f, 21f)
            moveTo(9f, 13f)
            lineTo(11f, 15f)
            lineTo(15f, 11f)
        }
    }.build().also { cachedAlarmClockCheck = it }

/** `AlarmClockCheck` vector, built on first access and reused afterwards. */
private var cachedAlarmClockCheck: ImageVector? = null
