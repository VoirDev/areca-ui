package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bluetooth connected icon.
 *
 * Converted from Lucide `bluetooth-connected` (ISC).
 */
public val ArecaIcons.BluetoothConnect: ImageVector
    get() = cachedBluetoothConnect ?: ImageVector.Builder(
        name = "BluetoothConnect",
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
            moveTo(18f, 12f)
            lineTo(21f, 12f)
            moveTo(3f, 12f)
            lineTo(6f, 12f)
        }
    }.build().also { cachedBluetoothConnect = it }

/** `BluetoothConnect` vector, built on first access and reused afterwards. */
private var cachedBluetoothConnect: ImageVector? = null
