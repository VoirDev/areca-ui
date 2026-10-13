package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square terminal icon.
 *
 * Converted from Lucide `square-terminal` (ISC).
 */
public val ArecaIcons.TerminalSquare: ImageVector
    get() = cachedTerminalSquare ?: ImageVector.Builder(
        name = "TerminalSquare",
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
            moveTo(7f, 11f)
            lineTo(9f, 9f)
            lineTo(7f, 7f)
            moveTo(11f, 13f)
            horizontalLineTo(15f)
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
        }
    }.build().also { cachedTerminalSquare = it }

/** `TerminalSquare` vector, built on first access and reused afterwards. */
private var cachedTerminalSquare: ImageVector? = null
