package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Italic icon.
 *
 * Converted from Lucide `italic` (ISC).
 */
public val ArecaIcons.Italic: ImageVector
    get() = cachedItalic ?: ImageVector.Builder(
        name = "Italic",
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
            moveTo(19f, 4f)
            lineTo(10f, 4f)
            moveTo(14f, 20f)
            lineTo(5f, 20f)
            moveTo(15f, 4f)
            lineTo(9f, 20f)
        }
    }.build().also { cachedItalic = it }

/** `Italic` vector, built on first access and reused afterwards. */
private var cachedItalic: ImageVector? = null
