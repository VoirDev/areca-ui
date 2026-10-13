package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move down icon.
 *
 * Converted from Lucide `move-down` (ISC).
 */
public val ArecaIcons.ArrowNarrowDown: ImageVector
    get() = cachedArrowNarrowDown ?: ImageVector.Builder(
        name = "ArrowNarrowDown",
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
            moveTo(8f, 18f)
            lineTo(12f, 22f)
            lineTo(16f, 18f)
            moveTo(12f, 2f)
            verticalLineTo(22f)
        }
    }.build().also { cachedArrowNarrowDown = it }

/** `ArrowNarrowDown` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowDown: ImageVector? = null
