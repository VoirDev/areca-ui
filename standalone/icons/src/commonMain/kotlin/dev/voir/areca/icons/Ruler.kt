package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ruler icon.
 *
 * Converted from Lucide `ruler` (ISC).
 */
public val ArecaIcons.Ruler: ImageVector
    get() = cachedRuler ?: ImageVector.Builder(
        name = "Ruler",
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
            moveTo(21.3f, 15.3f)
            arcTo(2.4f, 2.4f, 0f, false, true, 21.3f, 18.7f)
            lineTo(18.7f, 21.3f)
            arcTo(2.4f, 2.4f, 0f, false, true, 15.3f, 21.3f)
            lineTo(2.7f, 8.7f)
            arcTo(2.41f, 2.41f, 0f, false, true, 2.7f, 5.3f)
            lineTo(5.3f, 2.7f)
            arcTo(2.41f, 2.41f, 0f, false, true, 8.7f, 2.7f)
            close()
            moveTo(14.5f, 12.5f)
            lineTo(16.5f, 10.5f)
            moveTo(11.5f, 9.5f)
            lineTo(13.5f, 7.5f)
            moveTo(8.5f, 6.5f)
            lineTo(10.5f, 4.5f)
            moveTo(17.5f, 15.5f)
            lineTo(19.5f, 13.5f)
        }
    }.build().also { cachedRuler = it }

/** `Ruler` vector, built on first access and reused afterwards. */
private var cachedRuler: ImageVector? = null
