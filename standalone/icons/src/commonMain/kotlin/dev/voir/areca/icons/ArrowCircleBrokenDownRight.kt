package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow out down right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `circle-arrow-out-down-right` (ISC).
 */
public val ArecaIcons.ArrowCircleBrokenDownRight: ImageVector
    get() = cachedArrowCircleBrokenDownRight ?: ImageVector.Builder(
        name = "ArrowCircleBrokenDownRight",
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
            moveTo(12f, 22f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            moveTo(22f, 22f)
            lineTo(12f, 12f)
            moveTo(22f, 16f)
            verticalLineTo(22f)
            horizontalLineTo(16f)
        }
    }.build().also { cachedArrowCircleBrokenDownRight = it }

/** `ArrowCircleBrokenDownRight` vector, built on first access and reused afterwards. */
private var cachedArrowCircleBrokenDownRight: ImageVector? = null
