package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message question icon.
 *
 * Converted from Tabler Icons `message-question` (MIT).
 */
public val ArecaIcons.MessageQuestionSquare: ImageVector
    get() = cachedMessageQuestionSquare ?: ImageVector.Builder(
        name = "MessageQuestionSquare",
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
            moveTo(14f, 18f)
            horizontalLineTo(13f)
            lineTo(8f, 21f)
            verticalLineTo(18f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 15f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 7f)
            verticalLineTo(11.5f)
            moveTo(19f, 22f)
            verticalLineTo(22.01f)
            moveTo(19f, 19f)
            arcTo(2.003f, 2.003f, 0f, false, false, 19.914f, 15.218f)
            arcTo(1.98f, 1.98f, 0f, false, false, 17.5f, 15.701f)
        }
    }.build().also { cachedMessageQuestionSquare = it }

/** `MessageQuestionSquare` vector, built on first access and reused afterwards. */
private var cachedMessageQuestionSquare: ImageVector? = null
