package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vaccine icon.
 *
 * Converted from Tabler Icons `vaccine` (MIT).
 */
public val ArecaIcons.Vaccine: ImageVector
    get() = cachedVaccine ?: ImageVector.Builder(
        name = "Vaccine",
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
            moveTo(17f, 3f)
            lineTo(21f, 7f)
            moveTo(19f, 5f)
            lineTo(14.5f, 9.5f)
            moveTo(11.5f, 6.5f)
            lineTo(17.5f, 12.5f)
            moveTo(16.5f, 11.5f)
            lineTo(10f, 18f)
            horizontalLineTo(6f)
            verticalLineTo(14f)
            lineTo(12.5f, 7.5f)
            moveTo(7.5f, 12.5f)
            lineTo(9f, 14f)
            moveTo(10.5f, 9.5f)
            lineTo(12f, 11f)
            moveTo(3f, 21f)
            lineTo(6f, 18f)
        }
    }.build().also { cachedVaccine = it }

/** `Vaccine` vector, built on first access and reused afterwards. */
private var cachedVaccine: ImageVector? = null
