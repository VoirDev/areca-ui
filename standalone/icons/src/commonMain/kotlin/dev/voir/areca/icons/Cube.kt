package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cube icon.
 *
 * Converted from Tabler Icons `cube` (MIT).
 */
public val ArecaIcons.Cube: ImageVector
    get() = cachedCube ?: ImageVector.Builder(
        name = "Cube",
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
            moveTo(21f, 16.008f)
            verticalLineTo(7.99f)
            arcTo(1.98f, 1.98f, 0f, false, false, 20f, 6.273f)
            lineTo(13f, 2.265f)
            arcTo(2.016f, 2.016f, 0f, false, false, 11f, 2.265f)
            lineTo(4f, 6.273f)
            curveTo(3.381f, 6.628f, 3f, 7.283f, 3f, 7.991f)
            verticalLineTo(16.009f)
            curveTo(3f, 16.718f, 3.381f, 17.372f, 4f, 17.726f)
            lineTo(11f, 21.734f)
            arcTo(2.016f, 2.016f, 0f, false, false, 13f, 21.734f)
            lineTo(20f, 17.726f)
            curveTo(20.619f, 17.371f, 21f, 16.716f, 21f, 16.008f)
            moveTo(12f, 22f)
            verticalLineTo(12f)
            moveTo(12f, 12f)
            lineTo(20.73f, 6.96f)
            moveTo(3.27f, 6.96f)
            lineTo(12f, 12f)
        }
    }.build().also { cachedCube = it }

/** `Cube` vector, built on first access and reused afterwards. */
private var cachedCube: ImageVector? = null
