package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bookmark check icon.
 *
 * Converted from Lucide `bookmark-check` (ISC).
 */
public val ArecaIcons.BookmarkCheck: ImageVector
    get() = cachedBookmarkCheck ?: ImageVector.Builder(
        name = "BookmarkCheck",
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
            moveTo(9f, 10f)
            lineTo(11f, 12f)
            lineTo(15f, 8f)
        }
    }.build().also { cachedBookmarkCheck = it }

/** `BookmarkCheck` vector, built on first access and reused afterwards. */
private var cachedBookmarkCheck: ImageVector? = null
