package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner up left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-up-left` (ISC).
 */
public val ArecaIcons.CornerUpLeft: ImageVector
    get() = cachedCornerUpLeft ?: ImageVector.Builder(
        name = "CornerUpLeft",
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
            moveTo(20f, 20f)
            verticalLineTo(13f)
            arcTo(4f, 4f, 0f, false, false, 16f, 9f)
            horizontalLineTo(4f)
            moveTo(9f, 14f)
            lineTo(4f, 9f)
            lineTo(9f, 4f)
        }
    }.build().also { cachedCornerUpLeft = it }

/** `CornerUpLeft` vector, built on first access and reused afterwards. */
private var cachedCornerUpLeft: ImageVector? = null
