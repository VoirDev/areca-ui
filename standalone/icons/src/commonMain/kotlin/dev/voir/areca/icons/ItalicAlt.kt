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
 * Converted from Tabler Icons `italic` (MIT).
 */
public val ArecaIcons.ItalicAlt: ImageVector
    get() = cachedItalicAlt ?: ImageVector.Builder(
        name = "ItalicAlt",
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
            moveTo(11f, 5f)
            lineTo(17f, 5f)
            moveTo(7f, 19f)
            lineTo(13f, 19f)
            moveTo(14f, 5f)
            lineTo(10f, 19f)
        }
    }.build().also { cachedItalicAlt = it }

/** `ItalicAlt` vector, built on first access and reused afterwards. */
private var cachedItalicAlt: ImageVector? = null
