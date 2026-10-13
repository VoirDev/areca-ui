package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fish symbol icon.
 *
 * Converted from Lucide `fish-symbol` (ISC).
 */
public val ArecaIcons.FishSymbol: ImageVector
    get() = cachedFishSymbol ?: ImageVector.Builder(
        name = "FishSymbol",
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
            moveTo(2f, 16f)
            reflectiveCurveTo(11f, 1f, 22f, 12f)
            curveTo(11f, 23f, 2f, 8f, 2f, 8f)
        }
    }.build().also { cachedFishSymbol = it }

/** `FishSymbol` vector, built on first access and reused afterwards. */
private var cachedFishSymbol: ImageVector? = null
