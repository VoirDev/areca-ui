package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Unlink icon.
 *
 * Converted from Lucide `unlink` (ISC).
 */
public val ArecaIcons.LinkBroken: ImageVector
    get() = cachedLinkBroken ?: ImageVector.Builder(
        name = "LinkBroken",
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
            moveTo(18.84f, 12.25f)
            lineTo(20.56f, 10.54f)
            horizontalLineTo(20.54f)
            arcTo(5.004f, 5.004f, 0f, false, false, 20.42f, 3.47f)
            arcTo(5.006f, 5.006f, 0f, false, false, 13.47f, 3.47f)
            lineTo(11.75f, 5.18f)
            moveTo(5.17f, 11.75f)
            lineTo(3.46f, 13.46f)
            arcTo(5.004f, 5.004f, 0f, false, false, 3.58f, 20.53f)
            arcTo(5.006f, 5.006f, 0f, false, false, 10.53f, 20.53f)
            lineTo(12.24f, 18.82f)
            moveTo(8f, 2f)
            lineTo(8f, 5f)
            moveTo(2f, 8f)
            lineTo(5f, 8f)
            moveTo(16f, 19f)
            lineTo(16f, 22f)
            moveTo(19f, 16f)
            lineTo(22f, 16f)
        }
    }.build().also { cachedLinkBroken = it }

/** `LinkBroken` vector, built on first access and reused afterwards. */
private var cachedLinkBroken: ImageVector? = null
