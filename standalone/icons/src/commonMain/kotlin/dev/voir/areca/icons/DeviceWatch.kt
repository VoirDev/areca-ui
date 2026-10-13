package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device watch icon.
 *
 * Converted from Tabler Icons `device-watch` (MIT).
 */
public val ArecaIcons.DeviceWatch: ImageVector
    get() = cachedDeviceWatch ?: ImageVector.Builder(
        name = "DeviceWatch",
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
            moveTo(6f, 9f)
            arcTo(3f, 3f, 0f, false, true, 9f, 6f)
            horizontalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 18f, 9f)
            verticalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 15f, 18f)
            horizontalLineTo(9f)
            arcTo(3f, 3f, 0f, false, true, 6f, 15f)
            verticalLineTo(9f)
            moveTo(9f, 18f)
            verticalLineTo(21f)
            horizontalLineTo(15f)
            verticalLineTo(18f)
            moveTo(9f, 6f)
            verticalLineTo(3f)
            horizontalLineTo(15f)
            verticalLineTo(6f)
        }
    }.build().also { cachedDeviceWatch = it }

/** `DeviceWatch` vector, built on first access and reused afterwards. */
private var cachedDeviceWatch: ImageVector? = null
