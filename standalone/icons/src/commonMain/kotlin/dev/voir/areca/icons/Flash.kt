package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Zap icon.
 *
 * Same drawing as `ArecaIcons.Zap`; both names are kept.
 *
 * Converted from Lucide `zap` (ISC).
 */
public val ArecaIcons.Flash: ImageVector
    get() = cachedFlash ?: ImageVector.Builder(
        name = "Flash",
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
            moveTo(15.914f, 4f)
            arcTo(1.5f, 1.5f, 0f, false, false, 13.44f, 2.439f)
            lineTo(4.44f, 11.439f)
            arcTo(1.5f, 1.5f, 0f, false, false, 5.5f, 14f)
            horizontalLineTo(9.502f)
            arcTo(0.5f, 0.5f, 0f, false, true, 9.973f, 14.666f)
            lineTo(8.086f, 20f)
            arcTo(1.5f, 1.5f, 0f, false, false, 10.561f, 21.56f)
            lineTo(19.561f, 12.56f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 10f)
            horizontalLineTo(14.503f)
            arcTo(0.5f, 0.5f, 0f, false, true, 14.031f, 9.333f)
            close()
        }
    }.build().also { cachedFlash = it }

/** `Flash` vector, built on first access and reused afterwards. */
private var cachedFlash: ImageVector? = null
