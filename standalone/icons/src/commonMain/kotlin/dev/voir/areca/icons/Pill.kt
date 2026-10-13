package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pill icon.
 *
 * Converted from Lucide `pill` (ISC).
 */
public val ArecaIcons.Pill: ImageVector
    get() = cachedPill ?: ImageVector.Builder(
        name = "Pill",
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
            moveTo(10.5f, 20.5f)
            lineTo(20.5f, 10.5f)
            arcTo(4.95f, 4.95f, 0f, true, false, 13.5f, 3.5f)
            lineTo(3.5f, 13.5f)
            arcTo(4.95f, 4.95f, 0f, true, false, 10.5f, 20.5f)
            close()
            moveTo(8.5f, 8.5f)
            lineTo(15.5f, 15.5f)
        }
    }.build().also { cachedPill = it }

/** `Pill` vector, built on first access and reused afterwards. */
private var cachedPill: ImageVector? = null
