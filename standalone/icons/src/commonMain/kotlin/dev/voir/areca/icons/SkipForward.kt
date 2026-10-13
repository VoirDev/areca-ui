package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Skip forward icon.
 *
 * Converted from Lucide `skip-forward` (ISC).
 */
public val ArecaIcons.SkipForward: ImageVector
    get() = cachedSkipForward ?: ImageVector.Builder(
        name = "SkipForward",
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
            moveTo(21f, 4f)
            verticalLineTo(20f)
            moveTo(6.029f, 4.285f)
            arcTo(2f, 2f, 0f, false, false, 3f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 6.029f, 19.715f)
            lineTo(16.026f, 13.717f)
            arcTo(2f, 2f, 0f, false, false, 16.029f, 10.285f)
            close()
        }
    }.build().also { cachedSkipForward = it }

/** `SkipForward` vector, built on first access and reused afterwards. */
private var cachedSkipForward: ImageVector? = null
