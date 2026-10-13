package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clapperboard icon.
 *
 * Converted from Lucide `clapperboard` (ISC).
 */
public val ArecaIcons.Clapperboard: ImageVector
    get() = cachedClapperboard ?: ImageVector.Builder(
        name = "Clapperboard",
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
            moveTo(12.296f, 3.464f)
            lineTo(15.316f, 7.42f)
            moveTo(20.2f, 6f)
            lineTo(3f, 11f)
            lineTo(2.1f, 8.6f)
            curveTo(1.8f, 7.5f, 2.4f, 6.4f, 3.4f, 6.1f)
            lineTo(16.9f, 2.1f)
            curveTo(18f, 1.8f, 19.1f, 2.4f, 19.4f, 3.4f)
            close()
            moveTo(3f, 11f)
            horizontalLineTo(21f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            close()
            moveTo(6.18f, 5.276f)
            lineTo(9.28f, 9.175f)
        }
    }.build().also { cachedClapperboard = it }

/** `Clapperboard` vector, built on first access and reused afterwards. */
private var cachedClapperboard: ImageVector? = null
