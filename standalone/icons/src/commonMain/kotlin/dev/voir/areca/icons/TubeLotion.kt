package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tube lotion icon.
 *
 * Converted from Lucide `tube-lotion` (ISC).
 */
public val ArecaIcons.TubeLotion: ImageVector
    get() = cachedTubeLotion ?: ImageVector.Builder(
        name = "TubeLotion",
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
            moveTo(15f, 18f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 14f, 22f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 9f, 21f)
            verticalLineTo(18f)
            moveTo(17f, 2f)
            arcTo(2f, 2f, 0f, false, true, 18.6f, 5.2f)
            arcTo(8f, 8f, 0f, false, false, 17f, 10f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 15f, 18f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 7f, 16f)
            verticalLineTo(10f)
            arcTo(8f, 8f, 0f, false, false, 5.4f, 5.2f)
            arcTo(2f, 2f, 0f, false, true, 7f, 2f)
            close()
            moveTo(7f, 10f)
            arcTo(6.47f, 6.47f, 0f, false, true, 12f, 10f)
            arcTo(6.47f, 6.47f, 0f, false, false, 17f, 10f)
        }
    }.build().also { cachedTubeLotion = it }

/** `TubeLotion` vector, built on first access and reused afterwards. */
private var cachedTubeLotion: ImageVector? = null
