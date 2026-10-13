package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow down right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Tabler Icons `circle-arrow-down-right` (MIT).
 */
public val ArecaIcons.ArrowCircleDownRight: ImageVector
    get() = cachedArrowCircleDownRight ?: ImageVector.Builder(
        name = "ArrowCircleDownRight",
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
            moveTo(15f, 15f)
            horizontalLineTo(9f)
            moveTo(15f, 9f)
            verticalLineTo(15f)
            lineTo(9f, 9f)
        }
    }.build().also { cachedArrowCircleDownRight = it }

/** `ArrowCircleDownRight` vector, built on first access and reused afterwards. */
private var cachedArrowCircleDownRight: ImageVector? = null
