package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shuffle icon.
 *
 * Converted from Lucide `shuffle` (ISC).
 */
public val ArecaIcons.Shuffle: ImageVector
    get() = cachedShuffle ?: ImageVector.Builder(
        name = "Shuffle",
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
            moveTo(18f, 14f)
            lineTo(22f, 18f)
            lineTo(18f, 22f)
            moveTo(18f, 2f)
            lineTo(22f, 6f)
            lineTo(18f, 10f)
            moveTo(2f, 18f)
            horizontalLineTo(3.973f)
            arcTo(4f, 4f, 0f, false, false, 7.273f, 16.3f)
            lineTo(12.727f, 7.7f)
            arcTo(4f, 4f, 0f, false, true, 16.027f, 6f)
            horizontalLineTo(22f)
            moveTo(2f, 6f)
            horizontalLineTo(3.972f)
            arcTo(4f, 4f, 0f, false, true, 7.572f, 8.2f)
            moveTo(22f, 18f)
            horizontalLineTo(15.959f)
            arcTo(4f, 4f, 0f, false, true, 12.659f, 16.2f)
            lineTo(12.3f, 15.75f)
        }
    }.build().also { cachedShuffle = it }

/** `Shuffle` vector, built on first access and reused afterwards. */
private var cachedShuffle: ImageVector? = null
