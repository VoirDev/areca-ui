package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device tv old icon.
 *
 * Converted from Tabler Icons `device-tv-old` (MIT).
 */
public val ArecaIcons.DeviceTvOld: ImageVector
    get() = cachedDeviceTvOld ?: ImageVector.Builder(
        name = "DeviceTvOld",
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
            moveTo(3f, 9f)
            arcTo(2f, 2f, 0f, false, true, 5f, 7f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 9f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 19f, 20f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 18f)
            lineTo(3f, 9f)
            moveTo(16f, 3f)
            lineTo(12f, 7f)
            lineTo(8f, 3f)
            moveTo(15f, 7f)
            verticalLineTo(20f)
            moveTo(18f, 15f)
            verticalLineTo(15.01f)
            moveTo(18f, 12f)
            verticalLineTo(12.01f)
        }
    }.build().also { cachedDeviceTvOld = it }

/** `DeviceTvOld` vector, built on first access and reused afterwards. */
private var cachedDeviceTvOld: ImageVector? = null
