package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud upload icon.
 *
 * Converted from Lucide `cloud-upload` (ISC).
 */
public val ArecaIcons.UploadCloud: ImageVector
    get() = cachedUploadCloud ?: ImageVector.Builder(
        name = "UploadCloud",
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
            moveTo(12f, 13f)
            verticalLineTo(21f)
            moveTo(4f, 14.899f)
            arcTo(7f, 7f, 0f, true, true, 15.71f, 8f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, false, true, 20f, 16.242f)
            moveTo(8f, 17f)
            lineTo(12f, 13f)
            lineTo(16f, 17f)
        }
    }.build().also { cachedUploadCloud = it }

/** `UploadCloud` vector, built on first access and reused afterwards. */
private var cachedUploadCloud: ImageVector? = null
