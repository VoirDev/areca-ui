package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrows up icon.
 *
 * Converted from Tabler Icons `arrows-up` (MIT).
 */
public val ArecaIcons.ArrowsUp: ImageVector
    get() = cachedArrowsUp ?: ImageVector.Builder(
        name = "ArrowsUp",
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
            moveTo(17f, 3f)
            lineTo(17f, 21f)
            moveTo(4f, 6f)
            lineTo(7f, 3f)
            lineTo(10f, 6f)
            moveTo(20f, 6f)
            lineTo(17f, 3f)
            lineTo(14f, 6f)
            moveTo(7f, 3f)
            lineTo(7f, 21f)
        }
    }.build().also { cachedArrowsUp = it }

/** `ArrowsUp` vector, built on first access and reused afterwards. */
private var cachedArrowsUp: ImageVector? = null
