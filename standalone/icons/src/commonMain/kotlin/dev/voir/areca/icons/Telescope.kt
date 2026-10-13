package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Telescope icon.
 *
 * Converted from Lucide `telescope` (ISC).
 */
public val ArecaIcons.Telescope: ImageVector
    get() = cachedTelescope ?: ImageVector.Builder(
        name = "Telescope",
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
            moveTo(10.065f, 12.493f)
            lineTo(3.885f, 13.811f)
            arcTo(0.934f, 0.934f, 0f, false, true, 2.777f, 13.109f)
            lineTo(2.24f, 10.959f)
            arcTo(1.07f, 1.07f, 0f, false, true, 2.931f, 9.694f)
            lineTo(16.435f, 5.254f)
            moveTo(13.56f, 11.747f)
            lineTo(17.892f, 10.823f)
            moveTo(16f, 21f)
            lineTo(12.895f, 14.79f)
            moveTo(16.485f, 5.94f)
            arcTo(2f, 2f, 0f, false, true, 17.94f, 3.515f)
            lineTo(19.03f, 3.243f)
            arcTo(1f, 1f, 0f, false, true, 20.242f, 3.97f)
            lineTo(21.757f, 10.03f)
            arcTo(1f, 1f, 0f, false, true, 21.03f, 11.243f)
            lineTo(19.94f, 11.515f)
            arcTo(2f, 2f, 0f, false, true, 17.515f, 10.06f)
            close()
            moveTo(6.158f, 8.633f)
            lineTo(7.272f, 13.089f)
            moveTo(8f, 21f)
            lineTo(11.105f, 14.79f)
            moveTo(10f, 13f)
            arcTo(2f, 2f, 0f, true, true, 14f, 13f)
            arcTo(2f, 2f, 0f, true, true, 10f, 13f)
            close()
        }
    }.build().also { cachedTelescope = it }

/** `Telescope` vector, built on first access and reused afterwards. */
private var cachedTelescope: ImageVector? = null
