package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ear icon.
 *
 * Converted from Lucide `ear` (ISC).
 */
public val ArecaIcons.Ear: ImageVector
    get() = cachedEar ?: ImageVector.Builder(
        name = "Ear",
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
            moveTo(6f, 8.5f)
            arcTo(6.5f, 6.5f, 0f, true, true, 19f, 8.5f)
            curveTo(19f, 14.5f, 13f, 14.5f, 13f, 18.5f)
            arcTo(3.5f, 3.5f, 0f, true, true, 6f, 18.5f)
            moveTo(15f, 8.5f)
            arcTo(2.5f, 2.5f, 0f, false, false, 10f, 8.5f)
            verticalLineTo(9.5f)
            arcTo(2f, 2f, 0f, true, true, 10f, 13.5f)
        }
    }.build().also { cachedEar = it }

/** `Ear` vector, built on first access and reused afterwards. */
private var cachedEar: ImageVector? = null
