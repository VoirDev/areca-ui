package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flag icon.
 *
 * Converted from Lucide `flag` (ISC).
 */
public val ArecaIcons.Flag01: ImageVector
    get() = cachedFlag01 ?: ImageVector.Builder(
        name = "Flag01",
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
            moveTo(4f, 22f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 4.4f, 3.2f)
            arcTo(6f, 6f, 0f, false, true, 8f, 2f)
            curveTo(11f, 2f, 13f, 4f, 15.333f, 4f)
            quadTo(17.333f, 4f, 18.4f, 3.2f)
            arcTo(1f, 1f, 0f, false, true, 20f, 4f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 19.6f, 14.8f)
            arcTo(6f, 6f, 0f, false, true, 16f, 16f)
            curveTo(13f, 16f, 11f, 14f, 8f, 14f)
            arcTo(6f, 6f, 0f, false, false, 4f, 15.528f)
        }
    }.build().also { cachedFlag01 = it }

/** `Flag01` vector, built on first access and reused afterwards. */
private var cachedFlag01: ImageVector? = null
