package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Volleyball icon.
 *
 * Converted from Lucide `volleyball` (ISC).
 */
public val ArecaIcons.Volleyball: ImageVector
    get() = cachedVolleyball ?: ImageVector.Builder(
        name = "Volleyball",
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
            moveTo(11f, 7f)
            arcTo(16f, 16f, 20f, false, true, 21.98f, 11.362f)
            moveTo(12f, 12f)
            arcTo(13f, 13f, 0f, false, true, 3.34f, 17f)
            moveTo(16.83f, 13.634f)
            arcTo(16f, 16f, 0f, false, true, 7.563f, 20.962f)
            moveTo(20.66f, 17f)
            arcTo(13f, 13f, 0f, false, false, 12f, 12f)
            arcTo(13f, 13f, 0f, false, true, 12f, 2f)
            moveTo(8.17f, 15.366f)
            arcTo(16f, 16f, 0f, false, true, 6.457f, 3.676f)
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
        }
    }.build().also { cachedVolleyball = it }

/** `Volleyball` vector, built on first access and reused afterwards. */
private var cachedVolleyball: ImageVector? = null
