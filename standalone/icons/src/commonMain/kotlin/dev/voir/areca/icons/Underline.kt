package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Underline icon.
 *
 * Converted from Lucide `underline` (ISC).
 */
public val ArecaIcons.Underline: ImageVector
    get() = cachedUnderline ?: ImageVector.Builder(
        name = "Underline",
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
            moveTo(6f, 4f)
            verticalLineTo(10f)
            arcTo(6f, 6f, 0f, false, false, 18f, 10f)
            verticalLineTo(4f)
            moveTo(4f, 20f)
            lineTo(20f, 20f)
        }
    }.build().also { cachedUnderline = it }

/** `Underline` vector, built on first access and reused afterwards. */
private var cachedUnderline: ImageVector? = null
