package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Percent icon.
 *
 * Converted from Lucide `percent` (ISC).
 */
public val ArecaIcons.Percent: ImageVector
    get() = cachedPercent ?: ImageVector.Builder(
        name = "Percent",
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
            moveTo(19f, 5f)
            lineTo(5f, 19f)
            moveTo(4f, 6.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 9f, 6.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 4f, 6.5f)
            close()
            moveTo(15f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 20f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 15f, 17.5f)
            close()
        }
    }.build().also { cachedPercent = it }

/** `Percent` vector, built on first access and reused afterwards. */
private var cachedPercent: ImageVector? = null
