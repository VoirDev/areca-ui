package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Citrus icon.
 *
 * Converted from Lucide `citrus` (ISC).
 */
public val ArecaIcons.Citrus: ImageVector
    get() = cachedCitrus ?: ImageVector.Builder(
        name = "Citrus",
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
            moveTo(21.66f, 17.67f)
            arcTo(1.08f, 1.08f, 0f, false, true, 21.62f, 19.27f)
            arcTo(12f, 12f, 0f, false, true, 4.73f, 2.38f)
            arcTo(1.1f, 1.1f, 0f, false, true, 6.34f, 2.34f)
            close()
            moveTo(19.65f, 15.66f)
            arcTo(8f, 8f, 0f, false, true, 8.35f, 4.34f)
            moveTo(14f, 10f)
            lineTo(8.5f, 15.5f)
            moveTo(14f, 17.85f)
            verticalLineTo(10f)
            horizontalLineTo(6.15f)
        }
    }.build().also { cachedCitrus = it }

/** `Citrus` vector, built on first access and reused afterwards. */
private var cachedCitrus: ImageVector? = null
