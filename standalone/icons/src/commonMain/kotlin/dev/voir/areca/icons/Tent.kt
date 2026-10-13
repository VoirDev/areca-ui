package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tent icon.
 *
 * Converted from Lucide `tent` (ISC).
 */
public val ArecaIcons.Tent: ImageVector
    get() = cachedTent ?: ImageVector.Builder(
        name = "Tent",
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
            moveTo(3.5f, 21f)
            lineTo(14f, 3f)
            moveTo(20.5f, 21f)
            lineTo(10f, 3f)
            moveTo(15.5f, 21f)
            lineTo(12f, 15f)
            lineTo(8.5f, 21f)
            moveTo(2f, 21f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedTent = it }

/** `Tent` vector, built on first access and reused afterwards. */
private var cachedTent: ImageVector? = null
