package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chevrons right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `chevrons-right` (ISC).
 */
public val ArecaIcons.ChevronRightDouble: ImageVector
    get() = cachedChevronRightDouble ?: ImageVector.Builder(
        name = "ChevronRightDouble",
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
            moveTo(6f, 17f)
            lineTo(11f, 12f)
            lineTo(6f, 7f)
            moveTo(13f, 17f)
            lineTo(18f, 12f)
            lineTo(13f, 7f)
        }
    }.build().also { cachedChevronRightDouble = it }

/** `ChevronRightDouble` vector, built on first access and reused afterwards. */
private var cachedChevronRightDouble: ImageVector? = null
