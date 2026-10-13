package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flower 2 icon.
 *
 * Converted from Lucide `flower-2` (ISC).
 */
public val ArecaIcons.Flower2: ImageVector
    get() = cachedFlower2 ?: ImageVector.Builder(
        name = "Flower2",
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
            moveTo(12f, 5f)
            arcTo(3f, 3f, 0f, true, true, 15f, 8f)
            moveTo(12f, 5f)
            arcTo(3f, 3f, 0f, true, false, 9f, 8f)
            moveTo(12f, 5f)
            verticalLineTo(6f)
            moveTo(9f, 8f)
            arcTo(3f, 3f, 0f, true, false, 12f, 11f)
            moveTo(9f, 8f)
            horizontalLineTo(10f)
            moveTo(15f, 8f)
            arcTo(3f, 3f, 0f, true, true, 12f, 11f)
            moveTo(15f, 8f)
            horizontalLineTo(14f)
            moveTo(12f, 11f)
            verticalLineTo(10f)
            moveTo(10f, 8f)
            arcTo(2f, 2f, 0f, true, true, 14f, 8f)
            arcTo(2f, 2f, 0f, true, true, 10f, 8f)
            close()
            moveTo(12f, 10f)
            verticalLineTo(22f)
            moveTo(12f, 22f)
            curveTo(16.2f, 22f, 19f, 20.333f, 19f, 17f)
            curveTo(14.8f, 17f, 12f, 18.667f, 12f, 22f)
            close()
            moveTo(12f, 22f)
            curveTo(7.8f, 22f, 5f, 20.333f, 5f, 17f)
            curveTo(9.2f, 17f, 12f, 18.667f, 12f, 22f)
            close()
        }
    }.build().also { cachedFlower2 = it }

/** `Flower2` vector, built on first access and reused afterwards. */
private var cachedFlower2: ImageVector? = null
