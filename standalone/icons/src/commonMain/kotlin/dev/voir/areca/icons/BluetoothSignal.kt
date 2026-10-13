package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bluetooth searching icon.
 *
 * Converted from Lucide `bluetooth-searching` (ISC).
 */
public val ArecaIcons.BluetoothSignal: ImageVector
    get() = cachedBluetoothSignal ?: ImageVector.Builder(
        name = "BluetoothSignal",
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
            moveTo(7f, 7f)
            lineTo(17f, 17f)
            lineTo(12f, 22f)
            verticalLineTo(2f)
            lineTo(17f, 7f)
            lineTo(7f, 17f)
            moveTo(20.83f, 14.83f)
            arcTo(4f, 4f, 0f, false, false, 20.83f, 9.17f)
            moveTo(18f, 12f)
            horizontalLineTo(18.01f)
        }
    }.build().also { cachedBluetoothSignal = it }

/** `BluetoothSignal` vector, built on first access and reused afterwards. */
private var cachedBluetoothSignal: ImageVector? = null
