package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud lightning icon.
 *
 * Converted from Lucide `cloud-lightning` (ISC).
 */
public val ArecaIcons.CloudLightning: ImageVector
    get() = cachedCloudLightning ?: ImageVector.Builder(
        name = "CloudLightning",
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
            moveTo(6f, 16.326f)
            arcTo(7f, 7f, 0f, true, true, 15.71f, 8f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, false, true, 18f, 16.973f)
            moveTo(13f, 12f)
            lineTo(10f, 17f)
            horizontalLineTo(14f)
            lineTo(11f, 22f)
        }
    }.build().also { cachedCloudLightning = it }

/** `CloudLightning` vector, built on first access and reused afterwards. */
private var cachedCloudLightning: ImageVector? = null
