package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Footprints icon.
 *
 * Converted from Lucide `footprints` (ISC).
 */
public val ArecaIcons.Footprints: ImageVector
    get() = cachedFootprints ?: ImageVector.Builder(
        name = "Footprints",
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
            moveTo(4f, 16f)
            verticalLineTo(13.62f)
            curveTo(4f, 11.5f, 2.97f, 10.5f, 3f, 8f)
            curveTo(3.03f, 5.28f, 4.49f, 2f, 7.5f, 2f)
            curveTo(9.37f, 2f, 10f, 3.8f, 10f, 5.5f)
            curveTo(10f, 8.61f, 8f, 11.16f, 8f, 14.18f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, true, true, 4f, 16f)
            close()
            moveTo(20f, 20f)
            verticalLineTo(17.62f)
            curveTo(20f, 15.5f, 21.03f, 14.5f, 21f, 12f)
            curveTo(20.97f, 9.28f, 19.51f, 6f, 16.5f, 6f)
            curveTo(14.63f, 6f, 14f, 7.8f, 14f, 9.5f)
            curveTo(14f, 12.61f, 16f, 15.16f, 16f, 18.18f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, true, false, 20f, 20f)
            close()
            moveTo(16f, 17f)
            horizontalLineTo(20f)
            moveTo(4f, 13f)
            horizontalLineTo(8f)
        }
    }.build().also { cachedFootprints = it }

/** `Footprints` vector, built on first access and reused afterwards. */
private var cachedFootprints: ImageVector? = null
