package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Popcorn icon.
 *
 * Converted from Lucide `popcorn` (ISC).
 */
public val ArecaIcons.Popcorn: ImageVector
    get() = cachedPopcorn ?: ImageVector.Builder(
        name = "Popcorn",
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
            moveTo(18f, 8f)
            arcTo(2f, 2f, 0f, false, false, 18f, 4f)
            arcTo(2f, 2f, 0f, false, false, 14f, 4f)
            arcTo(2f, 2f, 0f, false, false, 10f, 4f)
            arcTo(2f, 2f, 0f, false, false, 6f, 4f)
            arcTo(2f, 2f, 0f, false, false, 6f, 8f)
            moveTo(10f, 22f)
            lineTo(9f, 8f)
            moveTo(14f, 22f)
            lineTo(15f, 8f)
            moveTo(20f, 8f)
            curveTo(20.5f, 8f, 20.9f, 8.4f, 20.8f, 9f)
            lineTo(18.2f, 21f)
            curveTo(18.1f, 21.5f, 17.5f, 22f, 17f, 22f)
            horizontalLineTo(7f)
            curveTo(6.4f, 22f, 5.9f, 21.6f, 5.8f, 21f)
            lineTo(3.2f, 9f)
            curveTo(3.1f, 8.4f, 3.5f, 8f, 4f, 8f)
            close()
        }
    }.build().also { cachedPopcorn = it }

/** `Popcorn` vector, built on first access and reused afterwards. */
private var cachedPopcorn: ImageVector? = null
