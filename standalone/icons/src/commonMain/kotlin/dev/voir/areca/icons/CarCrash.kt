package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car crash icon.
 *
 * Converted from Tabler Icons `car-crash` (MIT).
 */
public val ArecaIcons.CarCrash: ImageVector
    get() = cachedCarCrash ?: ImageVector.Builder(
        name = "CarCrash",
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
            moveTo(8f, 17f)
            arcTo(2f, 2f, 0f, true, false, 12f, 17f)
            arcTo(2f, 2f, 0f, true, false, 8f, 17f)
            moveTo(7f, 6f)
            lineTo(11f, 11f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 14f, 13f)
            verticalLineTo(17f)
            horizontalLineTo(12f)
            moveTo(8f, 17f)
            horizontalLineTo(3f)
            moveTo(3f, 11f)
            horizontalLineTo(11f)
            moveTo(5f, 11f)
            verticalLineTo(6f)
            moveTo(7f, 6f)
            horizontalLineTo(3f)
            moveTo(14f, 8f)
            verticalLineTo(6f)
            moveTo(19f, 12f)
            horizontalLineTo(21f)
            moveTo(17.5f, 15.5f)
            lineTo(19f, 17f)
            moveTo(17.5f, 8.5f)
            lineTo(19f, 7f)
        }
    }.build().also { cachedCarCrash = it }

/** `CarCrash` vector, built on first access and reused afterwards. */
private var cachedCarCrash: ImageVector? = null
