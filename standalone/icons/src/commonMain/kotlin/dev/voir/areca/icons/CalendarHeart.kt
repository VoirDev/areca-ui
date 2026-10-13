package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calendar heart icon.
 *
 * Converted from Lucide `calendar-heart` (ISC).
 */
public val ArecaIcons.CalendarHeart: ImageVector
    get() = cachedCalendarHeart ?: ImageVector.Builder(
        name = "CalendarHeart",
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
            moveTo(12.127f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(10.125f)
            moveTo(14.62f, 17.8f)
            arcTo(2.25f, 2.25f, 0f, true, true, 18f, 14.836f)
            arcTo(2.25f, 2.25f, 0f, true, true, 21.38f, 17.802f)
            lineTo(18.754f, 20.658f)
            arcTo(0.998f, 0.998f, 0f, false, true, 17.247f, 20.658f)
            close()
            moveTo(16f, 2f)
            verticalLineTo(5f)
            moveTo(3f, 9f)
            horizontalLineTo(21f)
            moveTo(8f, 2f)
            verticalLineTo(5f)
        }
    }.build().also { cachedCalendarHeart = it }

/** `CalendarHeart` vector, built on first access and reused afterwards. */
private var cachedCalendarHeart: ImageVector? = null
