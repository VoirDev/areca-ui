package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shield off icon.
 *
 * Converted from Lucide `shield-off` (ISC).
 */
public val ArecaIcons.ShieldOff: ImageVector
    get() = cachedShieldOff ?: ImageVector.Builder(
        name = "ShieldOff",
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
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(5f, 5f)
            arcTo(1f, 1f, 0f, false, false, 4f, 6f)
            verticalLineTo(13f)
            curveTo(4f, 18f, 7.5f, 20.5f, 11.67f, 21.94f)
            arcTo(1f, 1f, 0f, false, false, 12.34f, 21.95f)
            curveTo(14.69f, 21.13f, 16.82f, 19.98f, 18.24f, 18.24f)
            moveTo(9.309f, 3.652f)
            arcTo(12.252f, 12.252f, 0f, false, false, 11.24f, 2.28f)
            arcTo(1.17f, 1.17f, 0f, false, true, 12.76f, 2.28f)
            curveTo(14.51f, 3.81f, 17f, 5f, 19f, 5f)
            arcTo(1f, 1f, 0f, false, true, 20f, 6f)
            verticalLineTo(13f)
            arcTo(9.784f, 9.784f, 0f, false, true, 19.92f, 14.264f)
        }
    }.build().also { cachedShieldOff = it }

/** `ShieldOff` vector, built on first access and reused afterwards. */
private var cachedShieldOff: ImageVector? = null
