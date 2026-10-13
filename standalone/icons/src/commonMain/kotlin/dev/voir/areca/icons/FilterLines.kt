package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * List filter icon.
 *
 * Converted from Lucide `list-filter` (ISC).
 */
public val ArecaIcons.FilterLines: ImageVector
    get() = cachedFilterLines ?: ImageVector.Builder(
        name = "FilterLines",
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
            moveTo(2f, 5f)
            horizontalLineTo(22f)
            moveTo(6f, 12f)
            horizontalLineTo(18f)
            moveTo(9f, 19f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedFilterLines = it }

/** `FilterLines` vector, built on first access and reused afterwards. */
private var cachedFilterLines: ImageVector? = null
