package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cooking pot icon.
 *
 * Converted from Lucide `cooking-pot` (ISC).
 */
public val ArecaIcons.CookingPot: ImageVector
    get() = cachedCookingPot ?: ImageVector.Builder(
        name = "CookingPot",
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
            moveTo(2f, 12f)
            horizontalLineTo(22f)
            moveTo(20f, 12f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(12f)
            moveTo(4f, 8f)
            lineTo(20f, 4f)
            moveTo(8.86f, 6.78f)
            lineTo(8.41f, 4.97f)
            arcTo(2f, 2f, 0f, false, true, 9.86f, 2.54f)
            lineTo(11.8f, 2.06f)
            arcTo(2f, 2f, 0f, false, true, 14.23f, 3.52f)
            lineTo(14.68f, 5.32f)
        }
    }.build().also { cachedCookingPot = it }

/** `CookingPot` vector, built on first access and reused afterwards. */
private var cachedCookingPot: ImageVector? = null
