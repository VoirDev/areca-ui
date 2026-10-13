package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Thermometer sun icon.
 *
 * Converted from Lucide `thermometer-sun` (ISC).
 */
public val ArecaIcons.ThermometerWarm: ImageVector
    get() = cachedThermometerWarm ?: ImageVector.Builder(
        name = "ThermometerWarm",
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
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(12f, 8f)
            arcTo(4f, 4f, 0f, false, false, 10.355f, 15.647f)
            moveTo(2f, 12f)
            horizontalLineTo(4f)
            moveTo(20f, 14.54f)
            arcTo(4f, 4f, 0f, true, true, 16f, 14.54f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            close()
            moveTo(4.93f, 4.93f)
            lineTo(6.34f, 6.34f)
            moveTo(6.34f, 17.66f)
            lineTo(4.93f, 19.07f)
        }
    }.build().also { cachedThermometerWarm = it }

/** `ThermometerWarm` vector, built on first access and reused afterwards. */
private var cachedThermometerWarm: ImageVector? = null
