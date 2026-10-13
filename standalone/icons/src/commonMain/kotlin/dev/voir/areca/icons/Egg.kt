package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Egg icon.
 *
 * Converted from Lucide `egg` (ISC).
 */
public val ArecaIcons.Egg: ImageVector
    get() = cachedEgg ?: ImageVector.Builder(
        name = "Egg",
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
            moveTo(12f, 2f)
            curveTo(8f, 2f, 4f, 8f, 4f, 14f)
            arcTo(8f, 8f, 0f, false, false, 20f, 14f)
            curveTo(20f, 8f, 16f, 2f, 12f, 2f)
        }
    }.build().also { cachedEgg = it }

/** `Egg` vector, built on first access and reused afterwards. */
private var cachedEgg: ImageVector? = null
