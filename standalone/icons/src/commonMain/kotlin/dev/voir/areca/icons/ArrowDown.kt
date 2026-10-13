package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow down icon.
 *
 * Converted from Lucide `arrow-down` (ISC).
 */
public val ArecaIcons.ArrowDown: ImageVector
    get() = cachedArrowDown ?: ImageVector.Builder(
        name = "ArrowDown",
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
            moveTo(12f, 5f)
            verticalLineTo(19f)
            moveTo(19f, 12f)
            lineTo(12f, 19f)
            lineTo(5f, 12f)
        }
    }.build().also { cachedArrowDown = it }

/** `ArrowDown` vector, built on first access and reused afterwards. */
private var cachedArrowDown: ImageVector? = null
