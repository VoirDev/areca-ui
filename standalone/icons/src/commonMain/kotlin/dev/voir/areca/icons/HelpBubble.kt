package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message circle question mark icon.
 *
 * Converted from Lucide `message-circle-question-mark` (ISC).
 */
public val ArecaIcons.HelpBubble: ImageVector
    get() = cachedHelpBubble ?: ImageVector.Builder(
        name = "HelpBubble",
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
            moveTo(2.992f, 16.342f)
            arcTo(2f, 2f, 0f, false, true, 3.086f, 17.509f)
            lineTo(2.021f, 20.799f)
            arcTo(1f, 1f, 0f, false, false, 3.257f, 21.967f)
            lineTo(6.67f, 20.969f)
            arcTo(2f, 2f, 0f, false, true, 7.769f, 21.061f)
            arcTo(10f, 10f, 0f, true, false, 2.992f, 16.342f)
            moveTo(9.09f, 9f)
            arcTo(3f, 3f, 0f, false, true, 14.92f, 10f)
            curveTo(14.92f, 12f, 11.92f, 13f, 11.92f, 13f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedHelpBubble = it }

/** `HelpBubble` vector, built on first access and reused afterwards. */
private var cachedHelpBubble: ImageVector? = null
