package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Tabler Icons `circle-arrow-right` (MIT).
 */
public val ArecaIcons.ArrowCircleRight: ImageVector
    get() = cachedArrowCircleRight ?: ImageVector.Builder(
        name = "ArrowCircleRight",
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
            moveTo(12f, 3f)
            arcTo(9f, 9f, 0f, true, false, 12f, 21f)
            arcTo(9f, 9f, 0f, false, false, 12f, 3f)
            moveTo(16f, 12f)
            lineTo(12f, 8f)
            moveTo(16f, 12f)
            horizontalLineTo(8f)
            moveTo(12f, 16f)
            lineTo(16f, 12f)
        }
    }.build().also { cachedArrowCircleRight = it }

/** `ArrowCircleRight` vector, built on first access and reused afterwards. */
private var cachedArrowCircleRight: ImageVector? = null
