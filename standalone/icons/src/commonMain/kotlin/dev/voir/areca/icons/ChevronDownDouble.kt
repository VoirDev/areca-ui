package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons down icon.
 *
 * Converted from Lucide `chevrons-down` (ISC).
 */
public val ArecaIcons.ChevronDownDouble: ImageVector
    get() = cachedChevronDownDouble ?: ImageVector.Builder(
        name = "ChevronDownDouble",
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
            moveTo(7f, 6f)
            lineTo(12f, 11f)
            lineTo(17f, 6f)
            moveTo(7f, 13f)
            lineTo(12f, 18f)
            lineTo(17f, 13f)
        }
    }.build().also { cachedChevronDownDouble = it }

/** `ChevronDownDouble` vector, built on first access and reused afterwards. */
private var cachedChevronDownDouble: ImageVector? = null
