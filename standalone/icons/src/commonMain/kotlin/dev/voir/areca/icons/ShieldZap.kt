package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shield bolt icon.
 *
 * Converted from Tabler Icons `shield-bolt` (MIT).
 */
public val ArecaIcons.ShieldZap: ImageVector
    get() = cachedShieldZap ?: ImageVector.Builder(
        name = "ShieldZap",
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
            moveTo(13.342f, 20.566f)
            curveTo(12.906f, 20.736f, 12.458f, 20.881f, 12f, 21f)
            arcTo(12f, 12f, 0f, false, true, 3.5f, 6f)
            arcTo(12f, 12f, 0f, false, false, 12f, 3f)
            arcTo(12f, 12f, 0f, false, false, 20.5f, 6f)
            arcTo(12f, 12f, 0f, false, true, 20.617f, 12.34f)
            moveTo(19f, 16f)
            lineTo(17f, 19f)
            horizontalLineTo(21f)
            lineTo(19f, 22f)
        }
    }.build().also { cachedShieldZap = it }

/** `ShieldZap` vector, built on first access and reused afterwards. */
private var cachedShieldZap: ImageVector? = null
