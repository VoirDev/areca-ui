package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow out down left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `circle-arrow-out-down-left` (ISC).
 */
public val ArecaIcons.ArrowCircleBrokenDownLeft: ImageVector
    get() = cachedArrowCircleBrokenDownLeft ?: ImageVector.Builder(
        name = "ArrowCircleBrokenDownLeft",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 12f, 22f)
            moveTo(2f, 22f)
            lineTo(12f, 12f)
            moveTo(8f, 22f)
            horizontalLineTo(2f)
            verticalLineTo(16f)
        }
    }.build().also { cachedArrowCircleBrokenDownLeft = it }

/** `ArrowCircleBrokenDownLeft` vector, built on first access and reused afterwards. */
private var cachedArrowCircleBrokenDownLeft: ImageVector? = null
