package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow right left icon.
 *
 * Converted from Lucide `arrow-right-left` (ISC).
 */
public val ArecaIcons.SwitchHorizontalAlt: ImageVector
    get() = cachedSwitchHorizontalAlt ?: ImageVector.Builder(
        name = "SwitchHorizontalAlt",
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
            moveTo(16f, 3f)
            lineTo(20f, 7f)
            lineTo(16f, 11f)
            moveTo(20f, 7f)
            horizontalLineTo(4f)
            moveTo(8f, 21f)
            lineTo(4f, 17f)
            lineTo(8f, 13f)
            moveTo(4f, 17f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedSwitchHorizontalAlt = it }

/** `SwitchHorizontalAlt` vector, built on first access and reused afterwards. */
private var cachedSwitchHorizontalAlt: ImageVector? = null
