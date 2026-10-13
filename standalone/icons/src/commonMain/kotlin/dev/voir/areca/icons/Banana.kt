package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Banana icon.
 *
 * Converted from Lucide `banana` (ISC).
 */
public val ArecaIcons.Banana: ImageVector
    get() = cachedBanana ?: ImageVector.Builder(
        name = "Banana",
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
            moveTo(4f, 13f)
            curveTo(7.5f, 11f, 12f, 11f, 14f, 15f)
            arcTo(5.5f, 5.5f, 0f, false, true, 22f, 20f)
            moveTo(5.15f, 17.89f)
            curveTo(10.67f, 16.37f, 13.8f, 11f, 12.15f, 5.89f)
            curveTo(11.55f, 4f, 11.5f, 2f, 13f, 2f)
            curveTo(16.22f, 2f, 18f, 7.5f, 18f, 10f)
            curveTo(18f, 16.5f, 13.8f, 22f, 7.51f, 22f)
            curveTo(5.11f, 22f, 2f, 22f, 2f, 20f)
            curveTo(2f, 18.5f, 3.14f, 18.45f, 5.15f, 17.89f)
            close()
        }
    }.build().also { cachedBanana = it }

/** `Banana` vector, built on first access and reused afterwards. */
private var cachedBanana: ImageVector? = null
