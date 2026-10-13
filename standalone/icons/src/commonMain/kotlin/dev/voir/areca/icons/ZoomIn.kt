package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Zoom in icon.
 *
 * Converted from Lucide `zoom-in` (ISC).
 */
public val ArecaIcons.ZoomIn: ImageVector
    get() = cachedZoomIn ?: ImageVector.Builder(
        name = "ZoomIn",
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
            moveTo(3f, 11f)
            arcTo(8f, 8f, 0f, true, true, 19f, 11f)
            arcTo(8f, 8f, 0f, true, true, 3f, 11f)
            close()
            moveTo(21f, 21f)
            lineTo(16.65f, 16.65f)
            moveTo(11f, 8f)
            lineTo(11f, 14f)
            moveTo(8f, 11f)
            lineTo(14f, 11f)
        }
    }.build().also { cachedZoomIn = it }

/** `ZoomIn` vector, built on first access and reused afterwards. */
private var cachedZoomIn: ImageVector? = null
