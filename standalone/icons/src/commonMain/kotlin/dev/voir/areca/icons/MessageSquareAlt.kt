package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message icon.
 *
 * Converted from Tabler Icons `message` (MIT).
 */
public val ArecaIcons.MessageSquareAlt: ImageVector
    get() = cachedMessageSquareAlt ?: ImageVector.Builder(
        name = "MessageSquareAlt",
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
            moveTo(8f, 9f)
            horizontalLineTo(16f)
            moveTo(8f, 13f)
            horizontalLineTo(14f)
            moveTo(18f, 4f)
            arcTo(3f, 3f, 0f, false, true, 21f, 7f)
            verticalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 18f, 18f)
            horizontalLineTo(13f)
            lineTo(8f, 21f)
            verticalLineTo(18f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 15f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
        }
    }.build().also { cachedMessageSquareAlt = it }

/** `MessageSquareAlt` vector, built on first access and reused afterwards. */
private var cachedMessageSquareAlt: ImageVector? = null
