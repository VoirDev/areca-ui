package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vector icon.
 *
 * Converted from Tabler Icons `vector` (MIT).
 */
public val ArecaIcons.Transform: ImageVector
    get() = cachedTransform ?: ImageVector.Builder(
        name = "Transform",
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
            moveTo(3f, 4f)
            arcTo(1f, 1f, 0f, false, true, 4f, 3f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 7f, 4f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 6f, 7f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 6f)
            lineTo(3f, 4f)
            moveTo(17f, 4f)
            arcTo(1f, 1f, 0f, false, true, 18f, 3f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 4f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 20f, 7f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 17f, 6f)
            lineTo(17f, 4f)
            moveTo(17f, 18f)
            arcTo(1f, 1f, 0f, false, true, 18f, 17f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 18f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 20f, 21f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 17f, 20f)
            lineTo(17f, 18f)
            moveTo(3f, 18f)
            arcTo(1f, 1f, 0f, false, true, 4f, 17f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 7f, 18f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 6f, 21f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 20f)
            lineTo(3f, 18f)
            moveTo(5f, 7f)
            lineTo(5f, 17f)
            moveTo(19f, 7f)
            lineTo(19f, 17f)
            moveTo(7f, 5f)
            lineTo(17f, 5f)
            moveTo(7f, 19f)
            lineTo(17f, 19f)
        }
    }.build().also { cachedTransform = it }

/** `Transform` vector, built on first access and reused afterwards. */
private var cachedTransform: ImageVector? = null
