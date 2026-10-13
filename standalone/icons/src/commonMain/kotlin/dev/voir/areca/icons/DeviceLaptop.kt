package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device laptop icon.
 *
 * Converted from Tabler Icons `device-laptop` (MIT).
 */
public val ArecaIcons.DeviceLaptop: ImageVector
    get() = cachedDeviceLaptop ?: ImageVector.Builder(
        name = "DeviceLaptop",
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
            moveTo(3f, 19f)
            lineTo(21f, 19f)
            moveTo(5f, 7f)
            arcTo(1f, 1f, 0f, false, true, 6f, 6f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 7f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 18f, 16f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 5f, 15f)
            lineTo(5f, 7f)
        }
    }.build().also { cachedDeviceLaptop = it }

/** `DeviceLaptop` vector, built on first access and reused afterwards. */
private var cachedDeviceLaptop: ImageVector? = null
