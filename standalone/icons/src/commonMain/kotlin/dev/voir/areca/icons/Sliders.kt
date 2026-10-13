package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sliders vertical icon.
 *
 * Converted from Lucide `sliders-vertical` (ISC).
 */
public val ArecaIcons.Sliders: ImageVector
    get() = cachedSliders ?: ImageVector.Builder(
        name = "Sliders",
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
            moveTo(10f, 8f)
            horizontalLineTo(14f)
            moveTo(12f, 21f)
            verticalLineTo(12f)
            moveTo(12f, 8f)
            verticalLineTo(3f)
            moveTo(17f, 16f)
            horizontalLineTo(21f)
            moveTo(19f, 12f)
            verticalLineTo(3f)
            moveTo(19f, 21f)
            verticalLineTo(16f)
            moveTo(3f, 14f)
            horizontalLineTo(7f)
            moveTo(5f, 10f)
            verticalLineTo(3f)
            moveTo(5f, 21f)
            verticalLineTo(14f)
        }
    }.build().also { cachedSliders = it }

/** `Sliders` vector, built on first access and reused afterwards. */
private var cachedSliders: ImageVector? = null
