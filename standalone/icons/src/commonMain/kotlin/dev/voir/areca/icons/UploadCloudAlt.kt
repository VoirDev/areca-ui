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
 * Converted from Tabler Icons `cloud-upload` (MIT).
 */
public val ArecaIcons.UploadCloudAlt: ImageVector
    get() = cachedUploadCloudAlt ?: ImageVector.Builder(
        name = "UploadCloudAlt",
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
            moveTo(7f, 18f)
            arcTo(4.6f, 4.4f, 0f, false, true, 7f, 9f)
            arcTo(5f, 4.5f, 0f, false, true, 18f, 11f)
            horizontalLineTo(19f)
            arcTo(3.5f, 3.5f, 0f, false, true, 19f, 18f)
            horizontalLineTo(18f)
            moveTo(9f, 15f)
            lineTo(12f, 12f)
            lineTo(15f, 15f)
            moveTo(12f, 12f)
            lineTo(12f, 21f)
        }
    }.build().also { cachedUploadCloudAlt = it }

/** `UploadCloudAlt` vector, built on first access and reused afterwards. */
private var cachedUploadCloudAlt: ImageVector? = null
