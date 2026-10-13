package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Video icon.
 *
 * Converted from Lucide `video` (ISC).
 */
public val ArecaIcons.Video: ImageVector
    get() = cachedVideo ?: ImageVector.Builder(
        name = "Video",
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
            moveTo(16f, 13f)
            lineTo(21.223f, 16.482f)
            arcTo(0.5f, 0.5f, 0f, false, false, 22f, 16.066f)
            verticalLineTo(7.87f)
            arcTo(0.5f, 0.5f, 0f, false, false, 21.248f, 7.438f)
            lineTo(16f, 10.5f)
            moveTo(4f, 6f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 14f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            close()
        }
    }.build().also { cachedVideo = it }

/** `Video` vector, built on first access and reused afterwards. */
private var cachedVideo: ImageVector? = null
