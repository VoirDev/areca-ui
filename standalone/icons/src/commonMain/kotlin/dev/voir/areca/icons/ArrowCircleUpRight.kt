package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow up right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Tabler Icons `circle-arrow-up-right` (MIT).
 */
public val ArecaIcons.ArrowCircleUpRight: ImageVector
    get() = cachedArrowCircleUpRight ?: ImageVector.Builder(
        name = "ArrowCircleUpRight",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, false, false, 3f, 12f)
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(15f, 15f)
            verticalLineTo(9f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedArrowCircleUpRight = it }

/** `ArrowCircleUpRight` vector, built on first access and reused afterwards. */
private var cachedArrowCircleUpRight: ImageVector? = null
