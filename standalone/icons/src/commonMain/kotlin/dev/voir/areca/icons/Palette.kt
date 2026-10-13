package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Palette icon.
 *
 * Converted from Lucide `palette` (ISC).
 */
public val ArecaIcons.Palette: ImageVector
    get() = cachedPalette ?: ImageVector.Builder(
        name = "Palette",
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
            moveTo(12f, 22f)
            arcTo(1f, 1f, 0f, false, true, 12f, 2f)
            arcTo(10f, 9f, 0f, false, true, 22f, 11f)
            arcTo(5f, 5f, 0f, false, true, 17f, 16f)
            horizontalLineTo(14.75f)
            arcTo(1.75f, 1.75f, 0f, false, false, 13.35f, 18.8f)
            lineTo(13.65f, 19.2f)
            arcTo(1.75f, 1.75f, 0f, false, true, 12.25f, 22f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(13f, 6.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 14f, 6.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 13f, 6.5f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(17f, 10.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 18f, 10.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 17f, 10.5f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(6f, 12.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 7f, 12.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 6f, 12.5f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(8f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 9f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 8f, 7.5f)
            close()
        }
    }.build().also { cachedPalette = it }

/** `Palette` vector, built on first access and reused afterwards. */
private var cachedPalette: ImageVector? = null
