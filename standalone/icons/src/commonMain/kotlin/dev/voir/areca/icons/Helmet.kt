package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Helmet icon.
 *
 * Converted from Tabler Icons `helmet` (MIT).
 */
public val ArecaIcons.Helmet: ImageVector
    get() = cachedHelmet ?: ImageVector.Builder(
        name = "Helmet",
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
            moveTo(12f, 4f)
            arcTo(9f, 9f, 0f, false, true, 17.656f, 20f)
            horizontalLineTo(6.344f)
            arcTo(9f, 9f, 0f, false, true, 12f, 4f)
            moveTo(20f, 9f)
            horizontalLineTo(11.2f)
            arcTo(1f, 1f, 0f, false, false, 10.232f, 10.246f)
            curveTo(10.739f, 12.246f, 11.828f, 13.664f, 13.5f, 14.5f)
            curveTo(15.5f, 15.5f, 17.833f, 16f, 20.5f, 16f)
        }
    }.build().also { cachedHelmet = it }

/** `Helmet` vector, built on first access and reused afterwards. */
private var cachedHelmet: ImageVector? = null
