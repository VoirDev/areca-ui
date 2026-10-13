package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow out up right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `circle-arrow-out-up-right` (ISC).
 */
public val ArecaIcons.ArrowCircleBrokenUpRight: ImageVector
    get() = cachedArrowCircleBrokenUpRight ?: ImageVector.Builder(
        name = "ArrowCircleBrokenUpRight",
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
            moveTo(22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 12f, 2f)
            moveTo(22f, 2f)
            lineTo(12f, 12f)
            moveTo(16f, 2f)
            horizontalLineTo(22f)
            verticalLineTo(8f)
        }
    }.build().also { cachedArrowCircleBrokenUpRight = it }

/** `ArrowCircleBrokenUpRight` vector, built on first access and reused afterwards. */
private var cachedArrowCircleBrokenUpRight: ImageVector? = null
