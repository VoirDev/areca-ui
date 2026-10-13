package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Smart home icon.
 *
 * Converted from Tabler Icons `smart-home` (MIT).
 */
public val ArecaIcons.HomeSmile: ImageVector
    get() = cachedHomeSmile ?: ImageVector.Builder(
        name = "HomeSmile",
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
            moveTo(19f, 8.71f)
            lineTo(13.667f, 4.562f)
            arcTo(2.666f, 2.666f, 0f, false, false, 10.393f, 4.562f)
            lineTo(5.059f, 8.71f)
            arcTo(2.665f, 2.665f, 0f, false, false, 4.03f, 10.815f)
            verticalLineTo(18.015f)
            arcTo(2f, 2f, 0f, false, false, 6.03f, 20.015f)
            horizontalLineTo(18.03f)
            arcTo(2f, 2f, 0f, false, false, 20.03f, 18.015f)
            verticalLineTo(10.815f)
            curveTo(20.03f, 9.992f, 19.65f, 9.215f, 19f, 8.71f)
            moveTo(16f, 15f)
            curveTo(13.79f, 16.333f, 10.208f, 16.333f, 8f, 15f)
        }
    }.build().also { cachedHomeSmile = it }

/** `HomeSmile` vector, built on first access and reused afterwards. */
private var cachedHomeSmile: ImageVector? = null
