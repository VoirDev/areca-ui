package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tree deciduous icon.
 *
 * Converted from Lucide `tree-deciduous` (ISC).
 */
public val ArecaIcons.TreeDeciduous: ImageVector
    get() = cachedTreeDeciduous ?: ImageVector.Builder(
        name = "TreeDeciduous",
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
            moveTo(8f, 19f)
            arcTo(4f, 4f, 0f, false, true, 5.76f, 11.68f)
            arcTo(3.5f, 3.5f, 0f, false, true, 9f, 6.03f)
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, true, true, 15f, 6f)
            verticalLineTo(6.04f)
            arcTo(3.5f, 3.5f, 0f, false, true, 18.24f, 11.69f)
            arcTo(4f, 4f, 0f, false, true, 16f, 19f)
            close()
            moveTo(12f, 19f)
            verticalLineTo(22f)
        }
    }.build().also { cachedTreeDeciduous = it }

/** `TreeDeciduous` vector, built on first access and reused afterwards. */
private var cachedTreeDeciduous: ImageVector? = null
