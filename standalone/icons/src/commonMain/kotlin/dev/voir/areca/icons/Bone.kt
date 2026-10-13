package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bone icon.
 *
 * Converted from Lucide `bone` (ISC).
 */
public val ArecaIcons.Bone: ImageVector
    get() = cachedBone ?: ImageVector.Builder(
        name = "Bone",
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
            moveTo(17f, 10f)
            curveTo(17.7f, 9.3f, 18.69f, 10f, 19.5f, 10f)
            arcTo(2.5f, 2.5f, 0f, true, false, 19.5f, 5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 19f, 4.5f)
            arcTo(2.5f, 2.5f, 0f, true, false, 14f, 4.5f)
            curveTo(14f, 5.31f, 14.7f, 6.3f, 14f, 7f)
            lineTo(7f, 14f)
            curveTo(6.3f, 14.7f, 5.31f, 14f, 4.5f, 14f)
            arcTo(2.5f, 2.5f, 0f, false, false, 4.5f, 19f)
            curveTo(4.78f, 19f, 5f, 19.22f, 5f, 19.5f)
            arcTo(2.5f, 2.5f, 0f, true, false, 10f, 19.5f)
            curveTo(10f, 18.69f, 9.3f, 17.7f, 10f, 17f)
            close()
        }
    }.build().also { cachedBone = it }

/** `Bone` vector, built on first access and reused afterwards. */
private var cachedBone: ImageVector? = null
