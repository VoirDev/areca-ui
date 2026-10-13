package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Coffee icon.
 *
 * Converted from Lucide `coffee` (ISC).
 */
public val ArecaIcons.Coffee: ImageVector
    get() = cachedCoffee ?: ImageVector.Builder(
        name = "Coffee",
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
            moveTo(10f, 2f)
            verticalLineTo(4f)
            moveTo(14f, 2f)
            verticalLineTo(4f)
            moveTo(16f, 8f)
            arcTo(1f, 1f, 0f, false, true, 17f, 9f)
            verticalLineTo(17f)
            arcTo(4f, 4f, 0f, false, true, 13f, 21f)
            horizontalLineTo(7f)
            arcTo(4f, 4f, 0f, false, true, 3f, 17f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 4f, 8f)
            horizontalLineTo(18f)
            arcTo(4f, 4f, 0f, true, true, 18f, 16f)
            horizontalLineTo(17f)
            moveTo(6f, 2f)
            verticalLineTo(4f)
        }
    }.build().also { cachedCoffee = it }

/** `Coffee` vector, built on first access and reused afterwards. */
private var cachedCoffee: ImageVector? = null
