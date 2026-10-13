package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paintbrush vertical icon.
 *
 * Converted from Lucide `paintbrush-vertical` (ISC).
 */
public val ArecaIcons.BrushAlt: ImageVector
    get() = cachedBrushAlt ?: ImageVector.Builder(
        name = "BrushAlt",
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
            moveTo(10f, 2f)
            verticalLineTo(4f)
            moveTo(14f, 2f)
            verticalLineTo(6f)
            moveTo(17f, 2f)
            arcTo(1f, 1f, 0f, false, true, 18f, 3f)
            verticalLineTo(12f)
            horizontalLineTo(6f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 7f, 2f)
            close()
            moveTo(6f, 12f)
            arcTo(1f, 1f, 0f, false, false, 5f, 13f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, false, 7f, 16f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 10f, 17f)
            verticalLineTo(19.9f)
            arcTo(2f, 2f, 0f, true, false, 14f, 19.9f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 15f, 16f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 14f)
            verticalLineTo(13f)
            arcTo(1f, 1f, 0f, false, false, 18f, 12f)
        }
    }.build().also { cachedBrushAlt = it }

/** `BrushAlt` vector, built on first access and reused afterwards. */
private var cachedBrushAlt: ImageVector? = null
