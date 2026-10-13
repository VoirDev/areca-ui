package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toy brick icon.
 *
 * Converted from Lucide `toy-brick` (ISC).
 */
public val ArecaIcons.ToyBrick: ImageVector
    get() = cachedToyBrick ?: ImageVector.Builder(
        name = "ToyBrick",
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
            moveTo(4f, 8f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 9f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 19f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 4f, 8f)
            close()
            moveTo(10f, 8f)
            verticalLineTo(5f)
            curveTo(10f, 4.4f, 9.6f, 4f, 9f, 4f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, false, 5f, 5f)
            verticalLineTo(8f)
            moveTo(19f, 8f)
            verticalLineTo(5f)
            curveTo(19f, 4.4f, 18.6f, 4f, 18f, 4f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 14f, 5f)
            verticalLineTo(8f)
        }
    }.build().also { cachedToyBrick = it }

/** `ToyBrick` vector, built on first access and reused afterwards. */
private var cachedToyBrick: ImageVector? = null
