package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Play icon.
 *
 * Converted from Lucide `play` (ISC).
 */
public val ArecaIcons.Play: ImageVector
    get() = cachedPlay ?: ImageVector.Builder(
        name = "Play",
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
            moveTo(5f, 5f)
            arcTo(2f, 2f, 0f, false, true, 8.008f, 3.272f)
            lineTo(20.005f, 10.27f)
            arcTo(2f, 2f, 0f, false, true, 20.008f, 13.728f)
            lineTo(8.008f, 20.728f)
            arcTo(2f, 2f, 0f, false, true, 5f, 19f)
            close()
        }
    }.build().also { cachedPlay = it }

/** `Play` vector, built on first access and reused afterwards. */
private var cachedPlay: ImageVector? = null
