package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cake slice icon.
 *
 * Converted from Lucide `cake-slice` (ISC).
 */
public val ArecaIcons.CakeSlice: ImageVector
    get() = cachedCakeSlice ?: ImageVector.Builder(
        name = "CakeSlice",
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
            moveTo(16f, 13f)
            horizontalLineTo(3f)
            moveTo(16f, 17f)
            horizontalLineTo(3f)
            moveTo(7.2f, 7.9f)
            lineTo(3.812f, 10.4f)
            arcTo(2f, 2f, 0f, false, false, 3f, 12.01f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 21f, 20f)
            verticalLineTo(11.346f)
            curveTo(21f, 9.346f, 18.56f, 5.32f, 14.56f, 3.32f)
            arcTo(1f, 1f, 0f, false, false, 13.478f, 3.377f)
            lineTo(10.4f, 5.6f)
            moveTo(7f, 7f)
            arcTo(2f, 2f, 0f, true, true, 11f, 7f)
            arcTo(2f, 2f, 0f, true, true, 7f, 7f)
            close()
        }
    }.build().also { cachedCakeSlice = it }

/** `CakeSlice` vector, built on first access and reused afterwards. */
private var cachedCakeSlice: ImageVector? = null
