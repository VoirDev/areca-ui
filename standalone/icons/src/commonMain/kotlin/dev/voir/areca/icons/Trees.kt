package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Trees icon.
 *
 * Converted from Lucide `trees` (ISC).
 */
public val ArecaIcons.Trees: ImageVector
    get() = cachedTrees ?: ImageVector.Builder(
        name = "Trees",
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
            moveTo(10f, 10f)
            verticalLineTo(10.2f)
            arcTo(3f, 3f, 0f, false, true, 8.9f, 16f)
            horizontalLineTo(5f)
            arcTo(3f, 3f, 0f, false, true, 4f, 10.2f)
            verticalLineTo(10f)
            arcTo(3f, 3f, 0f, false, true, 10f, 10f)
            close()
            moveTo(7f, 16f)
            verticalLineTo(22f)
            moveTo(13f, 19f)
            verticalLineTo(22f)
            moveTo(12f, 19f)
            horizontalLineTo(20.3f)
            arcTo(1f, 1f, 0f, false, false, 21f, 17.3f)
            lineTo(18f, 14f)
            horizontalLineTo(18.3f)
            arcTo(1f, 1f, 0f, false, false, 19f, 12.3f)
            lineTo(16f, 9f)
            horizontalLineTo(16.2f)
            arcTo(1f, 1f, 0f, false, false, 17f, 7.3f)
            lineTo(13f, 3f)
            lineTo(11.6f, 4.5f)
        }
    }.build().also { cachedTrees = it }

/** `Trees` vector, built on first access and reused afterwards. */
private var cachedTrees: ImageVector? = null
