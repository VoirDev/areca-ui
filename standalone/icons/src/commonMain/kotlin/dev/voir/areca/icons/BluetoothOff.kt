package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bluetooth off icon.
 *
 * Converted from Lucide `bluetooth-off` (ISC).
 */
public val ArecaIcons.BluetoothOff: ImageVector
    get() = cachedBluetoothOff ?: ImageVector.Builder(
        name = "BluetoothOff",
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
            moveTo(17f, 17f)
            lineTo(12f, 22f)
            verticalLineTo(12f)
            lineTo(7f, 17f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(14.5f, 9.5f)
            lineTo(17f, 7f)
            lineTo(12f, 2f)
            verticalLineTo(6.5f)
        }
    }.build().also { cachedBluetoothOff = it }

/** `BluetoothOff` vector, built on first access and reused afterwards. */
private var cachedBluetoothOff: ImageVector? = null
