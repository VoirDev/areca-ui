package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paint bucket icon.
 *
 * Converted from Lucide `paint-bucket` (ISC).
 */
public val ArecaIcons.PaintPour: ImageVector
    get() = cachedPaintPour ?: ImageVector.Builder(
        name = "PaintPour",
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
            moveTo(11f, 7f)
            lineTo(6f, 2f)
            moveTo(18.992f, 12f)
            horizontalLineTo(2.041f)
            moveTo(21.145f, 18.38f)
            arcTo(3.34f, 3.34f, 0f, false, true, 20f, 16.5f)
            arcTo(3.3f, 3.3f, 0f, false, true, 18.855f, 18.38f)
            curveTo(18.28f, 18.84f, 18f, 19.4f, 18f, 19.975f)
            arcTo(2f, 2f, 0f, false, false, 20f, 22f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19.975f)
            curveTo(22f, 19.395f, 21.715f, 18.845f, 21.145f, 18.38f)
            moveTo(8.5f, 4.5f)
            lineTo(10.648f, 2.352f)
            arcTo(1.205f, 1.205f, 0f, false, true, 12.352f, 2.352f)
            lineTo(19.648f, 9.648f)
            arcTo(1.205f, 1.205f, 0f, false, true, 19.648f, 11.352f)
            lineTo(12.056f, 18.944f)
            arcTo(3.615f, 3.615f, 0f, false, true, 6.944f, 18.944f)
            lineTo(3.056f, 15.056f)
            arcTo(3.615f, 3.615f, 0f, false, true, 3.056f, 9.944f)
            lineTo(5.67f, 7.33f)
        }
    }.build().also { cachedPaintPour = it }

/** `PaintPour` vector, built on first access and reused afterwards. */
private var cachedPaintPour: ImageVector? = null
