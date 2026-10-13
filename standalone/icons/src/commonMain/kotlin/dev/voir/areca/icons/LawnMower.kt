package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lawn mower icon.
 *
 * Converted from Tabler Icons `lawn-mower` (MIT).
 */
public val ArecaIcons.LawnMower: ImageVector
    get() = cachedLawnMower ?: ImageVector.Builder(
        name = "LawnMower",
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
            moveTo(6f, 11f)
            horizontalLineTo(11.38f)
            arcTo(1f, 1f, 0f, false, true, 12.28f, 11.55f)
            lineTo(13f, 13f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 14f)
            verticalLineTo(16f)
            moveTo(3f, 4f)
            horizontalLineTo(4.13f)
            arcTo(1f, 1f, 0f, false, true, 5.13f, 4.86f)
            lineTo(6.72f, 16f)
            moveTo(17f, 18f)
            horizontalLineTo(9f)
            moveTo(9f, 18f)
            arcTo(2f, 2f, 0f, true, true, 5f, 18f)
            arcTo(2f, 2f, 0f, false, true, 9f, 18f)
            moveTo(21f, 18f)
            arcTo(2f, 2f, 0f, true, true, 17f, 18f)
            arcTo(2f, 2f, 0f, false, true, 21f, 18f)
        }
    }.build().also { cachedLawnMower = it }

/** `LawnMower` vector, built on first access and reused afterwards. */
private var cachedLawnMower: ImageVector? = null
