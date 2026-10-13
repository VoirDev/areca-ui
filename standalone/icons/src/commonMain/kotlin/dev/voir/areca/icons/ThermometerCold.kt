package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Thermometer snowflake icon.
 *
 * Converted from Lucide `thermometer-snowflake` (ISC).
 */
public val ArecaIcons.ThermometerCold: ImageVector
    get() = cachedThermometerCold ?: ImageVector.Builder(
        name = "ThermometerCold",
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
            moveTo(10f, 20f)
            lineTo(8.75f, 17.5f)
            lineTo(6f, 18f)
            moveTo(10f, 4f)
            lineTo(8.75f, 6.5f)
            lineTo(6f, 6f)
            moveTo(10.585f, 15f)
            horizontalLineTo(10f)
            moveTo(2f, 12f)
            horizontalLineTo(8.5f)
            lineTo(10f, 9f)
            moveTo(20f, 14.54f)
            arcTo(4f, 4f, 0f, true, true, 16f, 14.54f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            close()
            moveTo(4f, 10f)
            lineTo(5.5f, 12f)
            lineTo(4f, 14f)
            moveTo(7f, 21f)
            lineTo(10f, 15f)
            lineTo(8.5f, 12f)
            moveTo(7f, 3f)
            lineTo(10f, 9f)
            horizontalLineTo(12f)
        }
    }.build().also { cachedThermometerCold = it }

/** `ThermometerCold` vector, built on first access and reused afterwards. */
private var cachedThermometerCold: ImageVector? = null
