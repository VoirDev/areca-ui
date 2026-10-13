package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cable car icon.
 *
 * Converted from Lucide `cable-car` (ISC).
 */
public val ArecaIcons.CableCar: ImageVector
    get() = cachedCableCar ?: ImageVector.Builder(
        name = "CableCar",
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
            moveTo(10f, 3f)
            horizontalLineTo(10.01f)
            moveTo(14f, 2f)
            horizontalLineTo(14.01f)
            moveTo(2f, 9f)
            lineTo(22f, 4f)
            moveTo(12f, 12f)
            verticalLineTo(6.5f)
            moveTo(7f, 12f)
            horizontalLineTo(17f)
            arcTo(3f, 3f, 0f, false, true, 20f, 15f)
            verticalLineTo(19f)
            arcTo(3f, 3f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 4f, 19f)
            verticalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 7f, 12f)
            close()
            moveTo(9f, 12f)
            verticalLineTo(17f)
            moveTo(15f, 12f)
            verticalLineTo(17f)
            moveTo(4f, 17f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedCableCar = it }

/** `CableCar` vector, built on first access and reused afterwards. */
private var cachedCableCar: ImageVector? = null
