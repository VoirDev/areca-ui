package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clock check icon.
 *
 * Converted from Lucide `clock-check` (ISC).
 */
public val ArecaIcons.ClockCheck: ImageVector
    get() = cachedClockCheck ?: ImageVector.Builder(
        name = "ClockCheck",
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
            moveTo(21.95f, 13f)
            arcTo(10f, 10f, 0f, true, false, 13.265f, 21.92f)
            moveTo(12f, 6f)
            verticalLineTo(12f)
            lineTo(16f, 14f)
            moveTo(16f, 19f)
            lineTo(18f, 21f)
            lineTo(22f, 17f)
        }
    }.build().also { cachedClockCheck = it }

/** `ClockCheck` vector, built on first access and reused afterwards. */
private var cachedClockCheck: ImageVector? = null
