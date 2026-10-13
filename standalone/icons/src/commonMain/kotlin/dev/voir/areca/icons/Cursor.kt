package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mouse pointer 2 icon.
 *
 * Converted from Lucide `mouse-pointer-2` (ISC).
 */
public val ArecaIcons.Cursor: ImageVector
    get() = cachedCursor ?: ImageVector.Builder(
        name = "Cursor",
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
            moveTo(4.037f, 4.688f)
            arcTo(0.495f, 0.495f, 0f, false, true, 4.688f, 4.037f)
            lineTo(20.688f, 10.537f)
            arcTo(0.5f, 0.5f, 0f, false, true, 20.625f, 11.484f)
            lineTo(14.501f, 13.064f)
            arcTo(2f, 2f, 0f, false, false, 13.063f, 14.499f)
            lineTo(11.484f, 20.625f)
            arcTo(0.5f, 0.5f, 0f, false, true, 10.537f, 20.688f)
            close()
        }
    }.build().also { cachedCursor = it }

/** `Cursor` vector, built on first access and reused afterwards. */
private var cachedCursor: ImageVector? = null
