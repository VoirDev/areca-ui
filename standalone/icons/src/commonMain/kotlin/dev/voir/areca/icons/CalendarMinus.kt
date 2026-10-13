package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calendar minus icon.
 *
 * Converted from Lucide `calendar-minus` (ISC).
 */
public val ArecaIcons.CalendarMinus: ImageVector
    get() = cachedCalendarMinus ?: ImageVector.Builder(
        name = "CalendarMinus",
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
            moveTo(16f, 18f)
            horizontalLineTo(22f)
            moveTo(16f, 2f)
            verticalLineTo(5f)
            moveTo(21f, 14f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 19f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(13.3f)
            moveTo(3f, 9f)
            horizontalLineTo(21f)
            moveTo(8f, 2f)
            verticalLineTo(5f)
        }
    }.build().also { cachedCalendarMinus = it }

/** `CalendarMinus` vector, built on first access and reused afterwards. */
private var cachedCalendarMinus: ImageVector? = null
