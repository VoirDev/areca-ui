package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sandwich icon.
 *
 * Converted from Lucide `sandwich` (ISC).
 */
public val ArecaIcons.Sandwich: ImageVector
    get() = cachedSandwich ?: ImageVector.Builder(
        name = "Sandwich",
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
            moveTo(2.37f, 11.223f)
            lineTo(10.742f, 4.446f)
            arcTo(2f, 2f, 0f, false, true, 13.258f, 4.446f)
            lineTo(21.629f, 11.223f)
            moveTo(21f, 15f)
            arcTo(1f, 1f, 0f, false, true, 22f, 16f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 21f, 19f)
            horizontalLineTo(15.75f)
            moveTo(3f, 15f)
            arcTo(1f, 1f, 0f, false, false, 2f, 16f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, false, 3f, 19f)
            horizontalLineTo(12f)
            moveTo(6.67f, 15f)
            lineTo(12.8f, 19.6f)
            arcTo(2f, 2f, 0f, false, false, 15.6f, 19.2f)
            lineTo(18.75f, 15f)
            moveTo(3f, 11f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 22f, 12f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 21f, 15f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 2f, 14f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, true, 3f, 11f)
            close()
        }
    }.build().also { cachedSandwich = it }

/** `Sandwich` vector, built on first access and reused afterwards. */
private var cachedSandwich: ImageVector? = null
