package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device usb icon.
 *
 * Converted from Tabler Icons `device-usb` (MIT).
 */
public val ArecaIcons.UsbFlashDrive: ImageVector
    get() = cachedUsbFlashDrive ?: ImageVector.Builder(
        name = "UsbFlashDrive",
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
            moveTo(7f, 8f)
            horizontalLineTo(17f)
            verticalLineTo(16f)
            arcTo(5f, 5f, 0f, false, true, 7f, 16f)
            lineTo(7f, 8f)
            moveTo(9f, 8f)
            verticalLineTo(3f)
            horizontalLineTo(15f)
            verticalLineTo(8f)
        }
    }.build().also { cachedUsbFlashDrive = it }

/** `UsbFlashDrive` vector, built on first access and reused afterwards. */
private var cachedUsbFlashDrive: ImageVector? = null
