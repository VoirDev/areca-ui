package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-right` (ISC).
 */
public val ArecaIcons.ArrowRight: ImageVector
    get() = cachedArrowRight ?: ImageVector.Builder(
        name = "ArrowRight",
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
            moveTo(5f, 12f)
            horizontalLineTo(19f)
            moveTo(12f, 5f)
            lineTo(19f, 12f)
            lineTo(12f, 19f)
        }
    }.build().also { cachedArrowRight = it }

/** `ArrowRight` vector, built on first access and reused afterwards. */
private var cachedArrowRight: ImageVector? = null
