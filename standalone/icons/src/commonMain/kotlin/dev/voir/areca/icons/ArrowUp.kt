package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow up icon.
 *
 * Converted from Lucide `arrow-up` (ISC).
 */
public val ArecaIcons.ArrowUp: ImageVector
    get() = cachedArrowUp ?: ImageVector.Builder(
        name = "ArrowUp",
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
            moveTo(5f, 12f)
            lineTo(12f, 5f)
            lineTo(19f, 12f)
            moveTo(12f, 19f)
            verticalLineTo(5f)
        }
    }.build().also { cachedArrowUp = it }

/** `ArrowUp` vector, built on first access and reused afterwards. */
private var cachedArrowUp: ImageVector? = null
