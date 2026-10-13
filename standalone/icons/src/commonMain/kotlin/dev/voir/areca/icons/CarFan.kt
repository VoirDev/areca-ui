package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car fan icon.
 *
 * Converted from Tabler Icons `car-fan` (MIT).
 */
public val ArecaIcons.CarFan: ImageVector
    get() = cachedCarFan ?: ImageVector.Builder(
        name = "CarFan",
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
            moveTo(12f, 12f)
            verticalLineTo(3f)
            lineTo(16.912f, 4.914f)
            arcTo(1.7f, 1.7f, 0f, false, true, 17.34f, 7.839f)
            lineTo(12f, 12f)
            moveTo(12f, 12f)
            horizontalLineTo(21f)
            lineTo(19.086f, 16.912f)
            arcTo(1.7f, 1.7f, 0f, false, true, 16.161f, 17.34f)
            lineTo(12f, 12f)
            moveTo(12f, 12f)
            horizontalLineTo(3f)
            lineTo(4.914f, 7.088f)
            arcTo(1.7f, 1.7f, 0f, false, true, 7.839f, 6.66f)
            lineTo(12f, 12f)
            moveTo(12f, 12f)
            verticalLineTo(21f)
            lineTo(7.088f, 19.086f)
            arcTo(1.7f, 1.7f, 0f, false, true, 6.66f, 16.161f)
            lineTo(12f, 12f)
        }
    }.build().also { cachedCarFan = it }

/** `CarFan` vector, built on first access and reused afterwards. */
private var cachedCarFan: ImageVector? = null
