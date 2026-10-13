package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud download icon.
 *
 * Converted from Tabler Icons `cloud-download` (MIT).
 */
public val ArecaIcons.DownloadCloudAlt: ImageVector
    get() = cachedDownloadCloudAlt ?: ImageVector.Builder(
        name = "DownloadCloudAlt",
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
            moveTo(19f, 18f)
            arcTo(3.5f, 3.5f, 0f, false, false, 19f, 11f)
            horizontalLineTo(18f)
            arcTo(5f, 4.5f, 0f, false, false, 7f, 9f)
            arcTo(4.6f, 4.4f, 0f, false, false, 4.9f, 17.4f)
            moveTo(12f, 13f)
            lineTo(12f, 22f)
            moveTo(9f, 19f)
            lineTo(12f, 22f)
            lineTo(15f, 19f)
        }
    }.build().also { cachedDownloadCloudAlt = it }

/** `DownloadCloudAlt` vector, built on first access and reused afterwards. */
private var cachedDownloadCloudAlt: ImageVector? = null
