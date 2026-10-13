package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Frame icon.
 *
 * Converted from Lucide `frame` (ISC).
 */
public val ArecaIcons.Frame: ImageVector
    get() = cachedFrame ?: ImageVector.Builder(
        name = "Frame",
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
            moveTo(22f, 6f)
            lineTo(2f, 6f)
            moveTo(22f, 18f)
            lineTo(2f, 18f)
            moveTo(6f, 2f)
            lineTo(6f, 22f)
            moveTo(18f, 2f)
            lineTo(18f, 22f)
        }
    }.build().also { cachedFrame = it }

/** `Frame` vector, built on first access and reused afterwards. */
private var cachedFrame: ImageVector? = null
