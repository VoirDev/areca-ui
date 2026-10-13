package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Biceps flexed icon.
 *
 * Converted from Lucide `biceps-flexed` (ISC).
 */
public val ArecaIcons.BicepsFlexed: ImageVector
    get() = cachedBicepsFlexed ?: ImageVector.Builder(
        name = "BicepsFlexed",
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
            moveTo(12.409f, 13.017f)
            arcTo(5f, 5f, 0f, false, true, 22f, 15f)
            curveTo(22f, 18.866f, 18f, 22f, 13f, 22f)
            curveTo(8.923f, 22f, 4.847f, 21.18f, 2.629f, 19.538f)
            curveTo(2.203f, 19.222f, 1.998f, 18.706f, 2.009f, 18.176f)
            curveTo(2.118f, 12.723f, 2.627f, 2f, 10f, 2f)
            arcTo(3f, 3f, 0f, false, true, 13f, 5f)
            arcTo(2f, 2f, 0f, false, true, 11f, 7f)
            curveTo(9.895f, 7f, 9.36f, 6.556f, 9f, 6f)
            moveTo(15f, 14f)
            arcTo(5f, 5f, 0f, false, false, 7.416f, 16f)
            moveTo(9.964f, 6.825f)
            curveTo(8.019f, 7.977f, 9.5f, 13f, 8f, 15f)
        }
    }.build().also { cachedBicepsFlexed = it }

/** `BicepsFlexed` vector, built on first access and reused afterwards. */
private var cachedBicepsFlexed: ImageVector? = null
