package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Airplay icon.
 *
 * Converted from Lucide `airplay` (ISC).
 */
public val ArecaIcons.Airplay: ImageVector
    get() = cachedAirplay ?: ImageVector.Builder(
        name = "Airplay",
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
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 15f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 20f, 17f)
            horizontalLineTo(19f)
            moveTo(12f, 15f)
            lineTo(17f, 21f)
            horizontalLineTo(7f)
            close()
        }
    }.build().also { cachedAirplay = it }

/** `Airplay` vector, built on first access and reused afterwards. */
private var cachedAirplay: ImageVector? = null
