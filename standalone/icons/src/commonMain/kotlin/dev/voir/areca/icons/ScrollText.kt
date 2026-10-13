package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scroll text icon.
 *
 * Converted from Lucide `scroll-text` (ISC).
 */
public val ArecaIcons.ScrollText: ImageVector
    get() = cachedScrollText ?: ImageVector.Builder(
        name = "ScrollText",
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
            moveTo(15f, 12f)
            horizontalLineTo(10f)
            moveTo(15f, 8f)
            horizontalLineTo(10f)
            moveTo(19f, 17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 17f, 3f)
            horizontalLineTo(4f)
            moveTo(8f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, false, 21f, 17f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 10f, 18f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, true, true, 6f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, true, false, 2f, 5f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 3f, 8f)
            horizontalLineTo(6f)
        }
    }.build().also { cachedScrollText = it }

/** `ScrollText` vector, built on first access and reused afterwards. */
private var cachedScrollText: ImageVector? = null
