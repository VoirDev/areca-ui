package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fan icon.
 *
 * Converted from Lucide `fan` (ISC).
 */
public val ArecaIcons.Fan: ImageVector
    get() = cachedFan ?: ImageVector.Builder(
        name = "Fan",
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
            moveTo(10.827f, 16.379f)
            arcTo(6.082f, 6.082f, 0f, false, true, 2.209f, 9.377f)
            lineTo(7.621f, 10.827f)
            arcTo(6.082f, 6.082f, 0f, false, true, 14.623f, 2.209f)
            lineTo(13.173f, 7.621f)
            arcTo(6.082f, 6.082f, 0f, false, true, 21.791f, 14.623f)
            lineTo(16.379f, 13.173f)
            arcTo(6.082f, 6.082f, 0f, false, true, 9.377f, 21.791f)
            lineTo(10.827f, 16.379f)
            close()
            moveTo(12f, 12f)
            verticalLineTo(12.01f)
        }
    }.build().also { cachedFan = it }

/** `Fan` vector, built on first access and reused afterwards. */
private var cachedFan: ImageVector? = null
