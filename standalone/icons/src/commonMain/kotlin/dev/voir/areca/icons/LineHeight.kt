package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Line height icon.
 *
 * Converted from Tabler Icons `line-height` (MIT).
 */
public val ArecaIcons.LineHeight: ImageVector
    get() = cachedLineHeight ?: ImageVector.Builder(
        name = "LineHeight",
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
            moveTo(3f, 8f)
            lineTo(6f, 5f)
            lineTo(9f, 8f)
            moveTo(3f, 16f)
            lineTo(6f, 19f)
            lineTo(9f, 16f)
            moveTo(6f, 5f)
            lineTo(6f, 19f)
            moveTo(13f, 6f)
            lineTo(20f, 6f)
            moveTo(13f, 12f)
            lineTo(20f, 12f)
            moveTo(13f, 18f)
            lineTo(20f, 18f)
        }
    }.build().also { cachedLineHeight = it }

/** `LineHeight` vector, built on first access and reused afterwards. */
private var cachedLineHeight: ImageVector? = null
