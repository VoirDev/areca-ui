package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Switch horizontal icon.
 *
 * Converted from Tabler Icons `switch-horizontal` (MIT).
 */
public val ArecaIcons.SwitchHorizontal: ImageVector
    get() = cachedSwitchHorizontal ?: ImageVector.Builder(
        name = "SwitchHorizontal",
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
            moveTo(16f, 3f)
            lineTo(20f, 7f)
            lineTo(16f, 11f)
            moveTo(10f, 7f)
            lineTo(20f, 7f)
            moveTo(8f, 13f)
            lineTo(4f, 17f)
            lineTo(8f, 21f)
            moveTo(4f, 17f)
            lineTo(13f, 17f)
        }
    }.build().also { cachedSwitchHorizontal = it }

/** `SwitchHorizontal` vector, built on first access and reused afterwards. */
private var cachedSwitchHorizontal: ImageVector? = null
