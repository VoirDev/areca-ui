package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toothbrush icon.
 *
 * Converted from Lucide `toothbrush` (ISC).
 */
public val ArecaIcons.Toothbrush: ImageVector
    get() = cachedToothbrush ?: ImageVector.Builder(
        name = "Toothbrush",
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
            moveTo(15f, 11f)
            curveTo(13f, 13f, 11f, 13f, 9f, 15f)
            lineTo(2f, 22f)
            moveTo(22f, 4f)
            lineTo(14.586f, 11.414f)
            lineTo(12.586f, 9.414f)
            arcTo(2f, 2f, 0f, false, true, 14f, 6f)
            curveTo(14f, 5.488f, 14.196f, 4.976f, 14.586f, 4.586f)
            arcTo(2f, 2f, 0f, false, true, 16f, 4f)
            arcTo(2f, 2f, 0f, false, true, 19.262f, 2.448f)
            lineTo(21.414f, 4.586f)
        }
    }.build().also { cachedToothbrush = it }

/** `Toothbrush` vector, built on first access and reused afterwards. */
private var cachedToothbrush: ImageVector? = null
