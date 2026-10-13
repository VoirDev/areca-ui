package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Home shield icon.
 *
 * Converted from Tabler Icons `home-shield` (MIT).
 */
public val ArecaIcons.HomeShield: ImageVector
    get() = cachedHomeShield ?: ImageVector.Builder(
        name = "HomeShield",
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
            moveTo(5f, 12f)
            horizontalLineTo(3f)
            lineTo(12f, 3f)
            lineTo(19.636f, 10.636f)
            moveTo(5f, 12f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 7f, 21f)
            horizontalLineTo(12f)
            moveTo(9f, 21f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 11f, 13f)
            horizontalLineTo(12.5f)
            moveTo(22f, 16f)
            curveTo(22f, 20f, 19.5f, 22f, 18.5f, 22f)
            reflectiveCurveTo(15f, 20f, 15f, 16f)
            curveTo(16f, 16f, 17.5f, 15.5f, 18.5f, 14.5f)
            curveTo(19.5f, 15.5f, 21f, 16f, 22f, 16f)
        }
    }.build().also { cachedHomeShield = it }

/** `HomeShield` vector, built on first access and reused afterwards. */
private var cachedHomeShield: ImageVector? = null
