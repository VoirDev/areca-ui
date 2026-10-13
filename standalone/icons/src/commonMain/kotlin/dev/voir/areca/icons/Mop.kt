package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mop icon.
 *
 * Converted from Lucide `mop` (ISC).
 */
public val ArecaIcons.Mop: ImageVector
    get() = cachedMop ?: ImageVector.Builder(
        name = "Mop",
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
            moveTo(10f, 22f)
            curveTo(12.761f, 22f, 15f, 20.21f, 15f, 18f)
            curveTo(10.58f, 18f, 10.92f, 13f, 6.5f, 13f)
            arcTo(1f, 1f, 0f, true, false, 6.5f, 22f)
            close()
            arcTo(3f, 3f, 0f, false, true, 7f, 19f)
            moveTo(12.5f, 11.5f)
            lineTo(22f, 2f)
            moveTo(6.98f, 13.02f)
            lineTo(9.645f, 10.356f)
            arcTo(1.21f, 1.21f, 0f, false, true, 11.355f, 10.356f)
            lineTo(13.645f, 12.644f)
            arcTo(1.21f, 1.21f, 0f, false, true, 13.645f, 14.356f)
            lineTo(11.557f, 16.443f)
        }
    }.build().also { cachedMop = it }

/** `Mop` vector, built on first access and reused afterwards. */
private var cachedMop: ImageVector? = null
