package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File question mark icon.
 *
 * Converted from Lucide `file-question-mark` (ISC).
 */
public val ArecaIcons.FileQuestion: ImageVector
    get() = cachedFileQuestion ?: ImageVector.Builder(
        name = "FileQuestion",
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
            moveTo(6f, 22f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            horizontalLineTo(14f)
            arcTo(2.4f, 2.4f, 0f, false, true, 15.704f, 2.706f)
            lineTo(19.292f, 6.294f)
            arcTo(2.4f, 2.4f, 0f, false, true, 20f, 8f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            close()
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
            moveTo(9.1f, 9f)
            arcTo(3f, 3f, 0f, false, true, 14.92f, 10f)
            curveTo(14.92f, 12f, 11.92f, 13f, 11.92f, 13f)
        }
    }.build().also { cachedFileQuestion = it }

/** `FileQuestion` vector, built on first access and reused afterwards. */
private var cachedFileQuestion: ImageVector? = null
