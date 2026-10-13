package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mic off icon.
 *
 * Converted from Lucide `mic-off` (ISC).
 */
public val ArecaIcons.MicrophoneOff: ImageVector
    get() = cachedMicrophoneOff ?: ImageVector.Builder(
        name = "MicrophoneOff",
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
            moveTo(12f, 19f)
            verticalLineTo(22f)
            moveTo(15f, 9.34f)
            verticalLineTo(5f)
            arcTo(3f, 3f, 0f, false, false, 9.32f, 3.67f)
            moveTo(16.95f, 16.95f)
            arcTo(7f, 7f, 0f, false, true, 5f, 12f)
            verticalLineTo(10f)
            moveTo(18.89f, 13.23f)
            arcTo(7f, 7f, 0f, false, false, 19f, 12f)
            verticalLineTo(10f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(9f, 9f)
            verticalLineTo(12f)
            arcTo(3f, 3f, 0f, false, false, 14.12f, 14.12f)
        }
    }.build().also { cachedMicrophoneOff = it }

/** `MicrophoneOff` vector, built on first access and reused afterwards. */
private var cachedMicrophoneOff: ImageVector? = null
