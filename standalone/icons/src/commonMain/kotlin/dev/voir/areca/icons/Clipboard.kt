package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clipboard icon.
 *
 * Converted from Lucide `clipboard` (ISC).
 */
public val ArecaIcons.Clipboard: ImageVector
    get() = cachedClipboard ?: ImageVector.Builder(
        name = "Clipboard",
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
            moveTo(9f, 2f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 16f, 3f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 15f, 6f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 8f, 5f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 9f, 2f)
            close()
            moveTo(16f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(8f)
        }
    }.build().also { cachedClipboard = it }

/** `Clipboard` vector, built on first access and reused afterwards. */
private var cachedClipboard: ImageVector? = null
