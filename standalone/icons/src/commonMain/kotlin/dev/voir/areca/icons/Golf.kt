package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Golf icon.
 *
 * Converted from Tabler Icons `golf` (MIT).
 */
public val ArecaIcons.Golf: ImageVector
    get() = cachedGolf ?: ImageVector.Builder(
        name = "Golf",
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
            moveTo(12f, 18f)
            verticalLineTo(3f)
            lineTo(19f, 7f)
            lineTo(12f, 11f)
            moveTo(9f, 17.67f)
            curveTo(8.38f, 18.03f, 8f, 18.49f, 8f, 19f)
            curveTo(8f, 20.1f, 9.8f, 21f, 12f, 21f)
            reflectiveCurveTo(16f, 20.1f, 16f, 19f)
            curveTo(16f, 18.5f, 15.62f, 18.03f, 15f, 17.67f)
        }
    }.build().also { cachedGolf = it }

/** `Golf` vector, built on first access and reused afterwards. */
private var cachedGolf: ImageVector? = null
