package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Goal icon.
 *
 * Converted from Lucide `goal` (ISC).
 */
public val ArecaIcons.Goal: ImageVector
    get() = cachedGoal ?: ImageVector.Builder(
        name = "Goal",
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
            moveTo(12f, 13f)
            verticalLineTo(2f)
            lineTo(20f, 6f)
            lineTo(12f, 10f)
            moveTo(20.561f, 10.222f)
            arcTo(9f, 9f, 0f, true, true, 8.011f, 4.932f)
            moveTo(8.002f, 9.997f)
            arcTo(5f, 5f, 0f, true, false, 16.902f, 12.017f)
        }
    }.build().also { cachedGoal = it }

/** `Goal` vector, built on first access and reused afterwards. */
private var cachedGoal: ImageVector? = null
