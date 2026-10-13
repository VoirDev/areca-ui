package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Watch icon.
 *
 * Converted from Lucide `watch` (ISC).
 */
public val ArecaIcons.Watch: ImageVector
    get() = cachedWatch ?: ImageVector.Builder(
        name = "Watch",
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
            moveTo(12f, 10f)
            verticalLineTo(12.2f)
            lineTo(13.6f, 13.2f)
            moveTo(16.13f, 7.66f)
            lineTo(15.32f, 3.61f)
            arcTo(2f, 2f, 0f, false, false, 13.32f, 2f)
            horizontalLineTo(10.64f)
            arcTo(2f, 2f, 0f, false, false, 8.64f, 3.61f)
            lineTo(7.86f, 7.66f)
            moveTo(7.88f, 16.36f)
            lineTo(8.68f, 20.36f)
            arcTo(2f, 2f, 0f, false, false, 10.68f, 21.97f)
            horizontalLineTo(13.4f)
            arcTo(2f, 2f, 0f, false, false, 15.4f, 20.36f)
            lineTo(16.21f, 16.31f)
            moveTo(6f, 12f)
            arcTo(6f, 6f, 0f, true, true, 18f, 12f)
            arcTo(6f, 6f, 0f, true, true, 6f, 12f)
            close()
        }
    }.build().also { cachedWatch = it }

/** `Watch` vector, built on first access and reused afterwards. */
private var cachedWatch: ImageVector? = null
