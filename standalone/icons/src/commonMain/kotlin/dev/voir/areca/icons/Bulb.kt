package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bulb icon.
 *
 * Converted from Tabler Icons `bulb` (MIT).
 */
public val ArecaIcons.Bulb: ImageVector
    get() = cachedBulb ?: ImageVector.Builder(
        name = "Bulb",
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
            moveTo(3f, 12f)
            horizontalLineTo(4f)
            moveTo(12f, 3f)
            verticalLineTo(4f)
            moveTo(20f, 12f)
            horizontalLineTo(21f)
            moveTo(5.6f, 5.6f)
            lineTo(6.3f, 6.3f)
            moveTo(18.4f, 5.6f)
            lineTo(17.7f, 6.3f)
            moveTo(9f, 16f)
            arcTo(5f, 5f, 0f, true, true, 15f, 16f)
            arcTo(3.5f, 3.5f, 0f, false, false, 14f, 19f)
            arcTo(2f, 2f, 0f, false, true, 10f, 19f)
            arcTo(3.5f, 3.5f, 0f, false, false, 9f, 16f)
            moveTo(9.7f, 17f)
            lineTo(14.3f, 17f)
        }
    }.build().also { cachedBulb = it }

/** `Bulb` vector, built on first access and reused afterwards. */
private var cachedBulb: ImageVector? = null
