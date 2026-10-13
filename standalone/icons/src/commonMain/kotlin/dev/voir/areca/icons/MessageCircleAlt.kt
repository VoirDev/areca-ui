package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message circle icon.
 *
 * Converted from Tabler Icons `message-circle` (MIT).
 */
public val ArecaIcons.MessageCircleAlt: ImageVector
    get() = cachedMessageCircleAlt ?: ImageVector.Builder(
        name = "MessageCircleAlt",
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
            moveTo(3f, 20f)
            lineTo(4.3f, 16.1f)
            curveTo(1.976f, 12.663f, 2.874f, 8.228f, 6.4f, 5.726f)
            curveTo(9.926f, 3.225f, 14.99f, 3.43f, 18.245f, 6.206f)
            curveTo(21.5f, 8.983f, 21.94f, 13.472f, 19.274f, 16.707f)
            curveTo(16.608f, 19.942f, 11.659f, 20.922f, 7.7f, 19f)
            lineTo(3f, 20f)
        }
    }.build().also { cachedMessageCircleAlt = it }

/** `MessageCircleAlt` vector, built on first access and reused afterwards. */
private var cachedMessageCircleAlt: ImageVector? = null
