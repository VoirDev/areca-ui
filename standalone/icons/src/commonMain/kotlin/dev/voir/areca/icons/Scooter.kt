package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scooter icon.
 *
 * Converted from Lucide `scooter` (ISC).
 */
public val ArecaIcons.Scooter: ImageVector
    get() = cachedScooter ?: ImageVector.Builder(
        name = "Scooter",
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
            moveTo(21f, 4f)
            horizontalLineTo(17.5f)
            lineTo(19.5f, 15.05f)
            moveTo(6.95f, 17f)
            horizontalLineTo(12.092f)
            curveTo(12.615f, 17f, 13.042f, 16.594f, 13.155f, 16.084f)
            arcTo(6.5f, 6.5f, 0f, false, true, 18.5f, 11.075f)
            moveTo(17f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 22f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 17f, 17.5f)
            close()
            moveTo(2f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 7f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 2f, 17.5f)
            close()
        }
    }.build().also { cachedScooter = it }

/** `Scooter` vector, built on first access and reused afterwards. */
private var cachedScooter: ImageVector? = null
