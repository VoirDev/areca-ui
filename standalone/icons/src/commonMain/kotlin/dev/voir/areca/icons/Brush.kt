package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Brush icon.
 *
 * Converted from Lucide `brush` (ISC).
 */
public val ArecaIcons.Brush: ImageVector
    get() = cachedBrush ?: ImageVector.Builder(
        name = "Brush",
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
            moveTo(11f, 10f)
            lineTo(14f, 13f)
            moveTo(6.5f, 21f)
            arcTo(3.5f, 3.5f, 0f, true, false, 3f, 17.5f)
            arcTo(2.62f, 2.62f, 0f, false, true, 2.292f, 19.292f)
            arcTo(1f, 1f, 0f, false, false, 3f, 21f)
            close()
            moveTo(9.969f, 17.031f)
            lineTo(21.378f, 5.624f)
            arcTo(1f, 1f, 0f, false, false, 18.376f, 2.622f)
            lineTo(6.967f, 14.031f)
        }
    }.build().also { cachedBrush = it }

/** `Brush` vector, built on first access and reused afterwards. */
private var cachedBrush: ImageVector? = null
