package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calendar dollar icon.
 *
 * Converted from Tabler Icons `calendar-dollar` (MIT).
 */
public val ArecaIcons.CalendarDollar: ImageVector
    get() = cachedCalendarDollar ?: ImageVector.Builder(
        name = "CalendarDollar",
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
            moveTo(13f, 21f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 19f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 6f, 5f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 7f)
            verticalLineTo(10f)
            moveTo(16f, 3f)
            verticalLineTo(7f)
            moveTo(8f, 3f)
            verticalLineTo(7f)
            moveTo(4f, 11f)
            horizontalLineTo(16.5f)
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 18f)
            horizontalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
        }
    }.build().also { cachedCalendarDollar = it }

/** `CalendarDollar` vector, built on first access and reused afterwards. */
private var cachedCalendarDollar: ImageVector? = null
