package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paper bag icon.
 *
 * Converted from Lucide `paper-bag` (ISC).
 */
public val ArecaIcons.PaperBag: ImageVector
    get() = cachedPaperBag ?: ImageVector.Builder(
        name = "PaperBag",
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
            moveTo(5.364f, 3.848f)
            curveTo(4f, 6f, 3f, 9.652f, 3f, 12.652f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            verticalLineTo(14f)
            curveTo(21f, 11.666f, 19.184f, 9.332f, 18.378f, 6.998f)
            moveTo(7f, 3f)
            horizontalLineTo(18.379f)
            arcTo(2f, 2f, 0f, false, true, 20.168f, 4.106f)
            lineTo(20.891f, 5.553f)
            arcTo(1f, 1f, 0f, false, true, 19.997f, 7f)
            horizontalLineTo(11.472f)
            arcTo(2f, 2f, 0f, false, true, 9.683f, 5.894f)
            lineTo(8.79f, 4.105f)
            arcTo(2f, 2f, 0f, true, false, 5.211f, 5.894f)
            lineTo(7.472f, 10.416f)
            arcTo(5f, 5f, 0f, false, true, 8f, 12.652f)
            verticalLineTo(21f)
        }
    }.build().also { cachedPaperBag = it }

/** `PaperBag` vector, built on first access and reused afterwards. */
private var cachedPaperBag: ImageVector? = null
