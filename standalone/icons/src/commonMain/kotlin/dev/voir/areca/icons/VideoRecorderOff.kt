package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Video off icon.
 *
 * Converted from Lucide `video-off` (ISC).
 */
public val ArecaIcons.VideoRecorderOff: ImageVector
    get() = cachedVideoRecorderOff ?: ImageVector.Builder(
        name = "VideoRecorderOff",
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
            moveTo(10.66f, 6f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 8f)
            verticalLineTo(10.5f)
            lineTo(21.248f, 7.438f)
            arcTo(0.5f, 0.5f, 0f, false, true, 22f, 7.87f)
            verticalLineTo(16.066f)
            moveTo(16f, 16f)
            arcTo(2f, 2f, 0f, false, true, 14f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            horizontalLineTo(6f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedVideoRecorderOff = it }

/** `VideoRecorderOff` vector, built on first access and reused afterwards. */
private var cachedVideoRecorderOff: ImageVector? = null
