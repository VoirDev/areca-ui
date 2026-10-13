package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message square plus icon.
 *
 * Converted from Lucide `message-square-plus` (ISC).
 */
public val ArecaIcons.MessagePlusSquare: ImageVector
    get() = cachedMessagePlusSquare ?: ImageVector.Builder(
        name = "MessagePlusSquare",
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
            moveTo(22f, 17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(6.828f)
            arcTo(2f, 2f, 0f, false, false, 5.414f, 19.586f)
            lineTo(3.212f, 21.788f)
            arcTo(0.71f, 0.71f, 0f, false, true, 2f, 21.286f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            close()
            moveTo(12f, 8f)
            verticalLineTo(14f)
            moveTo(9f, 11f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedMessagePlusSquare = it }

/** `MessagePlusSquare` vector, built on first access and reused afterwards. */
private var cachedMessagePlusSquare: ImageVector? = null
