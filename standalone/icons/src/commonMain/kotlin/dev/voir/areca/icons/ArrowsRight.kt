package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrows right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Tabler Icons `arrows-right` (MIT).
 */
public val ArecaIcons.ArrowsRight: ImageVector
    get() = cachedArrowsRight ?: ImageVector.Builder(
        name = "ArrowsRight",
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
            moveTo(21f, 17f)
            lineTo(3f, 17f)
            moveTo(18f, 4f)
            lineTo(21f, 7f)
            lineTo(18f, 10f)
            moveTo(18f, 20f)
            lineTo(21f, 17f)
            lineTo(18f, 14f)
            moveTo(21f, 7f)
            lineTo(3f, 7f)
        }
    }.build().also { cachedArrowsRight = it }

/** `ArrowsRight` vector, built on first access and reused afterwards. */
private var cachedArrowsRight: ImageVector? = null
