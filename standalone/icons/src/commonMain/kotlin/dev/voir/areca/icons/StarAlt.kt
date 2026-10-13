package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Star icon.
 *
 * Converted from Tabler Icons `star` (MIT).
 */
public val ArecaIcons.StarAlt: ImageVector
    get() = cachedStarAlt ?: ImageVector.Builder(
        name = "StarAlt",
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
            moveTo(12f, 17.75f)
            lineTo(5.828f, 20.995f)
            lineTo(7.007f, 14.122f)
            lineTo(2.007f, 9.255f)
            lineTo(8.907f, 8.255f)
            lineTo(11.993f, 2.002f)
            lineTo(15.079f, 8.255f)
            lineTo(21.979f, 9.255f)
            lineTo(16.979f, 14.122f)
            lineTo(18.158f, 20.995f)
            lineTo(12f, 17.75f)
        }
    }.build().also { cachedStarAlt = it }

/** `StarAlt` vector, built on first access and reused afterwards. */
private var cachedStarAlt: ImageVector? = null
