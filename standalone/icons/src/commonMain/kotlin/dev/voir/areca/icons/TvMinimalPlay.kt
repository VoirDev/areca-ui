package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tv minimal play icon.
 *
 * Converted from Lucide `tv-minimal-play` (ISC).
 */
public val ArecaIcons.TvMinimalPlay: ImageVector
    get() = cachedTvMinimalPlay ?: ImageVector.Builder(
        name = "TvMinimalPlay",
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
            moveTo(15.033f, 9.44f)
            arcTo(0.647f, 0.647f, 0f, false, true, 15.033f, 10.56f)
            lineTo(10.968f, 12.912f)
            arcTo(0.645f, 0.645f, 0f, false, true, 10f, 12.352f)
            verticalLineTo(7.648f)
            arcTo(0.645f, 0.645f, 0f, false, true, 10.967f, 7.088f)
            close()
            moveTo(7f, 21f)
            horizontalLineTo(17f)
            moveTo(4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 20f, 17f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 15f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            close()
        }
    }.build().also { cachedTvMinimalPlay = it }

/** `TvMinimalPlay` vector, built on first access and reused afterwards. */
private var cachedTvMinimalPlay: ImageVector? = null
