package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Expand icon.
 *
 * Converted from Lucide `expand` (ISC).
 */
public val ArecaIcons.ExpandSquare: ImageVector
    get() = cachedExpandSquare ?: ImageVector.Builder(
        name = "ExpandSquare",
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
            moveTo(15f, 15f)
            lineTo(21f, 21f)
            moveTo(15f, 9f)
            lineTo(21f, 3f)
            moveTo(21f, 16f)
            verticalLineTo(21f)
            horizontalLineTo(16f)
            moveTo(21f, 8f)
            verticalLineTo(3f)
            horizontalLineTo(16f)
            moveTo(3f, 16f)
            verticalLineTo(21f)
            horizontalLineTo(8f)
            moveTo(3f, 21f)
            lineTo(9f, 15f)
            moveTo(3f, 8f)
            verticalLineTo(3f)
            horizontalLineTo(8f)
            moveTo(9f, 9f)
            lineTo(3f, 3f)
        }
    }.build().also { cachedExpandSquare = it }

/** `ExpandSquare` vector, built on first access and reused afterwards. */
private var cachedExpandSquare: ImageVector? = null
