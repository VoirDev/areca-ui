package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move icon.
 *
 * Converted from Lucide `move` (ISC).
 */
public val ArecaIcons.Move: ImageVector
    get() = cachedMove ?: ImageVector.Builder(
        name = "Move",
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
            moveTo(12f, 2f)
            verticalLineTo(22f)
            moveTo(15f, 19f)
            lineTo(12f, 22f)
            lineTo(9f, 19f)
            moveTo(19f, 9f)
            lineTo(22f, 12f)
            lineTo(19f, 15f)
            moveTo(2f, 12f)
            horizontalLineTo(22f)
            moveTo(5f, 9f)
            lineTo(2f, 12f)
            lineTo(5f, 15f)
            moveTo(9f, 5f)
            lineTo(12f, 2f)
            lineTo(15f, 5f)
        }
    }.build().also { cachedMove = it }

/** `Move` vector, built on first access and reused afterwards. */
private var cachedMove: ImageVector? = null
