package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow down to line icon.
 *
 * Converted from Lucide `arrow-down-to-line` (ISC).
 */
public val ArecaIcons.DownloadAlt: ImageVector
    get() = cachedDownloadAlt ?: ImageVector.Builder(
        name = "DownloadAlt",
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
            moveTo(12f, 17f)
            verticalLineTo(3f)
            moveTo(6f, 11f)
            lineTo(12f, 17f)
            lineTo(18f, 11f)
            moveTo(19f, 21f)
            horizontalLineTo(5f)
        }
    }.build().also { cachedDownloadAlt = it }

/** `DownloadAlt` vector, built on first access and reused afterwards. */
private var cachedDownloadAlt: ImageVector? = null
