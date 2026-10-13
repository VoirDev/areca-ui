package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gamepad 2 icon.
 *
 * Same drawing as `ArecaIcons.Gamepad2`; both names are kept.
 *
 * Converted from Lucide `gamepad-2` (ISC).
 */
public val ArecaIcons.GamingPad: ImageVector
    get() = cachedGamingPad ?: ImageVector.Builder(
        name = "GamingPad",
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
            moveTo(6f, 11f)
            lineTo(10f, 11f)
            moveTo(8f, 9f)
            lineTo(8f, 13f)
            moveTo(15f, 12f)
            lineTo(15.01f, 12f)
            moveTo(18f, 10f)
            lineTo(18.01f, 10f)
            moveTo(17.32f, 5f)
            horizontalLineTo(6.68f)
            arcTo(4f, 4f, 0f, false, false, 2.702f, 8.59f)
            curveTo(2.696f, 8.642f, 2.692f, 8.691f, 2.685f, 8.742f)
            curveTo(2.604f, 9.416f, 2f, 14.456f, 2f, 16f)
            arcTo(3f, 3f, 0f, false, false, 5f, 19f)
            curveTo(6f, 19f, 6.5f, 18.5f, 7f, 18f)
            lineTo(8.414f, 16.586f)
            arcTo(2f, 2f, 0f, false, true, 9.828f, 16f)
            horizontalLineTo(14.172f)
            arcTo(2f, 2f, 0f, false, true, 15.586f, 16.586f)
            lineTo(17f, 18f)
            curveTo(17.5f, 18.5f, 18f, 19f, 19f, 19f)
            arcTo(3f, 3f, 0f, false, false, 22f, 16f)
            curveTo(22f, 14.455f, 21.396f, 9.416f, 21.315f, 8.742f)
            curveTo(21.308f, 8.692f, 21.304f, 8.642f, 21.298f, 8.591f)
            arcTo(4f, 4f, 0f, false, false, 17.32f, 5f)
            close()
        }
    }.build().also { cachedGamingPad = it }

/** `GamingPad` vector, built on first access and reused afterwards. */
private var cachedGamingPad: ImageVector? = null
