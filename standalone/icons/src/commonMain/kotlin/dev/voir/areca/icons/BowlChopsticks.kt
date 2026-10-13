package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bowl chopsticks icon.
 *
 * Converted from Tabler Icons `bowl-chopsticks` (MIT).
 */
public val ArecaIcons.BowlChopsticks: ImageVector
    get() = cachedBowlChopsticks ?: ImageVector.Builder(
        name = "BowlChopsticks",
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
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 12f)
            verticalLineTo(12.5f)
            curveTo(21f, 14f, 18.483f, 18.073f, 17f, 19f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 16f, 21f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 7f, 20f)
            verticalLineTo(19f)
            curveTo(5.313f, 17.946f, 3f, 14f, 3f, 12.5f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, true, 4f, 11f)
            moveTo(19f, 7f)
            lineTo(5f, 8f)
            moveTo(19f, 2f)
            lineTo(5f, 5f)
        }
    }.build().also { cachedBowlChopsticks = it }

/** `BowlChopsticks` vector, built on first access and reused afterwards. */
private var cachedBowlChopsticks: ImageVector? = null
