package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Redo 2 icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `redo-2` (ISC).
 */
public val ArecaIcons.FlipForward: ImageVector
    get() = cachedFlipForward ?: ImageVector.Builder(
        name = "FlipForward",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(15f, 14f)
            lineTo(20f, 9f)
            lineTo(15f, 4f)
            moveTo(20f, 9f)
            horizontalLineTo(9.5f)
            arcTo(5.5f, 5.5f, 0f, false, false, 4f, 14.5f)
            arcTo(5.5f, 5.5f, 0f, false, false, 9.5f, 20f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedFlipForward = it }

/** `FlipForward` vector, built on first access and reused afterwards. */
private var cachedFlipForward: ImageVector? = null
