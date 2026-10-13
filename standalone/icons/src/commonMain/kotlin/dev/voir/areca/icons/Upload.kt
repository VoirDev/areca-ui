package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Upload icon.
 *
 * Converted from Lucide `upload` (ISC).
 */
public val ArecaIcons.Upload: ImageVector
    get() = cachedUpload ?: ImageVector.Builder(
        name = "Upload",
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
            moveTo(12f, 3f)
            verticalLineTo(15f)
            moveTo(17f, 8f)
            lineTo(12f, 3f)
            lineTo(7f, 8f)
            moveTo(21f, 15f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(15f)
        }
    }.build().also { cachedUpload = it }

/** `Upload` vector, built on first access and reused afterwards. */
private var cachedUpload: ImageVector? = null
