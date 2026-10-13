package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Volume 2 icon.
 *
 * Converted from Lucide `volume-2` (ISC).
 */
public val ArecaIcons.VolumeMax: ImageVector
    get() = cachedVolumeMax ?: ImageVector.Builder(
        name = "VolumeMax",
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
            arcTo(0.705f, 0.705f, 0f, false, false, 9.797f, 4.204f)
            lineTo(6.413f, 7.587f)
            arcTo(1.4f, 1.4f, 0f, false, true, 5.416f, 8f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 2f, 9f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 3f, 16f)
            horizontalLineTo(5.416f)
            arcTo(1.4f, 1.4f, 0f, false, true, 6.413f, 16.413f)
            lineTo(9.796f, 19.797f)
            arcTo(0.705f, 0.705f, 0f, false, false, 11f, 19.298f)
            close()
            moveTo(16f, 9f)
            arcTo(5f, 5f, 0f, false, true, 16f, 15f)
            moveTo(19.364f, 18.364f)
            arcTo(9f, 9f, 0f, false, false, 19.364f, 5.636f)
        }
    }.build().also { cachedVolumeMax = it }

/** `VolumeMax` vector, built on first access and reused afterwards. */
private var cachedVolumeMax: ImageVector? = null
