package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Text align end icon.
 *
 * Converted from Lucide `text-align-end` (ISC).
 */
public val ArecaIcons.AlignRight: ImageVector
    get() = cachedAlignRight ?: ImageVector.Builder(
        name = "AlignRight",
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
            moveTo(21f, 5f)
            horizontalLineTo(3f)
            moveTo(21f, 12f)
            horizontalLineTo(9f)
            moveTo(21f, 19f)
            horizontalLineTo(7f)
        }
    }.build().also { cachedAlignRight = it }

/** `AlignRight` vector, built on first access and reused afterwards. */
private var cachedAlignRight: ImageVector? = null
