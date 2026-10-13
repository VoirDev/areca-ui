package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Code icon.
 *
 * Converted from Lucide `code` (ISC).
 */
public val ArecaIcons.Code: ImageVector
    get() = cachedCode ?: ImageVector.Builder(
        name = "Code",
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
            moveTo(16f, 18f)
            lineTo(22f, 12f)
            lineTo(16f, 6f)
            moveTo(8f, 6f)
            lineTo(2f, 12f)
            lineTo(8f, 18f)
        }
    }.build().also { cachedCode = it }

/** `Code` vector, built on first access and reused afterwards. */
private var cachedCode: ImageVector? = null
