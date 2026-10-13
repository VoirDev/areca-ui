package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Text wrap icon.
 *
 * Converted from Lucide `text-wrap` (ISC).
 */
public val ArecaIcons.ParagraphWrap: ImageVector
    get() = cachedParagraphWrap ?: ImageVector.Builder(
        name = "ParagraphWrap",
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
            moveTo(16f, 16f)
            lineTo(13f, 19f)
            lineTo(16f, 22f)
            moveTo(3f, 12f)
            horizontalLineTo(17.5f)
            arcTo(1f, 1f, 0f, false, true, 17.5f, 19f)
            horizontalLineTo(13f)
            moveTo(3f, 19f)
            horizontalLineTo(9f)
            moveTo(3f, 5f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedParagraphWrap = it }

/** `ParagraphWrap` vector, built on first access and reused afterwards. */
private var cachedParagraphWrap: ImageVector? = null
