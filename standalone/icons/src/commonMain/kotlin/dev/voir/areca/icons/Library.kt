package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Library icon.
 *
 * Converted from Lucide `library` (ISC).
 */
public val ArecaIcons.Library: ImageVector
    get() = cachedLibrary ?: ImageVector.Builder(
        name = "Library",
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
            moveTo(16f, 6f)
            lineTo(20f, 20f)
            moveTo(12f, 6f)
            verticalLineTo(20f)
            moveTo(8f, 8f)
            verticalLineTo(20f)
            moveTo(4f, 4f)
            verticalLineTo(20f)
        }
    }.build().also { cachedLibrary = it }

/** `Library` vector, built on first access and reused afterwards. */
private var cachedLibrary: ImageVector? = null
