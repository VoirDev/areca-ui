package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrows left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Tabler Icons `arrows-left` (MIT).
 */
public val ArecaIcons.ArrowsLeft: ImageVector
    get() = cachedArrowsLeft ?: ImageVector.Builder(
        name = "ArrowsLeft",
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
            moveTo(3f, 7f)
            lineTo(21f, 7f)
            moveTo(6f, 20f)
            lineTo(3f, 17f)
            lineTo(6f, 14f)
            moveTo(6f, 4f)
            lineTo(3f, 7f)
            lineTo(6f, 10f)
            moveTo(3f, 17f)
            lineTo(21f, 17f)
        }
    }.build().also { cachedArrowsLeft = it }

/** `ArrowsLeft` vector, built on first access and reused afterwards. */
private var cachedArrowsLeft: ImageVector? = null
