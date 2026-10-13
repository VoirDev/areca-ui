package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons left right icon.
 *
 * Converted from Lucide `chevrons-left-right` (ISC).
 */
public val ArecaIcons.ChevronSelectorHorizontal: ImageVector
    get() = cachedChevronSelectorHorizontal ?: ImageVector.Builder(
        name = "ChevronSelectorHorizontal",
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
            moveTo(9f, 7f)
            lineTo(4f, 12f)
            lineTo(9f, 17f)
            moveTo(15f, 7f)
            lineTo(20f, 12f)
            lineTo(15f, 17f)
        }
    }.build().also { cachedChevronSelectorHorizontal = it }

/** `ChevronSelectorHorizontal` vector, built on first access and reused afterwards. */
private var cachedChevronSelectorHorizontal: ImageVector? = null
