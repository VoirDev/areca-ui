package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Messages circle icon.
 *
 * Converted from Lucide `messages-circle` (ISC).
 */
public val ArecaIcons.MessageChatCircle: ImageVector
    get() = cachedMessageChatCircle ?: ImageVector.Builder(
        name = "MessageChatCircle",
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
            moveTo(19.95f, 10.05f)
            arcTo(7f, 7f, 0f, false, true, 21.362f, 17.922f)
            arcTo(1f, 1f, 0f, false, false, 21.304f, 18.709f)
            lineTo(21.979f, 20.798f)
            arcTo(1f, 1f, 0f, false, true, 20.743f, 21.966f)
            lineTo(18.588f, 21.335f)
            arcTo(1f, 1f, 0f, false, false, 17.843f, 21.395f)
            arcTo(7f, 7f, 0f, false, true, 10.05f, 19.95f)
            moveTo(2.696f, 12.708f)
            arcTo(1f, 1f, 0f, false, false, 2.638f, 11.923f)
            arcTo(7f, 7f, 0f, true, true, 6.156f, 15.396f)
            arcTo(1f, 1f, 0f, false, false, 5.412f, 15.335f)
            lineTo(3.257f, 15.965f)
            arcTo(1f, 1f, 0f, false, true, 2.021f, 14.798f)
            close()
        }
    }.build().also { cachedMessageChatCircle = it }

/** `MessageChatCircle` vector, built on first access and reused afterwards. */
private var cachedMessageChatCircle: ImageVector? = null
