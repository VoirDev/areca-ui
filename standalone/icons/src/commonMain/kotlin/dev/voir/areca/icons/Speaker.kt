package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Speaker icon.
 *
 * Converted from Lucide `speaker` (ISC).
 */
public val ArecaIcons.Speaker: ImageVector
    get() = cachedSpeaker ?: ImageVector.Builder(
        name = "Speaker",
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
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
            moveTo(12f, 6f)
            horizontalLineTo(12.01f)
            moveTo(8f, 14f)
            arcTo(4f, 4f, 0f, true, true, 16f, 14f)
            arcTo(4f, 4f, 0f, true, true, 8f, 14f)
            close()
            moveTo(12f, 14f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedSpeaker = it }

/** `Speaker` vector, built on first access and reused afterwards. */
private var cachedSpeaker: ImageVector? = null
