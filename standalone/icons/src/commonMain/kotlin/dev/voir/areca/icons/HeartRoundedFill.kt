package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Filled heart icon.
 *
 * Converted from Tabler Icons `heart` (MIT), filled.
 */
public val ArecaIcons.HeartRoundedFill: ImageVector
    get() = cachedHeartRoundedFill ?: ImageVector.Builder(
        name = "HeartRoundedFill",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(19.5f, 12.572f)
            lineTo(12f, 20f)
            lineTo(4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, true, true, 19.5f, 12.578f)
        }
    }.build().also { cachedHeartRoundedFill = it }

/** `HeartRoundedFill` vector, built on first access and reused afterwards. */
private var cachedHeartRoundedFill: ImageVector? = null
