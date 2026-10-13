package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow down up icon.
 *
 * Converted from Lucide `arrow-down-up` (ISC).
 */
public val ArecaIcons.ArrowDownUp: ImageVector
    get() = cachedArrowDownUp ?: ImageVector.Builder(
        name = "ArrowDownUp",
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
            moveTo(3f, 16f)
            lineTo(7f, 20f)
            lineTo(11f, 16f)
            moveTo(7f, 20f)
            verticalLineTo(4f)
            moveTo(21f, 8f)
            lineTo(17f, 4f)
            lineTo(13f, 8f)
            moveTo(17f, 4f)
            verticalLineTo(20f)
        }
    }.build().also { cachedArrowDownUp = it }

/** `ArrowDownUp` vector, built on first access and reused afterwards. */
private var cachedArrowDownUp: ImageVector? = null
