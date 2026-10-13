package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball american football icon.
 *
 * Converted from Tabler Icons `ball-american-football` (MIT).
 */
public val ArecaIcons.BallAmericanFootball: ImageVector
    get() = cachedBallAmericanFootball ?: ImageVector.Builder(
        name = "BallAmericanFootball",
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
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(10f, 12f)
            lineTo(12f, 14f)
            moveTo(12f, 10f)
            lineTo(14f, 12f)
            moveTo(8f, 21f)
            arcTo(5f, 5f, 0f, false, false, 3f, 16f)
            moveTo(16f, 3f)
            curveTo(8.82f, 3f, 3f, 8.82f, 3f, 16f)
            arcTo(5f, 5f, 0f, false, false, 8f, 21f)
            curveTo(15.18f, 21f, 21f, 15.18f, 21f, 8f)
            arcTo(5f, 5f, 0f, false, false, 16f, 3f)
            moveTo(16f, 3f)
            arcTo(5f, 5f, 0f, false, false, 21f, 8f)
        }
    }.build().also { cachedBallAmericanFootball = it }

/** `BallAmericanFootball` vector, built on first access and reused afterwards. */
private var cachedBallAmericanFootball: ImageVector? = null
