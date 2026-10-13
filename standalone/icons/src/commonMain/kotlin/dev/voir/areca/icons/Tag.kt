package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tag icon.
 *
 * Converted from Lucide `tag` (ISC).
 */
public val ArecaIcons.Tag: ImageVector
    get() = cachedTag ?: ImageVector.Builder(
        name = "Tag",
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
            moveTo(12.586f, 2.586f)
            arcTo(2f, 2f, 0f, false, false, 11.172f, 2f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 4f)
            verticalLineTo(11.172f)
            arcTo(2f, 2f, 0f, false, false, 2.586f, 12.586f)
            lineTo(11.29f, 21.29f)
            arcTo(2.426f, 2.426f, 0f, false, false, 14.71f, 21.29f)
            lineTo(21.29f, 14.71f)
            arcTo(2.426f, 2.426f, 0f, false, false, 21.29f, 11.29f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(7f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 8f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 7f, 7.5f)
            close()
        }
    }.build().also { cachedTag = it }

/** `Tag` vector, built on first access and reused afterwards. */
private var cachedTag: ImageVector? = null
