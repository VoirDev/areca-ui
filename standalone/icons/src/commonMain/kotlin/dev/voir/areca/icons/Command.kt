package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Command icon.
 *
 * Converted from Lucide `command` (ISC).
 */
public val ArecaIcons.Command: ImageVector
    get() = cachedCommand ?: ImageVector.Builder(
        name = "Command",
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
            moveTo(15f, 6f)
            verticalLineTo(18f)
            arcTo(3f, 3f, 0f, true, false, 18f, 15f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, true, false, 9f, 18f)
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, true, false, 6f, 9f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, true, false, 15f, 6f)
        }
    }.build().also { cachedCommand = it }

/** `Command` vector, built on first access and reused afterwards. */
private var cachedCommand: ImageVector? = null
