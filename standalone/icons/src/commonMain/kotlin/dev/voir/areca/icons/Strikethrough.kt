package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Strikethrough icon.
 *
 * Converted from Lucide `strikethrough` (ISC).
 */
public val ArecaIcons.Strikethrough: ImageVector
    get() = cachedStrikethrough ?: ImageVector.Builder(
        name = "Strikethrough",
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
            moveTo(16f, 4f)
            horizontalLineTo(9f)
            arcTo(3f, 3f, 0f, false, false, 6.17f, 8f)
            moveTo(14f, 12f)
            arcTo(4f, 4f, 0f, false, true, 14f, 20f)
            horizontalLineTo(6f)
            moveTo(4f, 12f)
            lineTo(20f, 12f)
        }
    }.build().also { cachedStrikethrough = it }

/** `Strikethrough` vector, built on first access and reused afterwards. */
private var cachedStrikethrough: ImageVector? = null
