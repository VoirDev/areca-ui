package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Move left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `move-left` (ISC).
 */
public val ArecaIcons.ArrowNarrowLeft: ImageVector
    get() = cachedArrowNarrowLeft ?: ImageVector.Builder(
        name = "ArrowNarrowLeft",
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
            moveTo(6f, 8f)
            lineTo(2f, 12f)
            lineTo(6f, 16f)
            moveTo(2f, 12f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedArrowNarrowLeft = it }

/** `ArrowNarrowLeft` vector, built on first access and reused afterwards. */
private var cachedArrowNarrowLeft: ImageVector? = null
