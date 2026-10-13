package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paperclip icon.
 *
 * Converted from Lucide `paperclip` (ISC).
 */
public val ArecaIcons.Paperclip: ImageVector
    get() = cachedPaperclip ?: ImageVector.Builder(
        name = "Paperclip",
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
            moveTo(16f, 6f)
            lineTo(7.586f, 14.586f)
            arcTo(2f, 2f, 0f, false, false, 10.415f, 17.415f)
            lineTo(18.829f, 8.829f)
            arcTo(4f, 4f, 0f, true, false, 13.172f, 3.172f)
            lineTo(4.793f, 11.723f)
            arcTo(6f, 6f, 0f, true, false, 13.278f, 20.208f)
            lineTo(21.657f, 11.657f)
        }
    }.build().also { cachedPaperclip = it }

/** `Paperclip` vector, built on first access and reused afterwards. */
private var cachedPaperclip: ImageVector? = null
