package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Donut icon.
 *
 * Converted from Lucide `donut` (ISC).
 */
public val ArecaIcons.Donut: ImageVector
    get() = cachedDonut ?: ImageVector.Builder(
        name = "Donut",
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
            moveTo(20.5f, 10f)
            arcTo(2.5f, 2.5f, 0f, false, true, 18.1f, 7f)
            horizontalLineTo(18f)
            arcTo(2.95f, 2.95f, 0f, false, true, 15.4f, 2.6f)
            arcTo(10f, 10f, 0f, true, false, 21.7f, 9.7f)
            curveTo(21.4f, 9.9f, 20.9f, 10f, 20.5f, 10f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
        }
    }.build().also { cachedDonut = it }

/** `Donut` vector, built on first access and reused afterwards. */
private var cachedDonut: ImageVector? = null
