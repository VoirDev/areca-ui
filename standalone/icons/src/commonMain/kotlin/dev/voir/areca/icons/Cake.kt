package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cake icon.
 *
 * Converted from Lucide `cake` (ISC).
 */
public val ArecaIcons.Cake: ImageVector
    get() = cachedCake ?: ImageVector.Builder(
        name = "Cake",
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
            moveTo(20f, 21f)
            verticalLineTo(13f)
            arcTo(2f, 2f, 0f, false, false, 18f, 11f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 4f, 13f)
            verticalLineTo(21f)
            moveTo(4f, 16f)
            reflectiveCurveTo(4.5f, 15f, 6f, 15f)
            reflectiveCurveTo(8.5f, 17f, 10f, 17f)
            reflectiveCurveTo(12.5f, 15f, 14f, 15f)
            reflectiveCurveTo(16.5f, 17f, 18f, 17f)
            reflectiveCurveTo(20f, 16f, 20f, 16f)
            moveTo(2f, 21f)
            horizontalLineTo(22f)
            moveTo(7f, 8f)
            verticalLineTo(11f)
            moveTo(12f, 8f)
            verticalLineTo(11f)
            moveTo(17f, 8f)
            verticalLineTo(11f)
            moveTo(7f, 4f)
            horizontalLineTo(7.01f)
            moveTo(12f, 4f)
            horizontalLineTo(12.01f)
            moveTo(17f, 4f)
            horizontalLineTo(17.01f)
        }
    }.build().also { cachedCake = it }

/** `Cake` vector, built on first access and reused afterwards. */
private var cachedCake: ImageVector? = null
