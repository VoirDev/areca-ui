package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rocket icon.
 *
 * Converted from Tabler Icons `rocket` (MIT).
 */
public val ArecaIcons.RocketLaunch: ImageVector
    get() = cachedRocketLaunch ?: ImageVector.Builder(
        name = "RocketLaunch",
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
            moveTo(4f, 13f)
            arcTo(8f, 8f, 0f, false, true, 11f, 20f)
            arcTo(6f, 6f, 0f, false, false, 14f, 15f)
            arcTo(9f, 9f, 0f, false, false, 20f, 7f)
            arcTo(3f, 3f, 0f, false, false, 17f, 4f)
            arcTo(9f, 9f, 0f, false, false, 9f, 10f)
            arcTo(6f, 6f, 0f, false, false, 4f, 13f)
            moveTo(7f, 14f)
            arcTo(6f, 6f, 0f, false, false, 4f, 20f)
            arcTo(6f, 6f, 0f, false, false, 10f, 17f)
            moveTo(14f, 9f)
            arcTo(1f, 1f, 0f, true, false, 16f, 9f)
            arcTo(1f, 1f, 0f, true, false, 14f, 9f)
        }
    }.build().also { cachedRocketLaunch = it }

/** `RocketLaunch` vector, built on first access and reused afterwards. */
private var cachedRocketLaunch: ImageVector? = null
