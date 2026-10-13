package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car garage icon.
 *
 * Converted from Tabler Icons `car-garage` (MIT).
 */
public val ArecaIcons.CarGarage: ImageVector
    get() = cachedCarGarage ?: ImageVector.Builder(
        name = "CarGarage",
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
            moveTo(5f, 20f)
            arcTo(2f, 2f, 0f, true, false, 9f, 20f)
            arcTo(2f, 2f, 0f, false, false, 5f, 20f)
            moveTo(15f, 20f)
            arcTo(2f, 2f, 0f, true, false, 19f, 20f)
            arcTo(2f, 2f, 0f, false, false, 15f, 20f)
            moveTo(5f, 20f)
            horizontalLineTo(3f)
            verticalLineTo(14f)
            lineTo(5f, 9f)
            horizontalLineTo(14f)
            lineTo(18f, 14f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 16f)
            verticalLineTo(20f)
            horizontalLineTo(19f)
            moveTo(15f, 20f)
            horizontalLineTo(9f)
            moveTo(3f, 14f)
            horizontalLineTo(18f)
            moveTo(12f, 14f)
            verticalLineTo(9f)
            moveTo(3f, 6f)
            lineTo(12f, 2f)
            lineTo(21f, 6f)
        }
    }.build().also { cachedCarGarage = it }

/** `CarGarage` vector, built on first access and reused afterwards. */
private var cachedCarGarage: ImageVector? = null
