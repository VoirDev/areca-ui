package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fast forward icon.
 *
 * Converted from Lucide `fast-forward` (ISC).
 */
public val ArecaIcons.FastForward: ImageVector
    get() = cachedFastForward ?: ImageVector.Builder(
        name = "FastForward",
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
            arcTo(2f, 2f, 0f, false, true, 15.414f, 4.586f)
            lineTo(21.414f, 10.586f)
            arcTo(2f, 2f, 0f, false, true, 21.414f, 13.414f)
            lineTo(15.414f, 19.414f)
            arcTo(2f, 2f, 0f, false, true, 12f, 18f)
            close()
            moveTo(2f, 6f)
            arcTo(2f, 2f, 0f, false, true, 5.414f, 4.586f)
            lineTo(11.414f, 10.586f)
            arcTo(2f, 2f, 0f, false, true, 11.414f, 13.414f)
            lineTo(5.414f, 19.414f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            close()
        }
    }.build().also { cachedFastForward = it }

/** `FastForward` vector, built on first access and reused afterwards. */
private var cachedFastForward: ImageVector? = null
