package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Volume x icon.
 *
 * Converted from Lucide `volume-x` (ISC).
 */
public val ArecaIcons.VolumeX: ImageVector
    get() = cachedVolumeX ?: ImageVector.Builder(
        name = "VolumeX",
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
            moveTo(11f, 4.702f)
            arcTo(0.7f, 0.7f, 0f, false, false, 9.797f, 4.204f)
            lineTo(6.413f, 7.587f)
            arcTo(1.4f, 1.4f, 0f, false, true, 5.416f, 8f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 2f, 9f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 3f, 16f)
            horizontalLineTo(5.416f)
            arcTo(1.4f, 1.4f, 0f, false, true, 6.413f, 16.413f)
            lineTo(9.796f, 19.797f)
            arcTo(0.7f, 0.7f, 0f, false, false, 11f, 19.298f)
            close()
            moveTo(16.5f, 14.5f)
            lineTo(21.5f, 9.5f)
            moveTo(16.5f, 9.5f)
            lineTo(21.5f, 14.5f)
        }
    }.build().also { cachedVolumeX = it }

/** `VolumeX` vector, built on first access and reused afterwards. */
private var cachedVolumeX: ImageVector? = null
