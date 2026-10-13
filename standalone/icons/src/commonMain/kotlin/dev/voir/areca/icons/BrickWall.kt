package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Brick wall icon.
 *
 * Converted from Lucide `brick-wall` (ISC).
 */
public val ArecaIcons.BrickWall: ImageVector
    get() = cachedBrickWall ?: ImageVector.Builder(
        name = "BrickWall",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(12f, 9f)
            verticalLineTo(15f)
            moveTo(16f, 15f)
            verticalLineTo(21f)
            moveTo(16f, 3f)
            verticalLineTo(9f)
            moveTo(3f, 15f)
            horizontalLineTo(21f)
            moveTo(3f, 9f)
            horizontalLineTo(21f)
            moveTo(8f, 15f)
            verticalLineTo(21f)
            moveTo(8f, 3f)
            verticalLineTo(9f)
        }
    }.build().also { cachedBrickWall = it }

/** `BrickWall` vector, built on first access and reused afterwards. */
private var cachedBrickWall: ImageVector? = null
