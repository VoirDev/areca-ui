package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coins icon.
 *
 * Same drawing as `ArecaIcons.CoinsStacked`; both names are kept.
 *
 * Converted from Tabler Icons `coins` (MIT).
 */
public val ArecaIcons.Gold: ImageVector
    get() = cachedGold ?: ImageVector.Builder(
        name = "Gold",
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
            moveTo(9f, 14f)
            curveTo(9f, 15.657f, 11.686f, 17f, 15f, 17f)
            reflectiveCurveTo(21f, 15.657f, 21f, 14f)
            reflectiveCurveTo(18.314f, 11f, 15f, 11f)
            reflectiveCurveTo(9f, 12.343f, 9f, 14f)
            moveTo(9f, 14f)
            verticalLineTo(18f)
            curveTo(9f, 19.656f, 11.686f, 21f, 15f, 21f)
            reflectiveCurveTo(21f, 19.656f, 21f, 18f)
            verticalLineTo(14f)
            moveTo(3f, 6f)
            curveTo(3f, 7.072f, 4.144f, 8.062f, 6f, 8.598f)
            reflectiveCurveTo(10.144f, 9.134f, 12f, 8.598f)
            curveTo(13.856f, 8.062f, 15f, 7.072f, 15f, 6f)
            curveTo(15f, 4.928f, 13.856f, 3.938f, 12f, 3.402f)
            reflectiveCurveTo(7.856f, 2.866f, 6f, 3.402f)
            curveTo(4.144f, 3.938f, 3f, 4.928f, 3f, 6f)
            moveTo(3f, 6f)
            verticalLineTo(16f)
            curveTo(3f, 16.888f, 3.772f, 17.45f, 5f, 18f)
            moveTo(3f, 11f)
            curveTo(3f, 11.888f, 3.772f, 12.45f, 5f, 13f)
        }
    }.build().also { cachedGold = it }

/** `Gold` vector, built on first access and reused afterwards. */
private var cachedGold: ImageVector? = null
