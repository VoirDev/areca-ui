package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Messages square icon.
 *
 * Converted from Lucide `messages-square` (ISC).
 */
public val ArecaIcons.MessageChatSquare: ImageVector
    get() = cachedMessageChatSquare ?: ImageVector.Builder(
        name = "MessageChatSquare",
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
            moveTo(16f, 10f)
            arcTo(2f, 2f, 0f, false, true, 14f, 12f)
            horizontalLineTo(6.828f)
            arcTo(2f, 2f, 0f, false, false, 5.414f, 12.586f)
            lineTo(3.212f, 14.788f)
            arcTo(0.71f, 0.71f, 0f, false, true, 2f, 14.286f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 4f, 2f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 4f)
            close()
            moveTo(20f, 9f)
            arcTo(2f, 2f, 0f, false, true, 22f, 11f)
            verticalLineTo(21.286f)
            arcTo(0.71f, 0.71f, 0f, false, true, 20.788f, 21.788f)
            lineTo(18.586f, 19.586f)
            arcTo(2f, 2f, 0f, false, false, 17.172f, 19f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 8f, 17f)
            verticalLineTo(16f)
        }
    }.build().also { cachedMessageChatSquare = it }

/** `MessageChatSquare` vector, built on first access and reused afterwards. */
private var cachedMessageChatSquare: ImageVector? = null
