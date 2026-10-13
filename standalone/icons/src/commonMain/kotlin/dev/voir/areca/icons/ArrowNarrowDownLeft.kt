package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move down left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-down-left` (ISC).
 */
public val ArecaIcons.ArrowNarrowDownLeft: ImageVector
    get() = cachedArrowNarrowDownLeft ?: ImageVector.Builder(
        name = "ArrowNarrowDownLeft",
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
            moveTo(11f, 19f)
            horizontalLineTo(5f)
            verticalLineTo(13f)
            moveTo(19f, 5f)
            lineTo(5f, 19f)
        }
    }.build().also { cachedArrowNarrowDownLeft = it }

/** `ArrowNarrowDownLeft` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowDownLeft: ImageVector? = null
