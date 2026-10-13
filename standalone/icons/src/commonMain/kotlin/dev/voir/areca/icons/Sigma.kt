package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sigma icon.
 *
 * Converted from Lucide `sigma` (ISC).
 */
public val ArecaIcons.Sigma: ImageVector
    get() = cachedSigma ?: ImageVector.Builder(
        name = "Sigma",
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
            moveTo(18f, 7f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 17f, 4f)
            horizontalLineTo(6.5f)
            arcTo(0.5f, 0.5f, 0f, false, false, 6.1f, 4.8f)
            lineTo(10.6f, 10.8f)
            arcTo(2f, 2f, 0f, false, true, 10.6f, 13.2f)
            lineTo(6.1f, 19.2f)
            arcTo(0.5f, 0.5f, 0f, false, false, 6.5f, 20f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 18f, 19f)
            verticalLineTo(17f)
        }
    }.build().also { cachedSigma = it }

/** `Sigma` vector, built on first access and reused afterwards. */
private var cachedSigma: ImageVector? = null
