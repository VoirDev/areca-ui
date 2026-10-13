package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tv icon.
 *
 * Converted from Lucide `tv` (ISC).
 */
public val ArecaIcons.Tv: ImageVector
    get() = cachedTv ?: ImageVector.Builder(
        name = "Tv",
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
            moveTo(17f, 2f)
            lineTo(12f, 7f)
            lineTo(7f, 2f)
            moveTo(4f, 7f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 9f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 22f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 4f, 7f)
            close()
        }
    }.build().also { cachedTv = it }

/** `Tv` vector, built on first access and reused afterwards. */
private var cachedTv: ImageVector? = null
