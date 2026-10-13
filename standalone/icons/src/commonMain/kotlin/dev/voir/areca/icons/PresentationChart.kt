package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Presentation analytics icon.
 *
 * Converted from Tabler Icons `presentation-analytics` (MIT).
 */
public val ArecaIcons.PresentationChart: ImageVector
    get() = cachedPresentationChart ?: ImageVector.Builder(
        name = "PresentationChart",
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
            moveTo(9f, 12f)
            verticalLineTo(8f)
            moveTo(15f, 12f)
            verticalLineTo(10f)
            moveTo(12f, 12f)
            verticalLineTo(11f)
            moveTo(3f, 4f)
            horizontalLineTo(21f)
            moveTo(4f, 4f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, false, 6f, 16f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 14f)
            verticalLineTo(4f)
            moveTo(12f, 16f)
            verticalLineTo(20f)
            moveTo(9f, 20f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedPresentationChart = it }

/** `PresentationChart` vector, built on first access and reused afterwards. */
private var cachedPresentationChart: ImageVector? = null
