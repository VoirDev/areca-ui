package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bluetooth icon.
 *
 * Converted from Lucide `bluetooth` (ISC).
 */
public val ArecaIcons.BluetoothOn: ImageVector
    get() = cachedBluetoothOn ?: ImageVector.Builder(
        name = "BluetoothOn",
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
        }
    }.build().also { cachedBluetoothOn = it }

/** `BluetoothOn` vector, built on first access and reused afterwards. */
private var cachedBluetoothOn: ImageVector? = null
