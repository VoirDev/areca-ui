package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scooter electric icon.
 *
 * Converted from Tabler Icons `scooter-electric` (MIT).
 */
public val ArecaIcons.ScooterElectric: ImageVector
    get() = cachedScooterElectric ?: ImageVector.Builder(
        name = "ScooterElectric",
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
            moveTo(16f, 17f)
            arcTo(2f, 2f, 0f, true, false, 20f, 17f)
            arcTo(2f, 2f, 0f, true, false, 16f, 17f)
            moveTo(4f, 17f)
            arcTo(2f, 2f, 0f, true, false, 8f, 17f)
            arcTo(2f, 2f, 0f, true, false, 4f, 17f)
            moveTo(8f, 17f)
            horizontalLineTo(13f)
            arcTo(6f, 6f, 0f, false, true, 18f, 12f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 16f, 5f)
            horizontalLineTo(15f)
            moveTo(10f, 4f)
            lineTo(8f, 8f)
            horizontalLineTo(11f)
            lineTo(9f, 12f)
        }
    }.build().also { cachedScooterElectric = it }

/** `ScooterElectric` vector, built on first access and reused afterwards. */
private var cachedScooterElectric: ImageVector? = null
