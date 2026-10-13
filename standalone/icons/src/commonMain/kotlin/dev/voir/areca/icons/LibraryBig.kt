package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Library big icon.
 *
 * Converted from Lucide `library-big` (ISC).
 */
public val ArecaIcons.LibraryBig: ImageVector
    get() = cachedLibraryBig ?: ImageVector.Builder(
        name = "LibraryBig",
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
            moveTo(4f, 3f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 11f, 4f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 10f, 21f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 20f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 4f, 3f)
            close()
            moveTo(7f, 3f)
            verticalLineTo(21f)
            moveTo(20.4f, 18.9f)
            curveTo(20.6f, 19.4f, 20.3f, 20f, 19.8f, 20.2f)
            lineTo(17.9f, 20.9f)
            curveTo(17.4f, 21.1f, 16.8f, 20.8f, 16.6f, 20.3f)
            lineTo(11.1f, 5.1f)
            curveTo(10.9f, 4.6f, 11.2f, 4f, 11.7f, 3.8f)
            lineTo(13.6f, 3.1f)
            curveTo(14.1f, 2.9f, 14.7f, 3.2f, 14.9f, 3.7f)
            close()
        }
    }.build().also { cachedLibraryBig = it }

/** `LibraryBig` vector, built on first access and reused afterwards. */
private var cachedLibraryBig: ImageVector? = null
