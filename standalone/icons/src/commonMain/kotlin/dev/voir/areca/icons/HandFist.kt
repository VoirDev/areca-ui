package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand fist icon.
 *
 * Converted from Lucide `hand-fist` (ISC).
 */
public val ArecaIcons.HandFist: ImageVector
    get() = cachedHandFist ?: ImageVector.Builder(
        name = "HandFist",
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
            moveTo(12.035f, 17.012f)
            arcTo(3f, 3f, 0f, false, false, 9.035f, 14.012f)
            lineTo(8.724f, 14.01f)
            arcTo(0.72f, 0.72f, 0f, false, true, 8.219f, 12.781f)
            lineTo(9.414f, 11.586f)
            arcTo(2f, 2f, 0f, false, true, 10.828f, 11f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, false, 12f, 7f)
            horizontalLineTo(9.243f)
            arcTo(3f, 3f, 0f, false, false, 7.121f, 7.879f)
            lineTo(4.414f, 10.586f)
            arcTo(4.83f, 4.83f, 0f, false, false, 3f, 14f)
            arcTo(8f, 8f, 0f, false, false, 11f, 22f)
            horizontalLineTo(13f)
            arcTo(8f, 8f, 0f, false, false, 21f, 14f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, true, false, 17f, 7f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, true, false, 21f, 9f)
            moveTo(13.888f, 9.662f)
            arcTo(2f, 2f, 0f, false, false, 17f, 8f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, true, false, 13f, 5f)
            moveTo(9f, 5f)
            arcTo(2f, 2f, 0f, true, false, 5f, 5f)
            verticalLineTo(10f)
            moveTo(9f, 7f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, true, true, 13f, 4f)
            verticalLineTo(7.268f)
        }
    }.build().also { cachedHandFist = it }

/** `HandFist` vector, built on first access and reused afterwards. */
private var cachedHandFist: ImageVector? = null
