package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrows down icon.
 *
 * Converted from Tabler Icons `arrows-down` (MIT).
 */
public val ArecaIcons.ArrowsDown: ImageVector
    get() = cachedArrowsDown ?: ImageVector.Builder(
        name = "ArrowsDown",
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
            moveTo(7f, 21f)
            lineTo(7f, 3f)
            moveTo(20f, 18f)
            lineTo(17f, 21f)
            lineTo(14f, 18f)
            moveTo(4f, 18f)
            lineTo(7f, 21f)
            lineTo(10f, 18f)
            moveTo(17f, 21f)
            lineTo(17f, 3f)
        }
    }.build().also { cachedArrowsDown = it }

/** `ArrowsDown` vector, built on first access and reused afterwards. */
private var cachedArrowsDown: ImageVector? = null
