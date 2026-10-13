package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Leafy green icon.
 *
 * Converted from Lucide `leafy-green` (ISC).
 */
public val ArecaIcons.LeafyGreen: ImageVector
    get() = cachedLeafyGreen ?: ImageVector.Builder(
        name = "LeafyGreen",
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
            moveTo(2f, 22f)
            curveTo(3.25f, 21.013f, 4.27f, 20.025f, 5.9f, 19.8f)
            arcTo(5.56f, 5.56f, 0f, false, true, 9.7f, 21.3f)
            arcTo(4f, 4f, 0f, false, false, 15.887f, 18.947f)
            arcTo(3.5f, 3.5f, 0f, false, false, 19.577f, 13.831f)
            arcTo(3.5f, 3.5f, 0f, false, false, 20.95f, 8f)
            arcTo(3.5f, 3.5f, 0f, true, false, 16f, 3.05f)
            arcTo(3.5f, 3.5f, 0f, false, false, 10.169f, 4.423f)
            arcTo(3.5f, 3.5f, 0f, false, false, 5.053f, 8.113f)
            arcTo(4f, 4f, 0f, false, false, 2.705f, 14.268f)
            curveTo(3.499f, 15.42f, 4.409f, 16.712f, 4.2f, 18.1f)
            curveTo(3.926f, 19.743f, 3.014f, 20.732f, 2f, 22f)
            moveTo(2f, 22f)
            lineTo(17f, 7f)
        }
    }.build().also { cachedLeafyGreen = it }

/** `LeafyGreen` vector, built on first access and reused afterwards. */
private var cachedLeafyGreen: ImageVector? = null
