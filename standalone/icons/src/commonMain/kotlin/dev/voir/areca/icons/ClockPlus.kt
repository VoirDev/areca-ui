package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clock plus icon.
 *
 * Converted from Lucide `clock-plus` (ISC).
 */
public val ArecaIcons.ClockPlus: ImageVector
    get() = cachedClockPlus ?: ImageVector.Builder(
        name = "ClockPlus",
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
            moveTo(12f, 6f)
            verticalLineTo(12f)
            lineTo(15.644f, 13.822f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
            moveTo(21.92f, 13.267f)
            arcTo(10f, 10f, 0f, true, false, 13.267f, 21.92f)
        }
    }.build().also { cachedClockPlus = it }

/** `ClockPlus` vector, built on first access and reused afterwards. */
private var cachedClockPlus: ImageVector? = null
