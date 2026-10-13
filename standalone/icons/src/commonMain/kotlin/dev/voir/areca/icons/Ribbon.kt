package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ribbon icon.
 *
 * Converted from Lucide `ribbon` (ISC).
 */
public val ArecaIcons.Ribbon: ImageVector
    get() = cachedRibbon ?: ImageVector.Builder(
        name = "Ribbon",
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
            moveTo(12f, 11.22f)
            curveTo(11f, 9.997f, 10f, 9f, 10f, 8f)
            arcTo(2f, 2f, 0f, false, true, 14f, 8f)
            curveTo(14f, 9f, 13.002f, 10.002f, 11.99f, 11.22f)
            moveTo(12f, 18f)
            lineTo(14.57f, 14.5f)
            moveTo(6.243f, 9.016f)
            arcTo(7f, 7f, 0f, false, true, 17.75f, 9.007f)
            moveTo(9.35f, 14.53f)
            lineTo(12f, 11.22f)
            moveTo(9.35f, 14.53f)
            curveTo(7.728f, 12.246f, 6f, 10.221f, 6f, 7f)
            arcTo(6f, 5f, 0f, false, true, 18f, 7f)
            curveTo(17.995f, 10.22f, 16.222f, 12.235f, 14.57f, 14.5f)
            lineTo(18.127f, 19.027f)
            arcTo(1f, 1f, 0f, false, true, 17.924f, 20.457f)
            lineTo(16.03f, 21.817f)
            arcTo(1f, 1f, 0f, false, true, 14.646f, 21.602f)
            lineTo(12f, 18f)
            lineTo(9.321f, 21.593f)
            arcTo(1f, 1f, 0f, false, true, 7.931f, 21.806f)
            lineTo(6.066f, 20.453f)
            arcTo(1f, 1f, 0f, false, true, 5.863f, 19.031f)
            close()
        }
    }.build().also { cachedRibbon = it }

/** `Ribbon` vector, built on first access and reused afterwards. */
private var cachedRibbon: ImageVector? = null
