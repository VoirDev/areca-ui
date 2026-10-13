package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud moon icon.
 *
 * Converted from Lucide `cloud-moon` (ISC).
 */
public val ArecaIcons.CloudMoon: ImageVector
    get() = cachedCloudMoon ?: ImageVector.Builder(
        name = "CloudMoon",
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
            moveTo(13f, 16f)
            arcTo(3f, 3f, 0f, false, true, 13f, 22f)
            horizontalLineTo(7f)
            arcTo(5f, 5f, 0f, true, true, 11.9f, 16f)
            close()
            moveTo(18.376f, 14.512f)
            arcTo(6f, 6f, 0f, false, false, 21.837f, 10.385f)
            curveTo(21.985f, 9.76f, 21.178f, 9.415f, 20.589f, 9.671f)
            arcTo(4f, 4f, 0f, false, true, 15.33f, 4.411f)
            curveTo(15.585f, 3.822f, 15.24f, 3.016f, 14.614f, 3.163f)
            arcTo(6f, 6f, 0f, false, false, 10.02f, 8.523f)
        }
    }.build().also { cachedCloudMoon = it }

/** `CloudMoon` vector, built on first access and reused afterwards. */
private var cachedCloudMoon: ImageVector? = null
