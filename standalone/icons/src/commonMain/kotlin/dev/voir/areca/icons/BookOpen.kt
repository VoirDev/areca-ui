package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Book open icon.
 *
 * Converted from Lucide `book-open` (ISC).
 */
public val ArecaIcons.BookOpen: ImageVector
    get() = cachedBookOpen ?: ImageVector.Builder(
        name = "BookOpen",
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
            moveTo(12f, 5f)
            verticalLineTo(21f)
            moveTo(20.001f, 19f)
            arcTo(2f, 2f, 0f, false, false, 22f, 17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 20.001f, 3f)
            lineTo(16f, 3.002f)
            arcTo(5f, 5f, 0f, false, false, 12f, 5f)
            arcTo(5f, 5f, 0f, false, false, 8f, 3f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 5f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 3.999f, 19f)
            horizontalLineTo(8f)
            arcTo(5f, 5f, 0f, false, true, 12f, 21f)
            arcTo(5f, 5f, 0f, false, true, 16f, 19f)
            close()
        }
    }.build().also { cachedBookOpen = it }

/** `BookOpen` vector, built on first access and reused afterwards. */
private var cachedBookOpen: ImageVector? = null
