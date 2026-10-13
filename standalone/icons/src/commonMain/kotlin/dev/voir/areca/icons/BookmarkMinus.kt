package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bookmark minus icon.
 *
 * Converted from Lucide `bookmark-minus` (ISC).
 */
public val ArecaIcons.BookmarkMinus: ImageVector
    get() = cachedBookmarkMinus ?: ImageVector.Builder(
        name = "BookmarkMinus",
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
            moveTo(15f, 10f)
            horizontalLineTo(9f)
            moveTo(17f, 3f)
            arcTo(2f, 2f, 0f, false, true, 19f, 5f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 17.504f, 20.868f)
            lineTo(12.992f, 18.29f)
            arcTo(2f, 2f, 0f, false, false, 11.008f, 18.29f)
            lineTo(6.496f, 20.868f)
            arcTo(1f, 1f, 0f, false, true, 5f, 20f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 7f, 3f)
            close()
        }
    }.build().also { cachedBookmarkMinus = it }

/** `BookmarkMinus` vector, built on first access and reused afterwards. */
private var cachedBookmarkMinus: ImageVector? = null
