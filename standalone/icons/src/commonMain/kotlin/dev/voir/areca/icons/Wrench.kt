package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wrench icon.
 *
 * Converted from Lucide `wrench` (ISC).
 */
public val ArecaIcons.Wrench: ImageVector
    get() = cachedWrench ?: ImageVector.Builder(
        name = "Wrench",
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
            moveTo(14.7f, 6.3f)
            arcTo(1f, 1f, 0f, false, false, 14.7f, 7.7f)
            lineTo(16.3f, 9.3f)
            arcTo(1f, 1f, 0f, false, false, 17.7f, 9.3f)
            lineTo(20.806f, 6.195f)
            curveTo(21.126f, 5.873f, 21.669f, 5.975f, 21.789f, 6.413f)
            arcTo(6f, 6f, 0f, false, true, 13.53f, 13.47f)
            lineTo(5.62f, 21.38f)
            arcTo(1f, 1f, 0f, false, true, 2.621f, 18.38f)
            lineTo(10.531f, 10.47f)
            arcTo(6f, 6f, 0f, false, true, 17.588f, 2.211f)
            curveTo(18.026f, 2.331f, 18.128f, 2.873f, 17.807f, 3.195f)
            close()
        }
    }.build().also { cachedWrench = it }

/** `Wrench` vector, built on first access and reused afterwards. */
private var cachedWrench: ImageVector? = null
