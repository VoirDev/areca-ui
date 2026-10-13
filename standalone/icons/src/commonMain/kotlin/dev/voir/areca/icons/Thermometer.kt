package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Thermometer icon.
 *
 * Converted from Lucide `thermometer` (ISC).
 */
public val ArecaIcons.Thermometer: ImageVector
    get() = cachedThermometer ?: ImageVector.Builder(
        name = "Thermometer",
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
            moveTo(14f, 4f)
            verticalLineTo(14.54f)
            arcTo(4f, 4f, 0f, true, true, 10f, 14.54f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 14f, 4f)
            close()
        }
    }.build().also { cachedThermometer = it }

/** `Thermometer` vector, built on first access and reused afterwards. */
private var cachedThermometer: ImageVector? = null
