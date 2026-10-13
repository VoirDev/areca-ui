package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Road icon.
 *
 * Converted from Lucide `road` (ISC).
 */
public val ArecaIcons.Road: ImageVector
    get() = cachedRoad ?: ImageVector.Builder(
        name = "Road",
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
            moveTo(12f, 17f)
            verticalLineTo(21f)
            moveTo(12f, 5f)
            verticalLineTo(3f)
            moveTo(12f, 9f)
            verticalLineTo(12f)
            moveTo(2.077f, 18.449f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 21.924f, 18.45f)
            lineTo(17.924f, 4.45f)
            arcTo(2f, 2f, 0f, false, false, 16f, 3f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 6.076f, 4.45f)
            close()
        }
    }.build().also { cachedRoad = it }

/** `Road` vector, built on first access and reused afterwards. */
private var cachedRoad: ImageVector? = null
