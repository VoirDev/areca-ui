package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Navigation off icon.
 *
 * Converted from Lucide `navigation-off` (ISC).
 */
public val ArecaIcons.NavigationPointerOff: ImageVector
    get() = cachedNavigationPointerOff ?: ImageVector.Builder(
        name = "NavigationPointerOff",
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
            moveTo(8.43f, 8.43f)
            lineTo(3f, 11f)
            lineTo(11f, 13f)
            lineTo(13f, 21f)
            lineTo(15.57f, 15.57f)
            moveTo(17.39f, 11.73f)
            lineTo(22f, 2f)
            lineTo(12.27f, 6.61f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedNavigationPointerOff = it }

/** `NavigationPointerOff` vector, built on first access and reused afterwards. */
private var cachedNavigationPointerOff: ImageVector? = null
