package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move down right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-down-right` (ISC).
 */
public val ArecaIcons.ArrowNarrowDownRight: ImageVector
    get() = cachedArrowNarrowDownRight ?: ImageVector.Builder(
        name = "ArrowNarrowDownRight",
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
            moveTo(19f, 13f)
            verticalLineTo(19f)
            horizontalLineTo(13f)
            moveTo(5f, 5f)
            lineTo(19f, 19f)
        }
    }.build().also { cachedArrowNarrowDownRight = it }

/** `ArrowNarrowDownRight` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowDownRight: ImageVector? = null
