package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fuel icon.
 *
 * Converted from Lucide `fuel` (ISC).
 */
public val ArecaIcons.Fuel: ImageVector
    get() = cachedFuel ?: ImageVector.Builder(
        name = "Fuel",
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
            moveTo(14f, 13f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 15f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 22f, 17f)
            verticalLineTo(10.002f)
            arcTo(2f, 2f, 0f, false, false, 21.41f, 8.582f)
            lineTo(18f, 5f)
            moveTo(14f, 21f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 12f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(21f)
            moveTo(2f, 21f)
            horizontalLineTo(15f)
            moveTo(3f, 9f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedFuel = it }

/** `Fuel` vector, built on first access and reused afterwards. */
private var cachedFuel: ImageVector? = null
