package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gas station icon.
 *
 * Converted from Tabler Icons `gas-station` (MIT).
 */
public val ArecaIcons.GasStation: ImageVector
    get() = cachedGasStation ?: ImageVector.Builder(
        name = "GasStation",
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
            moveTo(14f, 11f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 17f, 13f)
            verticalLineTo(16f)
            arcTo(1.5f, 1.5f, 0f, false, false, 20f, 16f)
            verticalLineTo(9f)
            lineTo(17f, 6f)
            moveTo(4f, 20f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 14f, 6f)
            verticalLineTo(20f)
            moveTo(3f, 20f)
            lineTo(15f, 20f)
            moveTo(18f, 7f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 19f, 9f)
            horizontalLineTo(20f)
            moveTo(4f, 11f)
            lineTo(14f, 11f)
        }
    }.build().also { cachedGasStation = it }

/** `GasStation` vector, built on first access and reused afterwards. */
private var cachedGasStation: ImageVector? = null
