package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `chevrons-left` (ISC).
 */
public val ArecaIcons.ChevronLeftDouble: ImageVector
    get() = cachedChevronLeftDouble ?: ImageVector.Builder(
        name = "ChevronLeftDouble",
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
            moveTo(11f, 17f)
            lineTo(6f, 12f)
            lineTo(11f, 7f)
            moveTo(18f, 17f)
            lineTo(13f, 12f)
            lineTo(18f, 7f)
        }
    }.build().also { cachedChevronLeftDouble = it }

/** `ChevronLeftDouble` vector, built on first access and reused afterwards. */
private var cachedChevronLeftDouble: ImageVector? = null
