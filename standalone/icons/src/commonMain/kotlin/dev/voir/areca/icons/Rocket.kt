package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rocket icon.
 *
 * Converted from Lucide `rocket` (ISC).
 */
public val ArecaIcons.Rocket: ImageVector
    get() = cachedRocket ?: ImageVector.Builder(
        name = "Rocket",
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
            moveTo(12f, 15f)
            verticalLineTo(20f)
            reflectiveCurveTo(15.03f, 19.45f, 16f, 18f)
            curveTo(17.08f, 16.38f, 16f, 13f, 16f, 13f)
            moveTo(4.5f, 16.5f)
            curveTo(3f, 17.76f, 2.5f, 21.5f, 2.5f, 21.5f)
            reflectiveCurveTo(6.24f, 21f, 7.5f, 19.5f)
            curveTo(8.21f, 18.66f, 8.2f, 17.37f, 7.41f, 16.59f)
            arcTo(2.18f, 2.18f, 0f, false, false, 4.5f, 16.5f)
            moveTo(9f, 12f)
            arcTo(22f, 22f, 0f, false, true, 11f, 8.05f)
            arcTo(12.88f, 12.88f, 0f, false, true, 22f, 2f)
            curveTo(22f, 4.72f, 21.22f, 9.5f, 16f, 13f)
            arcTo(22.4f, 22.4f, 0f, false, true, 12f, 15f)
            close()
            moveTo(9f, 12f)
            horizontalLineTo(4f)
            reflectiveCurveTo(4.55f, 8.97f, 6f, 8f)
            curveTo(7.62f, 6.92f, 11f, 8.05f, 11f, 8.05f)
        }
    }.build().also { cachedRocket = it }

/** `Rocket` vector, built on first access and reused afterwards. */
private var cachedRocket: ImageVector? = null
