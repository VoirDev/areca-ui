package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Needle thread icon.
 *
 * Converted from Tabler Icons `needle-thread` (MIT).
 */
public val ArecaIcons.NeedleThread: ImageVector
    get() = cachedNeedleThread ?: ImageVector.Builder(
        name = "NeedleThread",
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
            moveTo(3f, 21f)
            curveTo(2.333f, 20.333f, 6.262f, 14.764f, 14.785f, 4.291f)
            arcTo(3.5f, 3.5f, 0f, true, true, 19.863f, 9.082f)
            curveTo(9.288f, 17.694f, 3.667f, 21.667f, 3f, 21f)
            moveTo(17.5f, 6.5f)
            lineTo(16.5f, 7.5f)
            moveTo(17f, 7f)
            curveTo(14.667f, 4.333f, 13.5f, 3f, 12f, 3f)
            reflectiveCurveTo(10f, 4f, 10f, 5f)
            curveTo(10f, 9f, 18.161f, 13.406f, 16f, 16f)
            curveTo(14.944f, 17.268f, 12.637f, 17.285f, 10.25f, 16.808f)
            moveTo(5.739f, 15.425f)
            curveTo(4.346f, 14.86f, 2f, 13.5f, 2f, 12f)
            moveTo(19.5f, 9.5f)
            lineTo(21f, 11f)
        }
    }.build().also { cachedNeedleThread = it }

/** `NeedleThread` vector, built on first access and reused afterwards. */
private var cachedNeedleThread: ImageVector? = null
