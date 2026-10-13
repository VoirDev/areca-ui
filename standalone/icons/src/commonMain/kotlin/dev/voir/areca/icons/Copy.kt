package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Copy icon.
 *
 * Converted from Lucide `copy` (ISC).
 */
public val ArecaIcons.Copy: ImageVector
    get() = cachedCopy ?: ImageVector.Builder(
        name = "Copy",
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
            moveTo(10f, 8f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 10f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 22f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 8f, 20f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 10f, 8f)
            close()
            moveTo(4f, 16f)
            curveTo(2.9f, 16f, 2f, 15.1f, 2f, 14f)
            verticalLineTo(4f)
            curveTo(2f, 2.9f, 2.9f, 2f, 4f, 2f)
            horizontalLineTo(14f)
            curveTo(15.1f, 2f, 16f, 2.9f, 16f, 4f)
        }
    }.build().also { cachedCopy = it }

/** `Copy` vector, built on first access and reused afterwards. */
private var cachedCopy: ImageVector? = null
