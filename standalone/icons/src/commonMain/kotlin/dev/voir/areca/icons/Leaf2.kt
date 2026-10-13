package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Leaf 2 icon.
 *
 * Converted from Tabler Icons `leaf-2` (MIT).
 */
public val ArecaIcons.Leaf2: ImageVector
    get() = cachedLeaf2 ?: ImageVector.Builder(
        name = "Leaf2",
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
            moveTo(5f, 21f)
            curveTo(5.5f, 16.5f, 7.5f, 13f, 12f, 11f)
            moveTo(7.5f, 15f)
            quadTo(4f, 15f, 3f, 9f)
            arcTo(8.4f, 8.4f, 0f, false, true, 6.438f, 9.402f)
            arcTo(12f, 12f, 0f, false, true, 6.386f, 8.609f)
            curveTo(6.386f, 5.003f, 9.59f, 3f, 9.59f, 3f)
            reflectiveCurveTo(11.593f, 4.252f, 12.432f, 6.557f)
            quadTo(15f, 5f, 19f, 5f)
            quadTo(19.396f, 8.775f, 17.443f, 11.568f)
            curveTo(19.748f, 12.407f, 21f, 14.41f, 21f, 14.41f)
            reflectiveCurveTo(18f, 17f, 14f, 17f)
            curveTo(14f, 18f, 14f, 18f, 14.5f, 20f)
            quadTo(8.5f, 20f, 7.5f, 15f)
        }
    }.build().also { cachedLeaf2 = it }

/** `Leaf2` vector, built on first access and reused afterwards. */
private var cachedLeaf2: ImageVector? = null
