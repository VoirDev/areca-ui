package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Strikethrough icon.
 *
 * Converted from Tabler Icons `strikethrough` (MIT).
 */
public val ArecaIcons.StrikethroughAlt: ImageVector
    get() = cachedStrikethroughAlt ?: ImageVector.Builder(
        name = "StrikethroughAlt",
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
            moveTo(5f, 12f)
            lineTo(19f, 12f)
            moveTo(16f, 6.5f)
            arcTo(4f, 2f, 0f, false, false, 12f, 5f)
            horizontalLineTo(11f)
            arcTo(3.5f, 3.5f, 0f, false, false, 11f, 12f)
            horizontalLineTo(13f)
            arcTo(3.5f, 3.5f, 0f, false, true, 13f, 19f)
            horizontalLineTo(11.5f)
            arcTo(4f, 2f, 0f, false, true, 7.5f, 17.5f)
        }
    }.build().also { cachedStrikethroughAlt = it }

/** `StrikethroughAlt` vector, built on first access and reused afterwards. */
private var cachedStrikethroughAlt: ImageVector? = null
