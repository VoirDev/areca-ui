package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coin bitcoin icon.
 *
 * Converted from Tabler Icons `coin-bitcoin` (MIT).
 */
public val ArecaIcons.Cryptocurrency: ImageVector
    get() = cachedCryptocurrency ?: ImageVector.Builder(
        name = "Cryptocurrency",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(9f, 8f)
            horizontalLineTo(13.09f)
            curveTo(14.145f, 8f, 15f, 8.895f, 15f, 10f)
            reflectiveCurveTo(14.145f, 12f, 13.09f, 12f)
            curveTo(14.145f, 12f, 15f, 12.895f, 15f, 14f)
            reflectiveCurveTo(14.145f, 16f, 13.09f, 16f)
            horizontalLineTo(9f)
            moveTo(10f, 12f)
            horizontalLineTo(14f)
            moveTo(10f, 7f)
            verticalLineTo(17f)
            verticalLineTo(8f)
            moveTo(13f, 7f)
            verticalLineTo(8f)
            moveTo(13f, 16f)
            verticalLineTo(17f)
        }
    }.build().also { cachedCryptocurrency = it }

/** `Cryptocurrency` vector, built on first access and reused afterwards. */
private var cachedCryptocurrency: ImageVector? = null
