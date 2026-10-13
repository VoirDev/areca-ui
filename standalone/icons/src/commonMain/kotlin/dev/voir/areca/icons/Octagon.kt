package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Octagon icon.
 *
 * Converted from Lucide `octagon` (ISC).
 */
public val ArecaIcons.Octagon: ImageVector
    get() = cachedOctagon ?: ImageVector.Builder(
        name = "Octagon",
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
            moveTo(2.586f, 16.726f)
            arcTo(2f, 2f, 0f, false, true, 2f, 15.312f)
            verticalLineTo(8.688f)
            arcTo(2f, 2f, 0f, false, true, 2.586f, 7.274f)
            lineTo(7.274f, 2.586f)
            arcTo(2f, 2f, 0f, false, true, 8.688f, 2f)
            horizontalLineTo(15.312f)
            arcTo(2f, 2f, 0f, false, true, 16.726f, 2.586f)
            lineTo(21.414f, 7.274f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8.688f)
            verticalLineTo(15.312f)
            arcTo(2f, 2f, 0f, false, true, 21.414f, 16.726f)
            lineTo(16.726f, 21.414f)
            arcTo(2f, 2f, 0f, false, true, 15.312f, 22f)
            horizontalLineTo(8.688f)
            arcTo(2f, 2f, 0f, false, true, 7.274f, 21.414f)
            close()
        }
    }.build().also { cachedOctagon = it }

/** `Octagon` vector, built on first access and reused afterwards. */
private var cachedOctagon: ImageVector? = null
