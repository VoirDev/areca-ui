package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square arrow down icon.
 *
 * Converted from Lucide `square-arrow-down` (ISC).
 */
public val ArecaIcons.ArrowSquareDown: ImageVector
    get() = cachedArrowSquareDown ?: ImageVector.Builder(
        name = "ArrowSquareDown",
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
            moveTo(12f, 8f)
            verticalLineTo(16f)
            moveTo(8f, 12f)
            lineTo(12f, 16f)
            lineTo(16f, 12f)
        }
    }.build().also { cachedArrowSquareDown = it }

/** `ArrowSquareDown` vector, built on first access and reused afterwards. */
private var cachedArrowSquareDown: ImageVector? = null
