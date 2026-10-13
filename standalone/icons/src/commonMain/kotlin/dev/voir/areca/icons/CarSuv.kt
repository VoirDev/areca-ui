package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car suv icon.
 *
 * Converted from Tabler Icons `car-suv` (MIT).
 */
public val ArecaIcons.CarSuv: ImageVector
    get() = cachedCarSuv ?: ImageVector.Builder(
        name = "CarSuv",
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
            moveTo(5f, 17f)
            arcTo(2f, 2f, 0f, true, false, 9f, 17f)
            arcTo(2f, 2f, 0f, false, false, 5f, 17f)
            moveTo(16f, 17f)
            arcTo(2f, 2f, 0f, true, false, 20f, 17f)
            arcTo(2f, 2f, 0f, false, false, 16f, 17f)
            moveTo(5f, 9f)
            lineTo(7f, 5f)
            horizontalLineTo(14.438f)
            arcTo(2f, 2f, 0f, false, true, 16.378f, 6.515f)
            lineTo(17f, 9f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 11f)
            verticalLineTo(14f)
            moveTo(10f, 9f)
            verticalLineTo(5f)
            moveTo(2f, 7f)
            verticalLineTo(11f)
            moveTo(22.001f, 14.001f)
            arcTo(4.992f, 4.992f, 0f, false, false, 18f, 12f)
            arcTo(4.992f, 4.992f, 0f, false, false, 14f, 14f)
            horizontalLineTo(11f)
            arcTo(4.998f, 4.998f, 0f, false, false, 2.997f, 14.003f)
            moveTo(5f, 12f)
            verticalLineTo(9f)
            horizontalLineTo(18f)
        }
    }.build().also { cachedCarSuv = it }

/** `CarSuv` vector, built on first access and reused afterwards. */
private var cachedCarSuv: ImageVector? = null
