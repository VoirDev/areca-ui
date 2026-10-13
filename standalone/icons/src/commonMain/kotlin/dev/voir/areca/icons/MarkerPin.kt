package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Map pin icon.
 *
 * Converted from Lucide `map-pin` (ISC).
 */
public val ArecaIcons.MarkerPin: ImageVector
    get() = cachedMarkerPin ?: ImageVector.Builder(
        name = "MarkerPin",
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
            moveTo(20f, 10f)
            curveTo(20f, 14.993f, 14.461f, 20.193f, 12.601f, 21.799f)
            arcTo(1f, 1f, 0f, false, true, 11.399f, 21.799f)
            curveTo(9.539f, 20.193f, 4f, 14.993f, 4f, 10f)
            arcTo(8f, 8f, 0f, false, true, 20f, 10f)
            moveTo(9f, 10f)
            arcTo(3f, 3f, 0f, true, true, 15f, 10f)
            arcTo(3f, 3f, 0f, true, true, 9f, 10f)
            close()
        }
    }.build().also { cachedMarkerPin = it }

/** `MarkerPin` vector, built on first access and reused afterwards. */
private var cachedMarkerPin: ImageVector? = null
