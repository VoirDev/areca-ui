package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow up down icon.
 *
 * Converted from Lucide `arrow-up-down` (ISC).
 */
public val ArecaIcons.SwitchVerticalAlt: ImageVector
    get() = cachedSwitchVerticalAlt ?: ImageVector.Builder(
        name = "SwitchVerticalAlt",
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
            moveTo(21f, 16f)
            lineTo(17f, 20f)
            lineTo(13f, 16f)
            moveTo(17f, 20f)
            verticalLineTo(4f)
            moveTo(3f, 8f)
            lineTo(7f, 4f)
            lineTo(11f, 8f)
            moveTo(7f, 4f)
            verticalLineTo(20f)
        }
    }.build().also { cachedSwitchVerticalAlt = it }

/** `SwitchVerticalAlt` vector, built on first access and reused afterwards. */
private var cachedSwitchVerticalAlt: ImageVector? = null
