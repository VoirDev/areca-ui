package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mountain snow icon.
 *
 * Converted from Lucide `mountain-snow` (ISC).
 */
public val ArecaIcons.MountainSnow: ImageVector
    get() = cachedMountainSnow ?: ImageVector.Builder(
        name = "MountainSnow",
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
            moveTo(8f, 3f)
            lineTo(12f, 11f)
            lineTo(17f, 6f)
            lineTo(22f, 21f)
            horizontalLineTo(2f)
            lineTo(8f, 3f)
            close()
            moveTo(4.14f, 15.08f)
            curveTo(6.76f, 13.51f, 9.38f, 13.65f, 12f, 15.5f)
            curveTo(14.74f, 17.44f, 17.49f, 17.5f, 20.23f, 15.69f)
        }
    }.build().also { cachedMountainSnow = it }

/** `MountainSnow` vector, built on first access and reused afterwards. */
private var cachedMountainSnow: ImageVector? = null
