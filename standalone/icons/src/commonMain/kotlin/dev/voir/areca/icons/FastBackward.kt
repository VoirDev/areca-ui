package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rewind icon.
 *
 * Converted from Lucide `rewind` (ISC).
 */
public val ArecaIcons.FastBackward: ImageVector
    get() = cachedFastBackward ?: ImageVector.Builder(
        name = "FastBackward",
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
            moveTo(12f, 6f)
            arcTo(2f, 2f, 0f, false, false, 8.586f, 4.586f)
            lineTo(2.586f, 10.586f)
            arcTo(2f, 2f, 0f, false, false, 2.586f, 13.414f)
            lineTo(8.586f, 19.414f)
            arcTo(2f, 2f, 0f, false, false, 12f, 18f)
            close()
            moveTo(22f, 6f)
            arcTo(2f, 2f, 0f, false, false, 18.586f, 4.586f)
            lineTo(12.586f, 10.586f)
            arcTo(2f, 2f, 0f, false, false, 12.586f, 13.414f)
            lineTo(18.586f, 19.414f)
            arcTo(2f, 2f, 0f, false, false, 22f, 18f)
            close()
        }
    }.build().also { cachedFastBackward = it }

/** `FastBackward` vector, built on first access and reused afterwards. */
private var cachedFastBackward: ImageVector? = null
