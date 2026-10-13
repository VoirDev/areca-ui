package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Home dollar icon.
 *
 * Converted from Tabler Icons `home-dollar` (MIT).
 */
public val ArecaIcons.HomeDollar: ImageVector
    get() = cachedHomeDollar ?: ImageVector.Builder(
        name = "HomeDollar",
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
            moveTo(19f, 10f)
            lineTo(12f, 3f)
            lineTo(3f, 12f)
            horizontalLineTo(5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 7f, 21f)
            horizontalLineTo(13f)
            moveTo(9f, 21f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 11f, 13f)
            horizontalLineTo(13f)
            curveTo(13.387f, 13f, 13.748f, 13.11f, 14.054f, 13.3f)
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 18f)
            horizontalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
        }
    }.build().also { cachedHomeDollar = it }

/** `HomeDollar` vector, built on first access and reused afterwards. */
private var cachedHomeDollar: ImageVector? = null
