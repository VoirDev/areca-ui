package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cookie icon.
 *
 * Converted from Lucide `cookie` (ISC).
 */
public val ArecaIcons.Cookie: ImageVector
    get() = cachedCookie ?: ImageVector.Builder(
        name = "Cookie",
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
            moveTo(11f, 17f)
            horizontalLineTo(11.01f)
            moveTo(11.496f, 2f)
            curveTo(11.82f, 1.984f, 12.054f, 2.292f, 12.025f, 2.615f)
            arcTo(4f, 4f, 0f, false, false, 16.26f, 6.983f)
            arcTo(0.713f, 0.713f, 0f, false, true, 17.018f, 7.74f)
            arcTo(4f, 4f, 0f, false, false, 21.384f, 11.977f)
            curveTo(21.707f, 11.947f, 22.014f, 12.181f, 21.998f, 12.504f)
            arcTo(10f, 10f, 0f, false, true, 19.083f, 19.07f)
            arcTo(1f, 1f, 0f, true, true, 4.93f, 4.918f)
            arcTo(10f, 10f, 0f, false, true, 11.496f, 2f)
            moveTo(12f, 12f)
            horizontalLineTo(12.01f)
            moveTo(16f, 16f)
            horizontalLineTo(16.01f)
            moveTo(16f, 3f)
            horizontalLineTo(16.01f)
            moveTo(21f, 4f)
            horizontalLineTo(21.01f)
            moveTo(21f, 8f)
            horizontalLineTo(21.01f)
            moveTo(7f, 14f)
            horizontalLineTo(7.01f)
            moveTo(9f, 8f)
            horizontalLineTo(9.01f)
        }
    }.build().also { cachedCookie = it }

/** `Cookie` vector, built on first access and reused afterwards. */
private var cachedCookie: ImageVector? = null
