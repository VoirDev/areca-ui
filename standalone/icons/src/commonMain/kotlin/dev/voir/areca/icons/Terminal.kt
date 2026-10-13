package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Terminal icon.
 *
 * Converted from Lucide `terminal` (ISC).
 */
public val ArecaIcons.Terminal: ImageVector
    get() = cachedTerminal ?: ImageVector.Builder(
        name = "Terminal",
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
            moveTo(12f, 19f)
            horizontalLineTo(20f)
            moveTo(4f, 17f)
            lineTo(10f, 11f)
            lineTo(4f, 5f)
        }
    }.build().also { cachedTerminal = it }

/** `Terminal` vector, built on first access and reused afterwards. */
private var cachedTerminal: ImageVector? = null
