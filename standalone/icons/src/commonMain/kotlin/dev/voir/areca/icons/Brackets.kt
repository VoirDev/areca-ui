package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Braces icon.
 *
 * Converted from Lucide `braces` (ISC).
 */
public val ArecaIcons.Brackets: ImageVector
    get() = cachedBrackets ?: ImageVector.Builder(
        name = "Brackets",
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
            moveTo(8f, 3f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 5f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 3f, 12f)
            arcTo(2f, 2f, 0f, false, true, 5f, 14f)
            verticalLineTo(19f)
            curveTo(5f, 20.1f, 5.9f, 21f, 7f, 21f)
            horizontalLineTo(8f)
            moveTo(16f, 21f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 19f)
            verticalLineTo(14f)
            curveTo(19f, 12.9f, 19.9f, 12f, 21f, 12f)
            arcTo(2f, 2f, 0f, false, true, 19f, 10f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 17f, 3f)
            horizontalLineTo(16f)
        }
    }.build().also { cachedBrackets = it }

/** `Brackets` vector, built on first access and reused afterwards. */
private var cachedBrackets: ImageVector? = null
