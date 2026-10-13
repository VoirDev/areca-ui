package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-left` (ISC).
 */
public val ArecaIcons.ArrowLeft: ImageVector
    get() = cachedArrowLeft ?: ImageVector.Builder(
        name = "ArrowLeft",
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
            moveTo(12f, 19f)
            lineTo(5f, 12f)
            lineTo(12f, 5f)
            moveTo(19f, 12f)
            horizontalLineTo(5f)
        }
    }.build().also { cachedArrowLeft = it }

/** `ArrowLeft` vector, built on first access and reused afterwards. */
private var cachedArrowLeft: ImageVector? = null
