package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ice cream cone icon.
 *
 * Converted from Lucide `ice-cream-cone` (ISC).
 */
public val ArecaIcons.IceCreamCone: ImageVector
    get() = cachedIceCreamCone ?: ImageVector.Builder(
        name = "IceCreamCone",
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
            moveTo(7f, 11f)
            lineTo(11.08f, 21.35f)
            arcTo(1f, 1f, 0f, false, false, 12.92f, 21.35f)
            lineTo(17f, 11f)
            moveTo(17f, 7f)
            arcTo(5f, 5f, 0f, false, false, 7f, 7f)
            moveTo(17f, 7f)
            arcTo(2f, 2f, 0f, false, true, 17f, 11f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 7f, 7f)
        }
    }.build().also { cachedIceCreamCone = it }

/** `IceCreamCone` vector, built on first access and reused afterwards. */
private var cachedIceCreamCone: ImageVector? = null
