package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cherry icon.
 *
 * Converted from Lucide `cherry` (ISC).
 */
public val ArecaIcons.Cherry: ImageVector
    get() = cachedCherry ?: ImageVector.Builder(
        name = "Cherry",
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
            moveTo(2f, 17f)
            arcTo(5f, 5f, 0f, false, false, 12f, 17f)
            curveTo(12f, 14.24f, 9.5f, 12f, 7f, 14f)
            curveTo(4.5f, 12f, 2f, 14.24f, 2f, 17f)
            close()
            moveTo(12f, 17f)
            arcTo(5f, 5f, 0f, false, false, 22f, 17f)
            curveTo(22f, 14.24f, 19.5f, 12f, 17f, 14f)
            curveTo(14.5f, 12f, 12f, 14.24f, 12f, 17f)
            close()
            moveTo(7f, 14f)
            curveTo(10.22f, 11.09f, 11.29f, 5.25f, 12f, 2f)
            curveTo(13.66f, 4.38f, 16.94f, 11f, 17f, 14f)
            moveTo(22f, 9f)
            curveTo(17.71f, 9f, 14.86f, 6.67f, 12f, 2f)
            curveTo(17.71f, 2f, 22f, 6.67f, 22f, 9f)
            close()
        }
    }.build().also { cachedCherry = it }

/** `Cherry` vector, built on first access and reused afterwards. */
private var cachedCherry: ImageVector? = null
