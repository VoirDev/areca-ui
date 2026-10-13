package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow out up left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `circle-arrow-out-up-left` (ISC).
 */
public val ArecaIcons.ArrowCircleBrokenUpLeft: ImageVector
    get() = cachedArrowCircleBrokenUpLeft ?: ImageVector.Builder(
        name = "ArrowCircleBrokenUpLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(2f, 8f)
            verticalLineTo(2f)
            horizontalLineTo(8f)
            moveTo(2f, 2f)
            lineTo(12f, 12f)
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
        }
    }.build().also { cachedArrowCircleBrokenUpLeft = it }

/** `ArrowCircleBrokenUpLeft` vector, built on first access and reused afterwards. */
private var cachedArrowCircleBrokenUpLeft: ImageVector? = null
