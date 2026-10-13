package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scissors line dashed icon.
 *
 * Converted from Lucide `scissors-line-dashed` (ISC).
 */
public val ArecaIcons.ScissorsCut: ImageVector
    get() = cachedScissorsCut ?: ImageVector.Builder(
        name = "ScissorsCut",
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
            moveTo(5.42f, 9.42f)
            lineTo(8f, 12f)
            moveTo(2f, 8f)
            arcTo(2f, 2f, 0f, true, true, 6f, 8f)
            arcTo(2f, 2f, 0f, true, true, 2f, 8f)
            close()
            moveTo(14f, 6f)
            lineTo(5.42f, 14.58f)
            moveTo(2f, 16f)
            arcTo(2f, 2f, 0f, true, true, 6f, 16f)
            arcTo(2f, 2f, 0f, true, true, 2f, 16f)
            close()
            moveTo(10.8f, 14.8f)
            lineTo(14f, 18f)
            moveTo(16f, 12f)
            horizontalLineTo(14f)
            moveTo(22f, 12f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedScissorsCut = it }

/** `ScissorsCut` vector, built on first access and reused afterwards. */
private var cachedScissorsCut: ImageVector? = null
