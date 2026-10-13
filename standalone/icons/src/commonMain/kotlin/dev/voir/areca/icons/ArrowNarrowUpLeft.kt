package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move up left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-up-left` (ISC).
 */
public val ArecaIcons.ArrowNarrowUpLeft: ImageVector
    get() = cachedArrowNarrowUpLeft ?: ImageVector.Builder(
        name = "ArrowNarrowUpLeft",
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
            moveTo(5f, 11f)
            verticalLineTo(5f)
            horizontalLineTo(11f)
            moveTo(5f, 5f)
            lineTo(19f, 19f)
        }
    }.build().also { cachedArrowNarrowUpLeft = it }

/** `ArrowNarrowUpLeft` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowUpLeft: ImageVector? = null
