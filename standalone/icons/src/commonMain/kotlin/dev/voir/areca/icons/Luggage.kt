package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Luggage icon.
 *
 * Converted from Lucide `luggage` (ISC).
 */
public val ArecaIcons.Luggage: ImageVector
    get() = cachedLuggage ?: ImageVector.Builder(
        name = "Luggage",
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
            moveTo(6f, 20f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 6f, 6f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 8f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            moveTo(8f, 18f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 10f, 2f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 4f)
            verticalLineTo(18f)
            moveTo(10f, 20f)
            horizontalLineTo(14f)
            moveTo(14f, 20f)
            arcTo(2f, 2f, 0f, true, true, 18f, 20f)
            arcTo(2f, 2f, 0f, true, true, 14f, 20f)
            close()
            moveTo(6f, 20f)
            arcTo(2f, 2f, 0f, true, true, 10f, 20f)
            arcTo(2f, 2f, 0f, true, true, 6f, 20f)
            close()
        }
    }.build().also { cachedLuggage = it }

/** `Luggage` vector, built on first access and reused afterwards. */
private var cachedLuggage: ImageVector? = null
