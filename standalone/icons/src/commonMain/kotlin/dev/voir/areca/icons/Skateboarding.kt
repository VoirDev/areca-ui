package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Skateboarding icon.
 *
 * Converted from Tabler Icons `skateboarding` (MIT).
 */
public val ArecaIcons.Skateboarding: ImageVector
    get() = cachedSkateboarding ?: ImageVector.Builder(
        name = "Skateboarding",
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
            moveTo(5.5f, 15f)
            horizontalLineTo(9f)
            lineTo(9.75f, 13.5f)
            moveTo(14f, 19f)
            verticalLineTo(14f)
            lineTo(11.5f, 11f)
            lineTo(14f, 7f)
            moveTo(8f, 8f)
            lineTo(11f, 7f)
            horizontalLineTo(15f)
            lineTo(16f, 10f)
            horizontalLineTo(19f)
            moveTo(17.5f, 21f)
            arcTo(0.5f, 0.5f, 0f, true, false, 17.5f, 20f)
            arcTo(0.5f, 0.5f, 0f, false, false, 17.5f, 21f)
            moveTo(3f, 18f)
            curveTo(3f, 18.552f, 3.895f, 19f, 5f, 19f)
            horizontalLineTo(19f)
            curveTo(20.105f, 19f, 21f, 18.552f, 21f, 18f)
            moveTo(6.5f, 21f)
            arcTo(0.5f, 0.5f, 0f, true, false, 6.5f, 20f)
            arcTo(0.5f, 0.5f, 0f, false, false, 6.5f, 21f)
            moveTo(14.007f, 4f)
            arcTo(2f, 2f, 0f, true, false, 18.007f, 4f)
            arcTo(2f, 2f, 0f, true, false, 14.007f, 4f)
        }
    }.build().also { cachedSkateboarding = it }

/** `Skateboarding` vector, built on first access and reused afterwards. */
private var cachedSkateboarding: ImageVector? = null
