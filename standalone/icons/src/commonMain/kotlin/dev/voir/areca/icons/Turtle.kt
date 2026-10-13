package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Turtle icon.
 *
 * Converted from Lucide `turtle` (ISC).
 */
public val ArecaIcons.Turtle: ImageVector
    get() = cachedTurtle ?: ImageVector.Builder(
        name = "Turtle",
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
            moveTo(12f, 10f)
            lineTo(14f, 14f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 15f, 18f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 18f, 17f)
            verticalLineTo(14f)
            arcTo(8f, 8f, 0f, true, false, 2f, 14f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 3f, 18f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 6f, 17f)
            verticalLineTo(14f)
            lineTo(8f, 10f)
            horizontalLineTo(12f)
            close()
            moveTo(4.82f, 7.9f)
            lineTo(8f, 10f)
            moveTo(15.18f, 7.9f)
            lineTo(12f, 10f)
            moveTo(16.93f, 10f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 14f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedTurtle = it }

/** `Turtle` vector, built on first access and reused afterwards. */
private var cachedTurtle: ImageVector? = null
