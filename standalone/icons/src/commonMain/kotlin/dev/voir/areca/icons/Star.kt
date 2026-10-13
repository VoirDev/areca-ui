package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Star icon.
 *
 * Converted from Lucide `star` (ISC).
 */
public val ArecaIcons.Star: ImageVector
    get() = cachedStar ?: ImageVector.Builder(
        name = "Star",
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
            moveTo(11.525f, 2.295f)
            arcTo(0.53f, 0.53f, 0f, false, true, 12.475f, 2.295f)
            lineTo(14.785f, 6.974f)
            arcTo(2.123f, 2.123f, 0f, false, false, 16.38f, 8.134f)
            lineTo(21.546f, 8.89f)
            arcTo(0.53f, 0.53f, 0f, false, true, 21.84f, 9.794f)
            lineTo(18.104f, 13.432f)
            arcTo(2.123f, 2.123f, 0f, false, false, 17.493f, 15.31f)
            lineTo(18.375f, 20.45f)
            arcTo(0.53f, 0.53f, 0f, false, true, 17.604f, 21.01f)
            lineTo(12.986f, 18.582f)
            arcTo(2.122f, 2.122f, 0f, false, false, 11.013f, 18.582f)
            lineTo(6.396f, 21.01f)
            arcTo(0.53f, 0.53f, 0f, false, true, 5.626f, 20.45f)
            lineTo(6.507f, 15.311f)
            arcTo(2.122f, 2.122f, 0f, false, false, 5.896f, 13.432f)
            lineTo(2.16f, 9.795f)
            arcTo(0.53f, 0.53f, 0f, false, true, 2.454f, 8.889f)
            lineTo(7.619f, 8.134f)
            arcTo(2.122f, 2.122f, 0f, false, false, 9.216f, 6.974f)
            close()
        }
    }.build().also { cachedStar = it }

/** `Star` vector, built on first access and reused afterwards. */
private var cachedStar: ImageVector? = null
