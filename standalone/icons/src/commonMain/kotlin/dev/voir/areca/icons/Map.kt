package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Map icon.
 *
 * Converted from Lucide `map` (ISC).
 */
public val ArecaIcons.Map: ImageVector
    get() = cachedMap ?: ImageVector.Builder(
        name = "Map",
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
            moveTo(14.106f, 5.553f)
            arcTo(2f, 2f, 0f, false, false, 15.894f, 5.553f)
            lineTo(19.553f, 3.723f)
            arcTo(1f, 1f, 0f, false, true, 21f, 4.619f)
            verticalLineTo(17.383f)
            arcTo(1f, 1f, 0f, false, true, 20.447f, 18.277f)
            lineTo(15.894f, 20.554f)
            arcTo(2f, 2f, 0f, false, true, 14.106f, 20.554f)
            lineTo(9.894f, 18.448f)
            arcTo(2f, 2f, 0f, false, false, 8.106f, 18.448f)
            lineTo(4.447f, 20.278f)
            arcTo(1f, 1f, 0f, false, true, 3f, 19.381f)
            verticalLineTo(6.618f)
            arcTo(1f, 1f, 0f, false, true, 3.553f, 5.724f)
            lineTo(8.106f, 3.447f)
            arcTo(2f, 2f, 0f, false, true, 9.894f, 3.447f)
            close()
            moveTo(15f, 5.764f)
            verticalLineTo(20.764f)
            moveTo(9f, 3.236f)
            verticalLineTo(18.236f)
        }
    }.build().also { cachedMap = it }

/** `Map` vector, built on first access and reused afterwards. */
private var cachedMap: ImageVector? = null
