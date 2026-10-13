package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Laptop icon.
 *
 * Converted from Lucide `laptop` (ISC).
 */
public val ArecaIcons.Laptop: ImageVector
    get() = cachedLaptop ?: ImageVector.Builder(
        name = "Laptop",
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
            moveTo(18f, 5f)
            arcTo(2f, 2f, 0f, false, true, 20f, 7f)
            verticalLineTo(15.526f)
            arcTo(2f, 2f, 0f, false, false, 20.212f, 16.423f)
            lineTo(21.28f, 18.55f)
            arcTo(1f, 1f, 0f, false, true, 20.38f, 20f)
            horizontalLineTo(3.62f)
            arcTo(1f, 1f, 0f, false, true, 2.72f, 18.55f)
            lineTo(3.788f, 16.423f)
            arcTo(2f, 2f, 0f, false, false, 4f, 15.526f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 6f, 5f)
            close()
            moveTo(20.054f, 15.987f)
            horizontalLineTo(3.946f)
        }
    }.build().also { cachedLaptop = it }

/** `Laptop` vector, built on first access and reused afterwards. */
private var cachedLaptop: ImageVector? = null
