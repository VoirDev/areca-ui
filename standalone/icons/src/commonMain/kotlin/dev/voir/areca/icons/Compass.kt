package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Compass icon.
 *
 * Converted from Lucide `compass` (ISC).
 */
public val ArecaIcons.Compass: ImageVector
    get() = cachedCompass ?: ImageVector.Builder(
        name = "Compass",
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
            moveTo(16.24f, 7.76f)
            lineTo(14.436f, 13.171f)
            arcTo(2f, 2f, 0f, false, true, 13.171f, 14.436f)
            lineTo(7.76f, 16.24f)
            lineTo(9.564f, 10.829f)
            arcTo(2f, 2f, 0f, false, true, 10.829f, 9.564f)
            close()
        }
    }.build().also { cachedCompass = it }

/** `Compass` vector, built on first access and reused afterwards. */
private var cachedCompass: ImageVector? = null
