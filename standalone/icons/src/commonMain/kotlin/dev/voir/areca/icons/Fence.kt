package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fence icon.
 *
 * Converted from Lucide `fence` (ISC).
 */
public val ArecaIcons.Fence: ImageVector
    get() = cachedFence ?: ImageVector.Builder(
        name = "Fence",
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
            moveTo(4f, 3f)
            lineTo(2f, 5f)
            verticalLineTo(20f)
            curveTo(2f, 20.6f, 2.4f, 21f, 3f, 21f)
            horizontalLineTo(5f)
            curveTo(5.6f, 21f, 6f, 20.6f, 6f, 20f)
            verticalLineTo(5f)
            close()
            moveTo(6f, 8f)
            horizontalLineTo(10f)
            moveTo(6f, 18f)
            horizontalLineTo(10f)
            moveTo(12f, 3f)
            lineTo(10f, 5f)
            verticalLineTo(20f)
            curveTo(10f, 20.6f, 10.4f, 21f, 11f, 21f)
            horizontalLineTo(13f)
            curveTo(13.6f, 21f, 14f, 20.6f, 14f, 20f)
            verticalLineTo(5f)
            close()
            moveTo(14f, 8f)
            horizontalLineTo(18f)
            moveTo(14f, 18f)
            horizontalLineTo(18f)
            moveTo(20f, 3f)
            lineTo(18f, 5f)
            verticalLineTo(20f)
            curveTo(18f, 20.6f, 18.4f, 21f, 19f, 21f)
            horizontalLineTo(21f)
            curveTo(21.6f, 21f, 22f, 20.6f, 22f, 20f)
            verticalLineTo(5f)
            close()
        }
    }.build().also { cachedFence = it }

/** `Fence` vector, built on first access and reused afterwards. */
private var cachedFence: ImageVector? = null
