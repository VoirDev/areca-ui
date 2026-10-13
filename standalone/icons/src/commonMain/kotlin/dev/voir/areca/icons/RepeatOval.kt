package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Repeat 2 icon.
 *
 * Converted from Lucide `repeat-2` (ISC).
 */
public val ArecaIcons.RepeatOval: ImageVector
    get() = cachedRepeatOval ?: ImageVector.Builder(
        name = "RepeatOval",
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
            moveTo(2f, 9f)
            lineTo(5f, 6f)
            lineTo(8f, 9f)
            moveTo(13f, 18f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 16f)
            verticalLineTo(6f)
            moveTo(22f, 15f)
            lineTo(19f, 18f)
            lineTo(16f, 15f)
            moveTo(11f, 6f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 8f)
            verticalLineTo(18f)
        }
    }.build().also { cachedRepeatOval = it }

/** `RepeatOval` vector, built on first access and reused afterwards. */
private var cachedRepeatOval: ImageVector? = null
