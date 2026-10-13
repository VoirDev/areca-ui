package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Globe icon.
 *
 * Converted from Lucide `globe` (ISC).
 */
public val ArecaIcons.Globe: ImageVector
    get() = cachedGlobe ?: ImageVector.Builder(
        name = "Globe",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(12f, 2f)
            arcTo(14.5f, 14.5f, 0f, false, false, 12f, 22f)
            arcTo(14.5f, 14.5f, 0f, false, false, 12f, 2f)
            moveTo(2f, 12f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedGlobe = it }

/** `Globe` vector, built on first access and reused afterwards. */
private var cachedGlobe: ImageVector? = null
