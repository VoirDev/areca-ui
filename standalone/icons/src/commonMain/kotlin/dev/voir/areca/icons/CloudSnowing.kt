package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud snow icon.
 *
 * Converted from Lucide `cloud-snow` (ISC).
 */
public val ArecaIcons.CloudSnowing: ImageVector
    get() = cachedCloudSnowing ?: ImageVector.Builder(
        name = "CloudSnowing",
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
            moveTo(4f, 14.899f)
            arcTo(7f, 7f, 0f, true, true, 15.71f, 8f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, false, true, 20f, 16.242f)
            moveTo(8f, 15f)
            horizontalLineTo(8.01f)
            moveTo(8f, 19f)
            horizontalLineTo(8.01f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
            moveTo(12f, 21f)
            horizontalLineTo(12.01f)
            moveTo(16f, 15f)
            horizontalLineTo(16.01f)
            moveTo(16f, 19f)
            horizontalLineTo(16.01f)
        }
    }.build().also { cachedCloudSnowing = it }

/** `CloudSnowing` vector, built on first access and reused afterwards. */
private var cachedCloudSnowing: ImageVector? = null
