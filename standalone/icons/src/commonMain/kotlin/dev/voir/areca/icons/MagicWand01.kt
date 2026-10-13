package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wand sparkles icon.
 *
 * Converted from Lucide `wand-sparkles` (ISC).
 */
public val ArecaIcons.MagicWand01: ImageVector
    get() = cachedMagicWand01 ?: ImageVector.Builder(
        name = "MagicWand01",
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
            moveTo(21.64f, 3.64f)
            lineTo(20.36f, 2.36f)
            arcTo(1.21f, 1.21f, 0f, false, false, 18.64f, 2.36f)
            lineTo(2.36f, 18.64f)
            arcTo(1.21f, 1.21f, 0f, false, false, 2.36f, 20.36f)
            lineTo(3.64f, 21.64f)
            arcTo(1.2f, 1.2f, 0f, false, false, 5.36f, 21.64f)
            lineTo(21.64f, 5.36f)
            arcTo(1.2f, 1.2f, 0f, false, false, 21.64f, 3.64f)
            moveTo(14f, 7f)
            lineTo(17f, 10f)
            moveTo(5f, 6f)
            verticalLineTo(10f)
            moveTo(19f, 14f)
            verticalLineTo(18f)
            moveTo(10f, 2f)
            verticalLineTo(4f)
            moveTo(7f, 8f)
            horizontalLineTo(3f)
            moveTo(21f, 16f)
            horizontalLineTo(17f)
            moveTo(11f, 3f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedMagicWand01 = it }

/** `MagicWand01` vector, built on first access and reused afterwards. */
private var cachedMagicWand01: ImageVector? = null
