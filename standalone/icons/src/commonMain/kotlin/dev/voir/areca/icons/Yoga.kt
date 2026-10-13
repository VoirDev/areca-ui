package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Yoga icon.
 *
 * Converted from Tabler Icons `yoga` (MIT).
 */
public val ArecaIcons.Yoga: ImageVector
    get() = cachedYoga ?: ImageVector.Builder(
        name = "Yoga",
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
            moveTo(4f, 20f)
            horizontalLineTo(8f)
            lineTo(9.5f, 17f)
            moveTo(17f, 20f)
            lineTo(16f, 15f)
            horizontalLineTo(11f)
            lineTo(12f, 8f)
            moveTo(4f, 10f)
            lineTo(8f, 9f)
            lineTo(12f, 8f)
            lineTo(16f, 9.5f)
            lineTo(20f, 11f)
            moveTo(10.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 14.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 10.007f, 5f)
        }
    }.build().also { cachedYoga = it }

/** `Yoga` vector, built on first access and reused afterwards. */
private var cachedYoga: ImageVector? = null
