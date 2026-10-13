package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sprout icon.
 *
 * Converted from Lucide `sprout` (ISC).
 */
public val ArecaIcons.Sprout: ImageVector
    get() = cachedSprout ?: ImageVector.Builder(
        name = "Sprout",
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
            moveTo(14f, 9.536f)
            verticalLineTo(7f)
            arcTo(4f, 4f, 0f, false, true, 18f, 3f)
            horizontalLineTo(19.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 20f, 3.5f)
            verticalLineTo(5f)
            arcTo(4f, 4f, 0f, false, true, 16f, 9f)
            arcTo(4f, 4f, 0f, false, false, 12f, 13f)
            curveTo(12f, 15f, 13f, 16f, 13f, 18f)
            arcTo(5f, 5f, 0f, false, true, 12f, 21f)
            moveTo(4f, 9f)
            arcTo(5f, 5f, 0f, false, true, 12f, 13f)
            arcTo(5f, 5f, 0f, false, true, 4f, 9f)
            moveTo(5f, 21f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedSprout = it }

/** `Sprout` vector, built on first access and reused afterwards. */
private var cachedSprout: ImageVector? = null
