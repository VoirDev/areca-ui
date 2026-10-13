package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message circle heart icon.
 *
 * Converted from Lucide `message-circle-heart` (ISC).
 */
public val ArecaIcons.MessageHeartCircle: ImageVector
    get() = cachedMessageHeartCircle ?: ImageVector.Builder(
        name = "MessageHeartCircle",
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
            moveTo(7.828f, 13.07f)
            arcTo(3f, 3f, 0f, false, true, 12f, 8.764f)
            arcTo(3f, 3f, 0f, false, true, 17.004f, 10.988f)
            arcTo(3f, 3f, 0f, false, true, 16.172f, 13.071f)
            lineTo(12.725f, 16.691f)
            arcTo(1f, 1f, 0f, false, true, 11.275f, 16.69f)
            close()
        }
    }.build().also { cachedMessageHeartCircle = it }

/** `MessageHeartCircle` vector, built on first access and reused afterwards. */
private var cachedMessageHeartCircle: ImageVector? = null
