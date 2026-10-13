package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wheel icon.
 *
 * Converted from Tabler Icons `wheel` (MIT).
 */
public val ArecaIcons.Wheel: ImageVector
    get() = cachedWheel ?: ImageVector.Builder(
        name = "Wheel",
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
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, false, 15f, 12f)
            arcTo(3f, 3f, 0f, true, false, 9f, 12f)
            moveTo(3f, 12f)
            horizontalLineTo(9f)
            moveTo(15f, 12f)
            horizontalLineTo(21f)
            moveTo(13.6f, 9.4f)
            lineTo(17f, 4.6f)
            moveTo(10.4f, 14.6f)
            lineTo(7f, 19.4f)
            moveTo(7f, 4.6f)
            lineTo(10.4f, 9.4f)
            moveTo(13.6f, 14.6f)
            lineTo(17f, 19.4f)
        }
    }.build().also { cachedWheel = it }

/** `Wheel` vector, built on first access and reused afterwards. */
private var cachedWheel: ImageVector? = null
