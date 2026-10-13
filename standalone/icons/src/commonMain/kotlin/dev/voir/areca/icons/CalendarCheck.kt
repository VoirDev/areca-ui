package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calendar check icon.
 *
 * Converted from Lucide `calendar-check` (ISC).
 */
public val ArecaIcons.CalendarCheck: ImageVector
    get() = cachedCalendarCheck ?: ImageVector.Builder(
        name = "CalendarCheck",
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
            moveTo(8f, 2f)
            verticalLineTo(5f)
            moveTo(16f, 2f)
            verticalLineTo(5f)
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(3f, 9f)
            horizontalLineTo(21f)
            moveTo(9f, 15f)
            lineTo(11f, 17f)
            lineTo(15f, 13f)
        }
    }.build().also { cachedCalendarCheck = it }

/** `CalendarCheck` vector, built on first access and reused afterwards. */
private var cachedCalendarCheck: ImageVector? = null
