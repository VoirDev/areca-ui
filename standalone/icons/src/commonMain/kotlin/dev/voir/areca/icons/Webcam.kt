package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Webcam icon.
 *
 * Converted from Lucide `webcam` (ISC).
 */
public val ArecaIcons.Webcam: ImageVector
    get() = cachedWebcam ?: ImageVector.Builder(
        name = "Webcam",
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
            moveTo(4f, 10f)
            arcTo(8f, 8f, 0f, true, true, 20f, 10f)
            arcTo(8f, 8f, 0f, true, true, 4f, 10f)
            close()
            moveTo(9f, 10f)
            arcTo(3f, 3f, 0f, true, true, 15f, 10f)
            arcTo(3f, 3f, 0f, true, true, 9f, 10f)
            close()
            moveTo(7f, 22f)
            horizontalLineTo(17f)
            moveTo(12f, 22f)
            verticalLineTo(18f)
        }
    }.build().also { cachedWebcam = it }

/** `Webcam` vector, built on first access and reused afterwards. */
private var cachedWebcam: ImageVector? = null
