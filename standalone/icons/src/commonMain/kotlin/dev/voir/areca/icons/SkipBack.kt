package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Skip back icon.
 *
 * Converted from Lucide `skip-back` (ISC).
 */
public val ArecaIcons.SkipBack: ImageVector
    get() = cachedSkipBack ?: ImageVector.Builder(
        name = "SkipBack",
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
            moveTo(17.971f, 4.285f)
            arcTo(2f, 2f, 0f, false, true, 21f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 17.971f, 19.715f)
            lineTo(7.974f, 13.717f)
            arcTo(2f, 2f, 0f, false, true, 7.971f, 10.285f)
            close()
            moveTo(3f, 20f)
            verticalLineTo(4f)
        }
    }.build().also { cachedSkipBack = it }

/** `SkipBack` vector, built on first access and reused afterwards. */
private var cachedSkipBack: ImageVector? = null
