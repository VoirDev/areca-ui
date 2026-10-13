package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Zap off icon.
 *
 * Converted from Lucide `zap-off` (ISC).
 */
public val ArecaIcons.ZapOff: ImageVector
    get() = cachedZapOff ?: ImageVector.Builder(
        name = "ZapOff",
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
            moveTo(10.768f, 5.111f)
            lineTo(13.44f, 2.44f)
            arcTo(1.5f, 1.5f, 0f, false, true, 15.914f, 4.001f)
            lineTo(14.281f, 8.626f)
            moveTo(18.889f, 13.232f)
            lineTo(19.561f, 12.56f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 10f)
            horizontalLineTo(15.656f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(7.94f, 7.94f)
            lineTo(4.44f, 11.439f)
            arcTo(1.5f, 1.5f, 0f, false, false, 5.5f, 14f)
            horizontalLineTo(9.502f)
            arcTo(0.5f, 0.5f, 0f, false, true, 9.973f, 14.666f)
            lineTo(8.086f, 20f)
            arcTo(1.5f, 1.5f, 0f, false, false, 10.561f, 21.56f)
            lineTo(16.061f, 16.06f)
        }
    }.build().also { cachedZapOff = it }

/** `ZapOff` vector, built on first access and reused afterwards. */
private var cachedZapOff: ImageVector? = null
