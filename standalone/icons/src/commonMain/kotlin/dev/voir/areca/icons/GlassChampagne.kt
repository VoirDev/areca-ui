package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Glass champagne icon.
 *
 * Converted from Tabler Icons `glass-champagne` (MIT).
 */
public val ArecaIcons.GlassChampagne: ImageVector
    get() = cachedGlassChampagne ?: ImageVector.Builder(
        name = "GlassChampagne",
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
            moveTo(9f, 21f)
            horizontalLineTo(15f)
            moveTo(12f, 16f)
            verticalLineTo(21f)
            moveTo(8f, 5f)
            arcTo(4f, 2f, 0f, true, false, 16f, 5f)
            arcTo(4f, 2f, 0f, true, false, 8f, 5f)
            moveTo(8f, 5f)
            curveTo(8f, 11.075f, 9.79f, 16f, 12f, 16f)
            reflectiveCurveTo(16f, 11.075f, 16f, 5f)
        }
    }.build().also { cachedGlassChampagne = it }

/** `GlassChampagne` vector, built on first access and reused afterwards. */
private var cachedGlassChampagne: ImageVector? = null
