package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move up right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-up-right` (ISC).
 */
public val ArecaIcons.ArrowNarrowUpRight: ImageVector
    get() = cachedArrowNarrowUpRight ?: ImageVector.Builder(
        name = "ArrowNarrowUpRight",
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
            moveTo(13f, 5f)
            horizontalLineTo(19f)
            verticalLineTo(11f)
            moveTo(19f, 5f)
            lineTo(5f, 19f)
        }
    }.build().also { cachedArrowNarrowUpRight = it }

/** `ArrowNarrowUpRight` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowUpRight: ImageVector? = null
