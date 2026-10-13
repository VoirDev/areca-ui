package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Divide icon.
 *
 * Converted from Lucide `divide` (ISC).
 */
public val ArecaIcons.Divide: ImageVector
    get() = cachedDivide ?: ImageVector.Builder(
        name = "Divide",
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
            moveTo(11f, 6f)
            arcTo(1f, 1f, 0f, true, true, 13f, 6f)
            arcTo(1f, 1f, 0f, true, true, 11f, 6f)
            close()
            moveTo(5f, 12f)
            lineTo(19f, 12f)
            moveTo(11f, 18f)
            arcTo(1f, 1f, 0f, true, true, 13f, 18f)
            arcTo(1f, 1f, 0f, true, true, 11f, 18f)
            close()
        }
    }.build().also { cachedDivide = it }

/** `Divide` vector, built on first access and reused afterwards. */
private var cachedDivide: ImageVector? = null
