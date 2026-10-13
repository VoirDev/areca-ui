package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Percentage icon.
 *
 * Converted from Tabler Icons `percentage` (MIT).
 */
public val ArecaIcons.Percentage: ImageVector
    get() = cachedPercentage ?: ImageVector.Builder(
        name = "Percentage",
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
            arcTo(1f, 1f, 0f, true, false, 18f, 17f)
            arcTo(1f, 1f, 0f, true, false, 16f, 17f)
            moveTo(6f, 7f)
            arcTo(1f, 1f, 0f, true, false, 8f, 7f)
            arcTo(1f, 1f, 0f, true, false, 6f, 7f)
            moveTo(6f, 18f)
            lineTo(18f, 6f)
        }
    }.build().also { cachedPercentage = it }

/** `Percentage` vector, built on first access and reused afterwards. */
private var cachedPercentage: ImageVector? = null
