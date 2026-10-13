package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gauge icon.
 *
 * Same drawing as `ArecaIcons.Gauge`; both names are kept.
 *
 * Converted from Lucide `gauge` (ISC).
 */
public val ArecaIcons.Speedometer: ImageVector
    get() = cachedSpeedometer ?: ImageVector.Builder(
        name = "Speedometer",
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
            moveTo(12f, 14f)
            lineTo(16f, 10f)
            moveTo(3.34f, 19f)
            arcTo(10f, 10f, 0f, true, true, 20.66f, 19f)
        }
    }.build().also { cachedSpeedometer = it }

/** `Speedometer` vector, built on first access and reused afterwards. */
private var cachedSpeedometer: ImageVector? = null
