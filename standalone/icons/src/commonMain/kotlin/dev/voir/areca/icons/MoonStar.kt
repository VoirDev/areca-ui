package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Moon star icon.
 *
 * Converted from Lucide `moon-star` (ISC).
 */
public val ArecaIcons.MoonStar: ImageVector
    get() = cachedMoonStar ?: ImageVector.Builder(
        name = "MoonStar",
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
            moveTo(18f, 5f)
            horizontalLineTo(22f)
            moveTo(20f, 3f)
            verticalLineTo(7f)
            moveTo(20.985f, 12.486f)
            arcTo(9f, 9f, 0f, true, true, 11.512f, 3.014f)
            curveTo(11.917f, 2.992f, 12.129f, 3.474f, 11.914f, 3.817f)
            arcTo(6f, 6f, 0f, false, false, 20.182f, 12.085f)
            curveTo(20.526f, 11.87f, 21.007f, 12.081f, 20.985f, 12.486f)
        }
    }.build().also { cachedMoonStar = it }

/** `MoonStar` vector, built on first access and reused afterwards. */
private var cachedMoonStar: ImageVector? = null
