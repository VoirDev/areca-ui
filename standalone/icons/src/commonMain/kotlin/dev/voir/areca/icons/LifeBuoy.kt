package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Life buoy icon.
 *
 * Converted from Lucide `life-buoy` (ISC).
 */
public val ArecaIcons.LifeBuoy: ImageVector
    get() = cachedLifeBuoy ?: ImageVector.Builder(
        name = "LifeBuoy",
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
            moveTo(4.93f, 4.93f)
            lineTo(9.17f, 9.17f)
            moveTo(14.83f, 9.17f)
            lineTo(19.07f, 4.93f)
            moveTo(14.83f, 14.83f)
            lineTo(19.07f, 19.07f)
            moveTo(9.17f, 14.83f)
            lineTo(4.93f, 19.07f)
            moveTo(8f, 12f)
            arcTo(4f, 4f, 0f, true, true, 16f, 12f)
            arcTo(4f, 4f, 0f, true, true, 8f, 12f)
            close()
        }
    }.build().also { cachedLifeBuoy = it }

/** `LifeBuoy` vector, built on first access and reused afterwards. */
private var cachedLifeBuoy: ImageVector? = null
