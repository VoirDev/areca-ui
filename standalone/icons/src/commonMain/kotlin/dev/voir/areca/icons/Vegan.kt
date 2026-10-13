package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vegan icon.
 *
 * Converted from Lucide `vegan` (ISC).
 */
public val ArecaIcons.Vegan: ImageVector
    get() = cachedVegan ?: ImageVector.Builder(
        name = "Vegan",
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
            moveTo(16f, 8f)
            quadTo(22f, 8f, 22f, 2f)
            quadTo(16f, 2f, 16f, 8f)
            moveTo(17.41f, 3.59f)
            arcTo(10f, 10f, 0f, true, false, 20.41f, 6.59f)
            moveTo(2f, 2f)
            arcTo(26.6f, 26.6f, 0f, false, true, 12f, 22f)
            curveTo(12.9f, 15.18f, 13.5f, 12.5f, 16f, 8f)
        }
    }.build().also { cachedVegan = it }

/** `Vegan` vector, built on first access and reused afterwards. */
private var cachedVegan: ImageVector? = null
