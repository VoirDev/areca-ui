package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow left right icon.
 *
 * Converted from Lucide `arrow-left-right` (ISC).
 */
public val ArecaIcons.ArrowLeftRight: ImageVector
    get() = cachedArrowLeftRight ?: ImageVector.Builder(
        name = "ArrowLeftRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(8f, 3f)
            lineTo(4f, 7f)
            lineTo(8f, 11f)
            moveTo(4f, 7f)
            horizontalLineTo(20f)
            moveTo(16f, 21f)
            lineTo(20f, 17f)
            lineTo(16f, 13f)
            moveTo(20f, 17f)
            horizontalLineTo(4f)
        }
    }.build().also { cachedArrowLeftRight = it }

/** `ArrowLeftRight` vector, built on first access and reused afterwards. */
private var cachedArrowLeftRight: ImageVector? = null
