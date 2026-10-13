package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Alarm snooze icon.
 *
 * Converted from Tabler Icons `alarm-snooze` (MIT).
 */
public val ArecaIcons.ClockSnooze: ImageVector
    get() = cachedClockSnooze ?: ImageVector.Builder(
        name = "ClockSnooze",
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
            moveTo(5f, 13f)
            arcTo(7f, 7f, 0f, true, false, 19f, 13f)
            arcTo(7f, 7f, 0f, true, false, 5f, 13f)
            moveTo(10f, 11f)
            horizontalLineTo(14f)
            lineTo(10f, 15f)
            horizontalLineTo(14f)
            moveTo(7f, 4f)
            lineTo(4.25f, 6f)
            moveTo(17f, 4f)
            lineTo(19.75f, 6f)
        }
    }.build().also { cachedClockSnooze = it }

/** `ClockSnooze` vector, built on first access and reused afterwards. */
private var cachedClockSnooze: ImageVector? = null
