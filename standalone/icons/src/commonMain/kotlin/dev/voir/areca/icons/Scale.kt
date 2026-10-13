package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scaling icon.
 *
 * Converted from Lucide `scaling` (ISC).
 */
public val ArecaIcons.Scale: ImageVector
    get() = cachedScale ?: ImageVector.Builder(
        name = "Scale",
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
            moveTo(12f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            verticalLineTo(12f)
            moveTo(14f, 15f)
            horizontalLineTo(9f)
            verticalLineTo(10f)
            moveTo(16f, 3f)
            horizontalLineTo(21f)
            verticalLineTo(8f)
            moveTo(21f, 3f)
            lineTo(9f, 15f)
        }
    }.build().also { cachedScale = it }

/** `Scale` vector, built on first access and reused afterwards. */
private var cachedScale: ImageVector? = null
