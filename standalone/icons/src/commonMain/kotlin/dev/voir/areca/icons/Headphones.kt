package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Headphones icon.
 *
 * Converted from Lucide `headphones` (ISC).
 */
public val ArecaIcons.Headphones: ImageVector
    get() = cachedHeadphones ?: ImageVector.Builder(
        name = "Headphones",
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
            moveTo(3f, 14f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 8f, 16f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 6f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(12f)
            arcTo(9f, 9f, 0f, false, true, 21f, 12f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 16f, 19f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 14f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedHeadphones = it }

/** `Headphones` vector, built on first access and reused afterwards. */
private var cachedHeadphones: ImageVector? = null
