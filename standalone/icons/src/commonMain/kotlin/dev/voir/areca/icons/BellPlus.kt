package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bell plus icon.
 *
 * Converted from Lucide `bell-plus` (ISC).
 */
public val ArecaIcons.BellPlus: ImageVector
    get() = cachedBellPlus ?: ImageVector.Builder(
        name = "BellPlus",
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
            moveTo(10.268f, 21f)
            arcTo(2f, 2f, 0f, false, false, 13.732f, 21f)
            moveTo(15f, 8f)
            horizontalLineTo(21f)
            moveTo(18f, 5f)
            verticalLineTo(11f)
            moveTo(20.002f, 14.464f)
            arcTo(9f, 9f, 0f, false, false, 20.74f, 15.327f)
            arcTo(1f, 1f, 0f, false, true, 20f, 17f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3.26f, 15.327f)
            curveTo(4.59f, 13.956f, 6f, 12.499f, 6f, 8f)
            arcTo(6f, 6f, 0f, false, true, 14.75f, 2.668f)
        }
    }.build().also { cachedBellPlus = it }

/** `BellPlus` vector, built on first access and reused afterwards. */
private var cachedBellPlus: ImageVector? = null
