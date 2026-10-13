package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Triangle icon.
 *
 * Converted from Lucide `triangle` (ISC).
 */
public val ArecaIcons.Triangle: ImageVector
    get() = cachedTriangle ?: ImageVector.Builder(
        name = "Triangle",
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
            moveTo(13.73f, 4f)
            arcTo(2f, 2f, 0f, false, false, 10.27f, 4f)
            lineTo(2.27f, 18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 21.73f, 18f)
            close()
        }
    }.build().also { cachedTriangle = it }

/** `Triangle` vector, built on first access and reused afterwards. */
private var cachedTriangle: ImageVector? = null
