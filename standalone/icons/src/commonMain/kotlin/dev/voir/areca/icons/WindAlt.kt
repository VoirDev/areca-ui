package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wind icon.
 *
 * Converted from Tabler Icons `wind` (MIT).
 */
public val ArecaIcons.WindAlt: ImageVector
    get() = cachedWindAlt ?: ImageVector.Builder(
        name = "WindAlt",
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
            moveTo(5f, 8f)
            horizontalLineTo(13.5f)
            arcTo(2.5f, 2.5f, 0f, true, false, 11.16f, 4.76f)
            moveTo(3f, 12f)
            horizontalLineTo(18.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 16.16f, 15.24f)
            moveTo(4f, 16f)
            horizontalLineTo(9.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 7.16f, 19.24f)
        }
    }.build().also { cachedWindAlt = it }

/** `WindAlt` vector, built on first access and reused afterwards. */
private var cachedWindAlt: ImageVector? = null
