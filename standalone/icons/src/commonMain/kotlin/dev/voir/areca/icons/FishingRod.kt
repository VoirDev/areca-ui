package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fishing rod icon.
 *
 * Converted from Lucide `fishing-rod` (ISC).
 */
public val ArecaIcons.FishingRod: ImageVector
    get() = cachedFishingRod ?: ImageVector.Builder(
        name = "FishingRod",
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
            moveTo(4f, 11f)
            horizontalLineTo(5f)
            moveTo(8f, 15f)
            arcTo(2f, 2f, 0f, false, true, 4f, 15f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 5f, 2f)
            horizontalLineTo(5.5f)
            curveTo(14f, 2f, 20f, 9f, 20f, 18f)
            verticalLineTo(22f)
            moveTo(16f, 18f)
            arcTo(2f, 2f, 0f, true, true, 20f, 18f)
            arcTo(2f, 2f, 0f, true, true, 16f, 18f)
            close()
        }
    }.build().also { cachedFishingRod = it }

/** `FishingRod` vector, built on first access and reused afterwards. */
private var cachedFishingRod: ImageVector? = null
