package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Drumstick icon.
 *
 * Converted from Lucide `drumstick` (ISC).
 */
public val ArecaIcons.Drumstick: ImageVector
    get() = cachedDrumstick ?: ImageVector.Builder(
        name = "Drumstick",
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
            moveTo(15.4f, 15.63f)
            arcTo(7.875f, 6f, 135f, true, true, 21.63f, 9.4f)
            arcTo(4.5f, 3.43f, 135f, false, false, 15.4f, 15.63f)
            moveTo(8.29f, 12.71f)
            lineTo(5.69f, 15.31f)
            arcTo(2.5f, 2.5f, 0f, true, false, 4.04f, 19.96f)
            arcTo(2.5f, 2.5f, 0f, true, false, 8.7f, 18.3f)
            lineTo(11.29f, 15.71f)
        }
    }.build().also { cachedDrumstick = it }

/** `Drumstick` vector, built on first access and reused afterwards. */
private var cachedDrumstick: ImageVector? = null
