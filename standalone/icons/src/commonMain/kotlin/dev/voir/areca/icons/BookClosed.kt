package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Book icon.
 *
 * Converted from Lucide `book` (ISC).
 */
public val ArecaIcons.BookClosed: ImageVector
    get() = cachedBookClosed ?: ImageVector.Builder(
        name = "BookClosed",
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
            moveTo(4f, 19.5f)
            verticalLineTo(4.5f)
            arcTo(2.5f, 2.5f, 0f, false, true, 6.5f, 2f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 3f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 19f, 22f)
            horizontalLineTo(6.5f)
            arcTo(1f, 1f, 0f, false, true, 6.5f, 17f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedBookClosed = it }

/** `BookClosed` vector, built on first access and reused afterwards. */
private var cachedBookClosed: ImageVector? = null
