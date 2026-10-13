package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevron left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `chevron-left` (ISC).
 */
public val ArecaIcons.ChevronLeft: ImageVector
    get() = cachedChevronLeft ?: ImageVector.Builder(
        name = "ChevronLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(15f, 18f)
            lineTo(9f, 12f)
            lineTo(15f, 6f)
        }
    }.build().also { cachedChevronLeft = it }

/** `ChevronLeft` vector, built on first access and reused afterwards. */
private var cachedChevronLeft: ImageVector? = null
