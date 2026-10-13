package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bus front icon.
 *
 * Converted from Lucide `bus-front` (ISC).
 */
public val ArecaIcons.BusFront: ImageVector
    get() = cachedBusFront ?: ImageVector.Builder(
        name = "BusFront",
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
            moveTo(4f, 6f)
            lineTo(2f, 7f)
            moveTo(10f, 6f)
            horizontalLineTo(14f)
            moveTo(22f, 7f)
            lineTo(20f, 6f)
            moveTo(6f, 3f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 5f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 18f, 19f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 6f, 3f)
            close()
            moveTo(4f, 11f)
            horizontalLineTo(20f)
            moveTo(8f, 15f)
            horizontalLineTo(8.01f)
            moveTo(16f, 15f)
            horizontalLineTo(16.01f)
            moveTo(6f, 19f)
            verticalLineTo(21f)
            moveTo(18f, 21f)
            verticalLineTo(19f)
        }
    }.build().also { cachedBusFront = it }

/** `BusFront` vector, built on first access and reused afterwards. */
private var cachedBusFront: ImageVector? = null
