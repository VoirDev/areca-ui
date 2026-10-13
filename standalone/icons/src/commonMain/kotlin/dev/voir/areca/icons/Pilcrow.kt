package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pilcrow icon.
 *
 * Converted from Lucide `pilcrow` (ISC).
 */
public val ArecaIcons.Pilcrow: ImageVector
    get() = cachedPilcrow ?: ImageVector.Builder(
        name = "Pilcrow",
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
            moveTo(13f, 4f)
            verticalLineTo(20f)
            moveTo(17f, 4f)
            verticalLineTo(20f)
            moveTo(19f, 4f)
            horizontalLineTo(9.5f)
            arcTo(4.5f, 4.5f, 0f, false, false, 9.5f, 13f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedPilcrow = it }

/** `Pilcrow` vector, built on first access and reused afterwards. */
private var cachedPilcrow: ImageVector? = null
