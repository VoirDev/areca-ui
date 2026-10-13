package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Solar panel icon.
 *
 * Converted from Lucide `solar-panel` (ISC).
 */
public val ArecaIcons.SolarPanel: ImageVector
    get() = cachedSolarPanel ?: ImageVector.Builder(
        name = "SolarPanel",
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
            moveTo(11f, 2f)
            horizontalLineTo(13f)
            moveTo(14.28f, 14f)
            lineTo(9.72f, 22f)
            moveTo(21f, 22f)
            lineTo(19.442f, 18f)
            horizontalLineTo(4.558f)
            moveTo(3f, 10f)
            verticalLineTo(12f)
            moveTo(6.245f, 15.04f)
            arcTo(2f, 2f, 0f, false, true, 8f, 14f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 20.864f, 15.505f)
            lineTo(17.754f, 20.962f)
            arcTo(2f, 2f, 0f, false, true, 16f, 22f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3.137f, 20.494f)
            close()
            moveTo(7f, 2f)
            arcTo(4f, 4f, 0f, false, true, 3f, 6f)
            moveTo(8.66f, 7.66f)
            lineTo(10.07f, 9.07f)
        }
    }.build().also { cachedSolarPanel = it }

/** `SolarPanel` vector, built on first access and reused afterwards. */
private var cachedSolarPanel: ImageVector? = null
