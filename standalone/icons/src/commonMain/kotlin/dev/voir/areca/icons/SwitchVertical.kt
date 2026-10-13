package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Switch vertical icon.
 *
 * Converted from Tabler Icons `switch-vertical` (MIT).
 */
public val ArecaIcons.SwitchVertical: ImageVector
    get() = cachedSwitchVertical ?: ImageVector.Builder(
        name = "SwitchVertical",
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
            moveTo(3f, 8f)
            lineTo(7f, 4f)
            lineTo(11f, 8f)
            moveTo(7f, 4f)
            lineTo(7f, 13f)
            moveTo(13f, 16f)
            lineTo(17f, 20f)
            lineTo(21f, 16f)
            moveTo(17f, 10f)
            lineTo(17f, 20f)
        }
    }.build().also { cachedSwitchVertical = it }

/** `SwitchVertical` vector, built on first access and reused afterwards. */
private var cachedSwitchVertical: ImageVector? = null
