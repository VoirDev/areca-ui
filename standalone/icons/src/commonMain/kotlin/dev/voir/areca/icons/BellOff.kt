package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bell off icon.
 *
 * Converted from Lucide `bell-off` (ISC).
 */
public val ArecaIcons.BellOff: ImageVector
    get() = cachedBellOff ?: ImageVector.Builder(
        name = "BellOff",
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
            moveTo(10.268f, 21f)
            arcTo(2f, 2f, 0f, false, false, 13.732f, 21f)
            moveTo(17f, 17f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3.26f, 15.327f)
            curveTo(4.59f, 13.956f, 6f, 12.499f, 6f, 8f)
            arcTo(6f, 6f, 0f, false, true, 6.258f, 6.258f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(8.668f, 3.01f)
            arcTo(6f, 6f, 0f, false, true, 18f, 8f)
            curveTo(18f, 10.687f, 18.77f, 12.653f, 19.707f, 14.05f)
        }
    }.build().also { cachedBellOff = it }

/** `BellOff` vector, built on first access and reused afterwards. */
private var cachedBellOff: ImageVector? = null
