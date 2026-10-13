package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lightbulb icon.
 *
 * Converted from Lucide `lightbulb` (ISC).
 */
public val ArecaIcons.Lightbulb: ImageVector
    get() = cachedLightbulb ?: ImageVector.Builder(
        name = "Lightbulb",
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
            moveTo(15f, 14f)
            curveTo(15.2f, 13f, 15.7f, 12.3f, 16.5f, 11.5f)
            curveTo(17.5f, 10.6f, 18f, 9.3f, 18f, 8f)
            arcTo(6f, 6f, 0f, false, false, 6f, 8f)
            curveTo(6f, 9f, 6.2f, 10.2f, 7.5f, 11.5f)
            curveTo(8.2f, 12.2f, 8.8f, 13f, 9f, 14f)
            moveTo(9f, 18f)
            horizontalLineTo(15f)
            moveTo(10f, 22f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedLightbulb = it }

/** `Lightbulb` vector, built on first access and reused afterwards. */
private var cachedLightbulb: ImageVector? = null
