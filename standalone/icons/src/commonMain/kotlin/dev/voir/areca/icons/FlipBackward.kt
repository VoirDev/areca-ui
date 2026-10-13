package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Undo 2 icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `undo-2` (ISC).
 */
public val ArecaIcons.FlipBackward: ImageVector
    get() = cachedFlipBackward ?: ImageVector.Builder(
        name = "FlipBackward",
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
            moveTo(9f, 14f)
            lineTo(4f, 9f)
            lineTo(9f, 4f)
            moveTo(4f, 9f)
            horizontalLineTo(14.5f)
            arcTo(5.5f, 5.5f, 0f, false, true, 20f, 14.5f)
            arcTo(5.5f, 5.5f, 0f, false, true, 14.5f, 20f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedFlipBackward = it }

/** `FlipBackward` vector, built on first access and reused afterwards. */
private var cachedFlipBackward: ImageVector? = null
