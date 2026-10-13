package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball bowling icon.
 *
 * Converted from Tabler Icons `ball-bowling` (MIT).
 */
public val ArecaIcons.BallBowling: ImageVector
    get() = cachedBallBowling ?: ImageVector.Builder(
        name = "BallBowling",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(11f, 9f)
            lineTo(11f, 9.01f)
            moveTo(15f, 8f)
            lineTo(15f, 8.01f)
            moveTo(14f, 12f)
            lineTo(14f, 12.01f)
        }
    }.build().also { cachedBallBowling = it }

/** `BallBowling` vector, built on first access and reused afterwards. */
private var cachedBallBowling: ImageVector? = null
