package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Device mobile dollar icon.
 *
 * Converted from Tabler Icons `device-mobile-dollar` (MIT).
 */
public val ArecaIcons.DeviceMobileDollar: ImageVector
    get() = cachedDeviceMobileDollar ?: ImageVector.Builder(
        name = "DeviceMobileDollar",
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
            moveTo(13f, 21f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 6f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 8f, 3f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 5f)
            verticalLineTo(10f)
            moveTo(11f, 4f)
            horizontalLineTo(13f)
            moveTo(12f, 17f)
            verticalLineTo(17.01f)
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 18f)
            horizontalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
        }
    }.build().also { cachedDeviceMobileDollar = it }

/** `DeviceMobileDollar` vector, built on first access and reused afterwards. */
private var cachedDeviceMobileDollar: ImageVector? = null
