package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons up icon.
 *
 * Converted from Lucide `chevrons-up` (ISC).
 */
public val ArecaIcons.ChevronUpDouble: ImageVector
    get() = cachedChevronUpDouble ?: ImageVector.Builder(
        name = "ChevronUpDouble",
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
            moveTo(17f, 11f)
            lineTo(12f, 6f)
            lineTo(7f, 11f)
            moveTo(17f, 18f)
            lineTo(12f, 13f)
            lineTo(7f, 18f)
        }
    }.build().also { cachedChevronUpDouble = it }

/** `ChevronUpDouble` vector, built on first access and reused afterwards. */
private var cachedChevronUpDouble: ImageVector? = null
