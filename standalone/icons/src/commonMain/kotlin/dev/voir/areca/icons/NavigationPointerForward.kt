package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Navigation 2 icon.
 *
 * Converted from Lucide `navigation-2` (ISC).
 */
public val ArecaIcons.NavigationPointerForward: ImageVector
    get() = cachedNavigationPointerForward ?: ImageVector.Builder(
        name = "NavigationPointerForward",
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
            moveTo(12f, 2f)
            lineTo(19f, 21f)
            lineTo(12f, 17f)
            lineTo(5f, 21f)
            lineTo(12f, 2f)
            close()
        }
    }.build().also { cachedNavigationPointerForward = it }

/** `NavigationPointerForward` vector, built on first access and reused afterwards. */
private var cachedNavigationPointerForward: ImageVector? = null
