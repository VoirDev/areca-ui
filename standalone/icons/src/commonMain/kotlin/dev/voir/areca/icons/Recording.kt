package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Audio lines icon.
 *
 * Converted from Lucide `audio-lines` (ISC).
 */
public val ArecaIcons.Recording: ImageVector
    get() = cachedRecording ?: ImageVector.Builder(
        name = "Recording",
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
            moveTo(2f, 10f)
            verticalLineTo(13f)
            moveTo(6f, 6f)
            verticalLineTo(17f)
            moveTo(10f, 3f)
            verticalLineTo(21f)
            moveTo(14f, 8f)
            verticalLineTo(15f)
            moveTo(18f, 5f)
            verticalLineTo(18f)
            moveTo(22f, 10f)
            verticalLineTo(13f)
        }
    }.build().also { cachedRecording = it }

/** `Recording` vector, built on first access and reused afterwards. */
private var cachedRecording: ImageVector? = null
