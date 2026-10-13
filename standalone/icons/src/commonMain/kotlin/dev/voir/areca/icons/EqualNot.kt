package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Equal not icon.
 *
 * Converted from Lucide `equal-not` (ISC).
 */
public val ArecaIcons.EqualNot: ImageVector
    get() = cachedEqualNot ?: ImageVector.Builder(
        name = "EqualNot",
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
            moveTo(5f, 9f)
            lineTo(19f, 9f)
            moveTo(5f, 15f)
            lineTo(19f, 15f)
            moveTo(19f, 5f)
            lineTo(5f, 19f)
        }
    }.build().also { cachedEqualNot = it }

/** `EqualNot` vector, built on first access and reused afterwards. */
private var cachedEqualNot: ImageVector? = null
