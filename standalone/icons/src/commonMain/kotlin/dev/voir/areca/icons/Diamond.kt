package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gem icon.
 *
 * Same drawing as `ArecaIcons.Gem`; both names are kept.
 *
 * Converted from Lucide `gem` (ISC).
 */
public val ArecaIcons.Diamond: ImageVector
    get() = cachedDiamond ?: ImageVector.Builder(
        name = "Diamond",
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
            moveTo(10.5f, 3f)
            lineTo(8f, 9f)
            lineTo(12f, 22f)
            lineTo(16f, 9f)
            lineTo(13.5f, 3f)
            moveTo(17f, 3f)
            arcTo(2f, 2f, 0f, false, true, 18.6f, 3.8f)
            lineTo(21.6f, 7.8f)
            arcTo(2f, 2f, 0f, false, true, 21.613f, 10.182f)
            lineTo(13.623f, 21.168f)
            arcTo(2f, 2f, 0f, false, true, 10.376f, 21.168f)
            lineTo(2.386f, 10.182f)
            arcTo(2f, 2f, 0f, false, true, 2.4f, 7.8f)
            lineTo(5.398f, 3.803f)
            arcTo(2f, 2f, 0f, false, true, 7f, 3f)
            close()
            moveTo(2f, 9f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedDiamond = it }

/** `Diamond` vector, built on first access and reused afterwards. */
private var cachedDiamond: ImageVector? = null
