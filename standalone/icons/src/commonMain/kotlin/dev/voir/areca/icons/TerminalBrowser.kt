package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Terminal 2 icon.
 *
 * Converted from Tabler Icons `terminal-2` (MIT).
 */
public val ArecaIcons.TerminalBrowser: ImageVector
    get() = cachedTerminalBrowser ?: ImageVector.Builder(
        name = "TerminalBrowser",
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
            moveTo(8f, 9f)
            lineTo(11f, 12f)
            lineTo(8f, 15f)
            moveTo(13f, 15f)
            lineTo(16f, 15f)
            moveTo(3f, 6f)
            arcTo(2f, 2f, 0f, false, true, 5f, 4f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 19f, 20f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 18f)
            lineTo(3f, 6f)
        }
    }.build().also { cachedTerminalBrowser = it }

/** `TerminalBrowser` vector, built on first access and reused afterwards. */
private var cachedTerminalBrowser: ImageVector? = null
