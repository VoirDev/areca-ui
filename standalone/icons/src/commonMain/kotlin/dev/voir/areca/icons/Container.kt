package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Container icon.
 *
 * Converted from Lucide `container` (ISC).
 */
public val ArecaIcons.Container: ImageVector
    get() = cachedContainer ?: ImageVector.Builder(
        name = "Container",
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
            moveTo(22f, 7.7f)
            curveTo(22f, 7.1f, 21.6f, 6.5f, 21.2f, 6.2f)
            lineTo(14.9f, 2.3f)
            arcTo(1.72f, 1.72f, 0f, false, false, 13.2f, 2.3f)
            lineTo(2.9f, 8.3f)
            curveTo(2.4f, 8.5f, 2f, 9.1f, 2f, 9.7f)
            verticalLineTo(16.3f)
            curveTo(2f, 16.8f, 2.4f, 17.5f, 2.8f, 17.8f)
            lineTo(9.1f, 21.7f)
            arcTo(1.72f, 1.72f, 0f, false, false, 10.8f, 21.7f)
            lineTo(21.1f, 15.7f)
            curveTo(21.6f, 15.4f, 22f, 14.7f, 22f, 14.2f)
            close()
            moveTo(10f, 21.9f)
            verticalLineTo(14f)
            lineTo(2.1f, 9.1f)
            moveTo(10f, 14f)
            lineTo(21.9f, 7.1f)
            moveTo(14f, 19.8f)
            verticalLineTo(11.7f)
            moveTo(18f, 17.5f)
            verticalLineTo(9.4f)
        }
    }.build().also { cachedContainer = it }

/** `Container` vector, built on first access and reused afterwards. */
private var cachedContainer: ImageVector? = null
