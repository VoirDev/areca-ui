package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons up down icon.
 *
 * Converted from Lucide `chevrons-up-down` (ISC).
 */
public val ArecaIcons.ChevronSelectorVertical: ImageVector
    get() = cachedChevronSelectorVertical ?: ImageVector.Builder(
        name = "ChevronSelectorVertical",
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
            moveTo(7f, 15f)
            lineTo(12f, 20f)
            lineTo(17f, 15f)
            moveTo(7f, 9f)
            lineTo(12f, 4f)
            lineTo(17f, 9f)
        }
    }.build().also { cachedChevronSelectorVertical = it }

/** `ChevronSelectorVertical` vector, built on first access and reused afterwards. */
private var cachedChevronSelectorVertical: ImageVector? = null
