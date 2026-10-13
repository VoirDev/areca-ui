package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flower icon.
 *
 * Converted from Lucide `flower` (ISC).
 */
public val ArecaIcons.Flower: ImageVector
    get() = cachedFlower ?: ImageVector.Builder(
        name = "Flower",
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
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
            moveTo(12f, 16.5f)
            arcTo(4.5f, 4.5f, 0f, true, true, 7.5f, 12f)
            arcTo(4.5f, 4.5f, 0f, true, true, 12f, 7.5f)
            arcTo(4.5f, 4.5f, 0f, true, true, 16.5f, 12f)
            arcTo(4.5f, 4.5f, 0f, true, true, 12f, 16.5f)
            moveTo(12f, 7.5f)
            verticalLineTo(9f)
            moveTo(7.5f, 12f)
            horizontalLineTo(9f)
            moveTo(16.5f, 12f)
            horizontalLineTo(15f)
            moveTo(12f, 16.5f)
            verticalLineTo(15f)
            moveTo(8f, 8f)
            lineTo(9.88f, 9.88f)
            moveTo(14.12f, 9.88f)
            lineTo(16f, 8f)
            moveTo(8f, 16f)
            lineTo(9.88f, 14.12f)
            moveTo(14.12f, 14.12f)
            lineTo(16f, 16f)
        }
    }.build().also { cachedFlower = it }

/** `Flower` vector, built on first access and reused afterwards. */
private var cachedFlower: ImageVector? = null
