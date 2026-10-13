package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message square dot icon.
 *
 * Converted from Lucide `message-square-dot` (ISC).
 */
public val ArecaIcons.MessageNotificationSquare: ImageVector
    get() = cachedMessageNotificationSquare ?: ImageVector.Builder(
        name = "MessageNotificationSquare",
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
            moveTo(12.7f, 3f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 5f)
            verticalLineTo(21.286f)
            arcTo(0.71f, 0.71f, 0f, false, false, 3.212f, 21.788f)
            lineTo(5.414f, 19.586f)
            arcTo(2f, 2f, 0f, false, true, 6.828f, 19f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 17f)
            verticalLineTo(12.3f)
            moveTo(16f, 6f)
            arcTo(3f, 3f, 0f, true, true, 22f, 6f)
            arcTo(3f, 3f, 0f, true, true, 16f, 6f)
            close()
        }
    }.build().also { cachedMessageNotificationSquare = it }

/** `MessageNotificationSquare` vector, built on first access and reused afterwards. */
private var cachedMessageNotificationSquare: ImageVector? = null
