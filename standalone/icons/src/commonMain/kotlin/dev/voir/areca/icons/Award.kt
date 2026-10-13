package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Award icon.
 *
 * Converted from Lucide `award` (ISC).
 */
public val ArecaIcons.Award: ImageVector
    get() = cachedAward ?: ImageVector.Builder(
        name = "Award",
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
            moveTo(15.477f, 12.89f)
            lineTo(16.992f, 21.416f)
            arcTo(0.5f, 0.5f, 0f, false, true, 16.182f, 21.886f)
            lineTo(12.602f, 19.199f)
            arcTo(1f, 1f, 0f, false, false, 11.405f, 19.199f)
            lineTo(7.819f, 21.885f)
            arcTo(0.5f, 0.5f, 0f, false, true, 7.009f, 21.416f)
            lineTo(8.523f, 12.89f)
            moveTo(6f, 8f)
            arcTo(6f, 6f, 0f, true, true, 18f, 8f)
            arcTo(6f, 6f, 0f, true, true, 6f, 8f)
            close()
        }
    }.build().also { cachedAward = it }

/** `Award` vector, built on first access and reused afterwards. */
private var cachedAward: ImageVector? = null
