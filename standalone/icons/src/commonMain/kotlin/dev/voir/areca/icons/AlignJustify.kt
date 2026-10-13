package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Text align justify icon.
 *
 * Converted from Lucide `text-align-justify` (ISC).
 */
public val ArecaIcons.AlignJustify: ImageVector
    get() = cachedAlignJustify ?: ImageVector.Builder(
        name = "AlignJustify",
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
            moveTo(3f, 5f)
            horizontalLineTo(21f)
            moveTo(3f, 12f)
            horizontalLineTo(21f)
            moveTo(3f, 19f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedAlignJustify = it }

/** `AlignJustify` vector, built on first access and reused afterwards. */
private var cachedAlignJustify: ImageVector? = null
