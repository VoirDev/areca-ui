package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ban icon.
 *
 * Converted from Lucide `ban` (ISC).
 */
public val ArecaIcons.Forbidden: ImageVector
    get() = cachedForbidden ?: ImageVector.Builder(
        name = "Forbidden",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(4.929f, 4.929f)
            lineTo(19.07f, 19.071f)
        }
    }.build().also { cachedForbidden = it }

/** `Forbidden` vector, built on first access and reused afterwards. */
private var cachedForbidden: ImageVector? = null
