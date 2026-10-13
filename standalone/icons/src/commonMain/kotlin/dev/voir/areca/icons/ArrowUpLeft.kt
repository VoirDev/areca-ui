package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow up left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-up-left` (ISC).
 */
public val ArecaIcons.ArrowUpLeft: ImageVector
    get() = cachedArrowUpLeft ?: ImageVector.Builder(
        name = "ArrowUpLeft",
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
            moveTo(7f, 17f)
            verticalLineTo(7f)
            horizontalLineTo(17f)
            moveTo(17f, 17f)
            lineTo(7f, 7f)
        }
    }.build().also { cachedArrowUpLeft = it }

/** `ArrowUpLeft` vector, built on first access and reused afterwards. */
private var cachedArrowUpLeft: ImageVector? = null
