package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lamp icon.
 *
 * Converted from Lucide `lamp` (ISC).
 */
public val ArecaIcons.Lamp: ImageVector
    get() = cachedLamp ?: ImageVector.Builder(
        name = "Lamp",
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
            moveTo(12f, 12f)
            verticalLineTo(18f)
            moveTo(4.077f, 10.615f)
            arcTo(1f, 1f, 0f, false, false, 5f, 12f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 19.923f, 10.615f)
            lineTo(16.846f, 3.231f)
            arcTo(2f, 2f, 0f, false, false, 15f, 2f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, false, 7.154f, 3.23f)
            close()
            moveTo(8f, 20f)
            arcTo(2f, 2f, 0f, false, true, 10f, 18f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 20f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 15f, 22f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 8f, 21f)
            close()
        }
    }.build().also { cachedLamp = it }

/** `Lamp` vector, built on first access and reused afterwards. */
private var cachedLamp: ImageVector? = null
