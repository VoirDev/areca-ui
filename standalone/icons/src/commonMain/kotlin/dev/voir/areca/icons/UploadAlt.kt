package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow up to line icon.
 *
 * Converted from Lucide `arrow-up-to-line` (ISC).
 */
public val ArecaIcons.UploadAlt: ImageVector
    get() = cachedUploadAlt ?: ImageVector.Builder(
        name = "UploadAlt",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            moveTo(18f, 13f)
            lineTo(12f, 7f)
            lineTo(6f, 13f)
            moveTo(12f, 7f)
            verticalLineTo(21f)
        }
    }.build().also { cachedUploadAlt = it }

/** `UploadAlt` vector, built on first access and reused afterwards. */
private var cachedUploadAlt: ImageVector? = null
