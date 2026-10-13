package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Photo circle icon.
 *
 * Converted from Tabler Icons `photo-circle` (MIT).
 */
public val ArecaIcons.ImageCircle: ImageVector
    get() = cachedImageCircle ?: ImageVector.Builder(
        name = "ImageCircle",
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
            moveTo(15f, 8f)
            horizontalLineTo(15.01f)
            moveTo(4f, 15f)
            lineTo(8f, 11f)
            curveTo(8.928f, 10.107f, 10.072f, 10.107f, 11f, 11f)
            lineTo(16f, 16f)
            moveTo(14f, 14f)
            lineTo(15f, 13f)
            curveTo(15.928f, 12.107f, 17.072f, 12.107f, 18f, 13f)
            lineTo(20f, 15f)
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, false, false, 12f, 21f)
            arcTo(9f, 9f, 0f, false, false, 21f, 12f)
            arcTo(9f, 9f, 0f, false, false, 12f, 3f)
            arcTo(9f, 9f, 0f, false, false, 3f, 12f)
        }
    }.build().also { cachedImageCircle = it }

/** `ImageCircle` vector, built on first access and reused afterwards. */
private var cachedImageCircle: ImageVector? = null
