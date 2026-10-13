package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message square heart icon.
 *
 * Converted from Lucide `message-square-heart` (ISC).
 */
public val ArecaIcons.MessageHeartSquare: ImageVector
    get() = cachedMessageHeartSquare ?: ImageVector.Builder(
        name = "MessageHeartSquare",
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
            moveTo(7.5f, 9.5f)
            curveTo(7.5f, 10.187f, 7.765f, 10.883f, 8.197f, 11.344f)
            lineTo(11.206f, 14.608f)
            arcTo(1.14f, 1.14f, 0f, false, false, 11.613f, 14.922f)
            arcTo(1f, 1f, 0f, false, false, 12.396f, 14.918f)
            arcTo(1.14f, 1.14f, 0f, false, false, 12.794f, 14.608f)
            lineTo(15.802f, 11.344f)
            arcTo(2.77f, 2.77f, 0f, false, false, 16.5f, 9.5f)
            arcTo(2.5f, 2.5f, 0f, false, false, 12f, 8f)
            arcTo(2.5f, 2.5f, 0f, false, false, 7.5f, 9.5f)
        }
    }.build().also { cachedMessageHeartSquare = it }

/** `MessageHeartSquare` vector, built on first access and reused afterwards. */
private var cachedMessageHeartSquare: ImageVector? = null
