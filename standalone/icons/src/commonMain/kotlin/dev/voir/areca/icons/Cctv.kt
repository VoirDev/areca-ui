package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cctv icon.
 *
 * Converted from Lucide `cctv` (ISC).
 */
public val ArecaIcons.Cctv: ImageVector
    get() = cachedCctv ?: ImageVector.Builder(
        name = "Cctv",
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
            moveTo(16.75f, 12f)
            horizontalLineTo(20.382f)
            arcTo(1f, 1f, 0f, false, true, 21.276f, 13.447f)
            lineTo(19.242f, 17.516f)
            arcTo(1f, 1f, 0f, false, true, 17.534f, 17.65f)
            lineTo(15.41f, 14.68f)
            moveTo(17.106f, 9.053f)
            arcTo(1f, 1f, 0f, false, true, 17.553f, 10.394f)
            lineTo(14.447f, 16.605f)
            arcTo(1f, 1f, 0f, false, true, 13.105f, 17.052f)
            lineTo(3.61f, 12.3f)
            arcTo(2.92f, 2.92f, 0f, false, true, 2.31f, 8.39f)
            lineTo(3.69f, 5.6f)
            arcTo(2.92f, 2.92f, 0f, false, true, 7.61f, 4.3f)
            close()
            moveTo(2f, 19f)
            horizontalLineTo(5.76f)
            arcTo(2f, 2f, 0f, false, false, 7.56f, 17.9f)
            lineTo(9f, 15f)
            moveTo(2f, 21f)
            verticalLineTo(17f)
            moveTo(7f, 9f)
            horizontalLineTo(7.01f)
        }
    }.build().also { cachedCctv = it }

/** `Cctv` vector, built on first access and reused afterwards. */
private var cachedCctv: ImageVector? = null
