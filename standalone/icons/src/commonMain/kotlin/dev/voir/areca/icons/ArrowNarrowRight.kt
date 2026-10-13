package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-right` (ISC).
 */
public val ArecaIcons.ArrowNarrowRight: ImageVector
    get() = cachedArrowNarrowRight ?: ImageVector.Builder(
        name = "ArrowNarrowRight",
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
            moveTo(18f, 8f)
            lineTo(22f, 12f)
            lineTo(18f, 16f)
            moveTo(2f, 12f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedArrowNarrowRight = it }

/** `ArrowNarrowRight` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowRight: ImageVector? = null
