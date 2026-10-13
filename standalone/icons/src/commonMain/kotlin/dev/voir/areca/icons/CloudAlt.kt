package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud icon.
 *
 * Converted from Tabler Icons `cloud` (MIT).
 */
public val ArecaIcons.CloudAlt: ImageVector
    get() = cachedCloudAlt ?: ImageVector.Builder(
        name = "CloudAlt",
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
            moveTo(6.657f, 18f)
            curveTo(4.085f, 18f, 2f, 15.993f, 2f, 13.517f)
            curveTo(2f, 11.042f, 4.085f, 9.035f, 6.657f, 9.035f)
            curveTo(7.05f, 7.273f, 8.451f, 5.835f, 10.332f, 5.262f)
            curveTo(12.212f, 4.69f, 14.288f, 5.069f, 15.776f, 6.262f)
            curveTo(17.264f, 7.452f, 17.938f, 9.269f, 17.546f, 11.031f)
            horizontalLineTo(18.536f)
            curveTo(20.449f, 11.031f, 22f, 12.591f, 22f, 14.517f)
            curveTo(22f, 16.444f, 20.449f, 18.004f, 18.535f, 18.004f)
            horizontalLineTo(6.657f)
        }
    }.build().also { cachedCloudAlt = it }

/** `CloudAlt` vector, built on first access and reused afterwards. */
private var cachedCloudAlt: ImageVector? = null
