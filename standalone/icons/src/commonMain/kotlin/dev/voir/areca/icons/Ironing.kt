package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ironing icon.
 *
 * Converted from Tabler Icons `ironing` (MIT).
 */
public val ArecaIcons.Ironing: ImageVector
    get() = cachedIroning ?: ImageVector.Builder(
        name = "Ironing",
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
            moveTo(9f, 6f)
            horizontalLineTo(16.459f)
            arcTo(3f, 3f, 0f, false, true, 19.418f, 8.507f)
            lineTo(19.995f, 11.971f)
            lineTo(20.805f, 16.836f)
            arcTo(1f, 1f, 0f, false, true, 19.82f, 18f)
            horizontalLineTo(3f)
            arcTo(7f, 7f, 0f, false, true, 10f, 11f)
            horizontalLineTo(19.8f)
        }
    }.build().also { cachedIroning = it }

/** `Ironing` vector, built on first access and reused afterwards. */
private var cachedIroning: ImageVector? = null
