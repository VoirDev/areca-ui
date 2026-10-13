package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car front icon.
 *
 * Converted from Lucide `car-front` (ISC).
 */
public val ArecaIcons.CarFront: ImageVector
    get() = cachedCarFront ?: ImageVector.Builder(
        name = "CarFront",
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
            moveTo(21f, 8f)
            lineTo(19f, 10f)
            lineTo(17.5f, 6.3f)
            arcTo(2f, 2f, 0f, false, false, 15.646f, 5f)
            horizontalLineTo(8.4f)
            arcTo(2f, 2f, 0f, false, false, 6.497f, 6.257f)
            lineTo(5f, 10f)
            lineTo(3f, 8f)
            moveTo(7f, 14f)
            horizontalLineTo(7.01f)
            moveTo(17f, 14f)
            horizontalLineTo(17.01f)
            moveTo(5f, 10f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 12f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 19f, 18f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 16f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 5f, 10f)
            close()
            moveTo(5f, 18f)
            verticalLineTo(20f)
            moveTo(19f, 18f)
            verticalLineTo(20f)
        }
    }.build().also { cachedCarFront = it }

/** `CarFront` vector, built on first access and reused afterwards. */
private var cachedCarFront: ImageVector? = null
