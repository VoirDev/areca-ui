package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calendar days icon.
 *
 * Converted from Lucide `calendar-days` (ISC).
 */
public val ArecaIcons.CalendarDate: ImageVector
    get() = cachedCalendarDate ?: ImageVector.Builder(
        name = "CalendarDate",
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
            moveTo(8f, 13f)
            horizontalLineTo(8.01f)
            moveTo(12f, 13f)
            horizontalLineTo(12.01f)
            moveTo(16f, 13f)
            horizontalLineTo(16.01f)
            moveTo(8f, 17f)
            horizontalLineTo(8.01f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
            moveTo(16f, 17f)
            horizontalLineTo(16.01f)
        }
    }.build().also { cachedCalendarDate = it }

/** `CalendarDate` vector, built on first access and reused afterwards. */
private var cachedCalendarDate: ImageVector? = null
