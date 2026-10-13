package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sunglasses icon.
 *
 * Converted from Tabler Icons `sunglasses` (MIT).
 */
public val ArecaIcons.Sunglasses: ImageVector
    get() = cachedSunglasses ?: ImageVector.Builder(
        name = "Sunglasses",
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
            moveTo(8f, 4f)
            horizontalLineTo(6f)
            lineTo(3f, 14f)
            moveTo(16f, 4f)
            horizontalLineTo(18f)
            lineTo(21f, 14f)
            moveTo(10f, 16f)
            horizontalLineTo(14f)
            moveTo(21f, 16.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 14f, 16.5f)
            verticalLineTo(14f)
            horizontalLineTo(21f)
            verticalLineTo(16.5f)
            moveTo(10f, 16.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 3f, 16.5f)
            verticalLineTo(14f)
            horizontalLineTo(10f)
            verticalLineTo(16.5f)
            moveTo(4f, 14f)
            lineTo(8.5f, 18.5f)
            moveTo(15f, 14f)
            lineTo(19.5f, 18.5f)
        }
    }.build().also { cachedSunglasses = it }

/** `Sunglasses` vector, built on first access and reused afterwards. */
private var cachedSunglasses: ImageVector? = null
