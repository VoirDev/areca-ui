package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Link 2 icon.
 *
 * Converted from Lucide `link-2` (ISC).
 */
public val ArecaIcons.LinkAlt: ImageVector
    get() = cachedLinkAlt ?: ImageVector.Builder(
        name = "LinkAlt",
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
            moveTo(9f, 17f)
            horizontalLineTo(7f)
            arcTo(5f, 5f, 0f, false, true, 7f, 7f)
            horizontalLineTo(9f)
            moveTo(15f, 7f)
            horizontalLineTo(17f)
            arcTo(5f, 5f, 0f, true, true, 17f, 17f)
            horizontalLineTo(15f)
            moveTo(8f, 12f)
            lineTo(16f, 12f)
        }
    }.build().also { cachedLinkAlt = it }

/** `LinkAlt` vector, built on first access and reused afterwards. */
private var cachedLinkAlt: ImageVector? = null
