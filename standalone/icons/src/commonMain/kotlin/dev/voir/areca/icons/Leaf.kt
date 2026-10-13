package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Leaf icon.
 *
 * Converted from Lucide `leaf` (ISC).
 */
public val ArecaIcons.Leaf: ImageVector
    get() = cachedLeaf ?: ImageVector.Builder(
        name = "Leaf",
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
            moveTo(11f, 20f)
            arcTo(10f, 10f, 0f, false, false, 21f, 10f)
            arcTo(25.9f, 25.9f, 0f, false, false, 19.96f, 2.719f)
            arcTo(1f, 1f, 0f, false, false, 18.205f, 2.394f)
            curveTo(15.833f, 5.5f, 13f, 5.5f, 9.8f, 6.1f)
            arcTo(7f, 7f, 0f, false, false, 11f, 20f)
            moveTo(2f, 21f)
            arcTo(5f, 5f, 0f, false, true, 4.911f, 16.456f)
            curveTo(7.613f, 15.212f, 8.351f, 15.24f, 11f, 13f)
        }
    }.build().also { cachedLeaf = it }

/** `Leaf` vector, built on first access and reused afterwards. */
private var cachedLeaf: ImageVector? = null
