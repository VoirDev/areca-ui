package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mouse pointer icon.
 *
 * Converted from Lucide `mouse-pointer` (ISC).
 */
public val ArecaIcons.CursorAlt: ImageVector
    get() = cachedCursorAlt ?: ImageVector.Builder(
        name = "CursorAlt",
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
            moveTo(12.586f, 12.586f)
            lineTo(19f, 19f)
            moveTo(3.688f, 3.037f)
            arcTo(0.497f, 0.497f, 0f, false, false, 3.037f, 3.688f)
            lineTo(9.537f, 19.687f)
            arcTo(0.501f, 0.501f, 0f, false, false, 10.484f, 19.625f)
            lineTo(12.053f, 13.542f)
            arcTo(2f, 2f, 0f, false, true, 13.501f, 12.063f)
            lineTo(19.625f, 10.484f)
            arcTo(0.5f, 0.5f, 0f, false, false, 19.688f, 9.537f)
            close()
        }
    }.build().also { cachedCursorAlt = it }

/** `CursorAlt` vector, built on first access and reused afterwards. */
private var cachedCursorAlt: ImageVector? = null
