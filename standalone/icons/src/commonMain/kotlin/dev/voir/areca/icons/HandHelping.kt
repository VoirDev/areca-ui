package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand helping icon.
 *
 * Converted from Lucide `hand-helping` (ISC).
 */
public val ArecaIcons.HandHelping: ImageVector
    get() = cachedHandHelping ?: ImageVector.Builder(
        name = "HandHelping",
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
            moveTo(11f, 12f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, true, false, 13f, 8f)
            horizontalLineTo(10f)
            curveTo(9.4f, 8f, 8.9f, 8.2f, 8.6f, 8.6f)
            lineTo(3f, 14f)
            moveTo(7f, 18f)
            lineTo(8.6f, 16.6f)
            curveTo(8.9f, 16.2f, 9.4f, 16f, 10f, 16f)
            horizontalLineTo(14f)
            curveTo(15.1f, 16f, 16.1f, 15.6f, 16.8f, 14.8f)
            lineTo(21.4f, 10.4f)
            arcTo(2f, 2f, 0f, false, false, 18.65f, 7.49f)
            lineTo(14.45f, 11.39f)
            moveTo(2f, 13f)
            lineTo(8f, 19f)
        }
    }.build().also { cachedHandHelping = it }

/** `HandHelping` vector, built on first access and reused afterwards. */
private var cachedHandHelping: ImageVector? = null
