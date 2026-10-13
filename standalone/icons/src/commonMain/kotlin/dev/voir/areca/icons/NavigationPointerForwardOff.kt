package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Navigation 2 off icon.
 *
 * Converted from Lucide `navigation-2-off` (ISC).
 */
public val ArecaIcons.NavigationPointerForwardOff: ImageVector
    get() = cachedNavigationPointerForwardOff ?: ImageVector.Builder(
        name = "NavigationPointerForwardOff",
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
            moveTo(9.31f, 9.31f)
            lineTo(5f, 21f)
            lineTo(12f, 17f)
            lineTo(19f, 21f)
            lineTo(17.83f, 17.83f)
            moveTo(14.53f, 8.88f)
            lineTo(12f, 2f)
            lineTo(10.83f, 5.17f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedNavigationPointerForwardOff = it }

/** `NavigationPointerForwardOff` vector, built on first access and reused afterwards. */
private var cachedNavigationPointerForwardOff: ImageVector? = null
