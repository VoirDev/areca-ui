package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * World pin icon.
 *
 * Converted from Tabler Icons `world-pin` (MIT).
 */
public val ArecaIcons.EarthPin: ImageVector
    get() = cachedEarthPin ?: ImageVector.Builder(
        name = "EarthPin",
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
            moveTo(20.972f, 11.291f)
            arcTo(9f, 9f, 0f, true, false, 12.65f, 20.977f)
            moveTo(3.6f, 9f)
            horizontalLineTo(20.4f)
            moveTo(3.6f, 15f)
            horizontalLineTo(12.5f)
            moveTo(11.5f, 3f)
            arcTo(17f, 17f, 0f, false, false, 11.5f, 21f)
            moveTo(12.5f, 3f)
            arcTo(16.986f, 16.986f, 0f, false, true, 15.078f, 12.018f)
            moveTo(21.121f, 20.121f)
            arcTo(3f, 3f, 0f, true, false, 16.879f, 20.121f)
            curveTo(17.297f, 20.54f, 18.004f, 21.166f, 19f, 22f)
            curveTo(20.051f, 21.11f, 20.759f, 20.484f, 21.121f, 20.121f)
            moveTo(19f, 18f)
            verticalLineTo(18.01f)
        }
    }.build().also { cachedEarthPin = it }

/** `EarthPin` vector, built on first access and reused afterwards. */
private var cachedEarthPin: ImageVector? = null
